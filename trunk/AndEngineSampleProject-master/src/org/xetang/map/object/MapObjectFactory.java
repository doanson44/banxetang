package org.xetang.map.object;

import java.util.HashMap;
import java.util.Map;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.map.model.MapObjectBlockDTO;
import org.xetang.map.model.XMLLoader;

import android.graphics.Point;
import android.util.Pair;
import android.util.SparseArray;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class MapObjectFactory {

	public enum ObjectType {
		EAGLE, BRICK_WALL, STEEL_WALL, BUSH, WATER, ICE, BULLET, BLAST, EXPLOSION, SLOW_BULLET, FAST_BULLET, DRILL_BULLET, BLOW_BULLET, BOMB, CLOCK, HELMET, SHOVEL, STAR, TANK_ITEM, PLAYER_TANK, ENERMY_TANK
	};

	public enum ObjectLayer {
		CONSTRUCTION, MOVING, WRAPPER, BLOW_UP
	};

	public enum TankType {
		BIG_MOM, GLASS_CANNON, NORMAL, RACER
	}

	public static final int MAX_RESOURCE_BITMAP_WIDTH = 768;
	public static final int MAX_RESOURCE_BITMAP_HEIGHT = 576;

	private static final Point EAGLE_TEXTURE_POS = new Point(0, 0);
	private static final Point BRICKWALL_TEXTURE_POS = new Point(352, 0);
	private static final Point STEELWALL_TEXTURE_POS = new Point(320, 0);
	private static final Point BUSH_TEXTURE_POS = new Point(128, 0);
	private static final Point WATER_TEXTURE_POS = new Point(192, 0);
	private static final Point ICE_TEXTURE_POS = new Point(256, 0);
	private static final Point BULLET_TEXTURE_POS = new Point(368, 0);
	private static final Point BLAST_TEXTURE_POS = new Point(0, 64);
	private static final Point EXPLOSION_TEXTURE_POS = new Point(384, 64);

	public static final float EAGLE_CELL_SIZE = GameManager.LARGE_CELL_SIZE;
	public static final float BRICK_WALL_CELL_SIZE = EAGLE_CELL_SIZE / 4f;
	public static final float STEEL_WALL_CELL_SIZE = EAGLE_CELL_SIZE / 2f;
	public static final float BUSH_CELL_SIZE = EAGLE_CELL_SIZE;
	public static final float WATER_CELL_SIZE = EAGLE_CELL_SIZE;
	public static final float ICE_CELL_SIZE = EAGLE_CELL_SIZE;
	public static final float BULLET_CELL_SIZE = EAGLE_CELL_SIZE / 5f;
	public static final float BLAST_CELL_SIZE = EAGLE_CELL_SIZE * 1.5f;
	public static final float EXPLOSION_CELL_SIZE = EAGLE_CELL_SIZE * 3f;
	public static final float TINY_CELL_SIZE = EAGLE_CELL_SIZE / 8f;

	public static final float BULLET_DENSITY = 0.5f;
	public static final float BULLET_ELASTICITY = 0f;
	public static final float BULLET_FRICTION = 1f;

	public static final int BLAST_ANIMATE = 45;
	public static final int EXPLOSION_ANIMATE = 85;

	private static final short CATEGORYBIT_DEFAULT = 1;
	private static final short CATEGORYBIT_WATER = 2;
	private static final short CATEGORYBIT_BULLET = 4;

	private static final short MASKBITS_DEFAULT = CATEGORYBIT_DEFAULT
			| CATEGORYBIT_WATER | CATEGORYBIT_BULLET;
	private static final short MASKBITS_WATER = CATEGORYBIT_DEFAULT
			| CATEGORYBIT_WATER;
	private static final short MASKBITS_BULLET = CATEGORYBIT_DEFAULT
			| CATEGORYBIT_BULLET;

	private static final short GROUP_DEFAULT = 0;

	public static final int Z_INDEX_CONSTRUCTION = 0;
	public static final int Z_INDEX_WRAPPER = 10;
	public static final int Z_INDEX_MOVING = 5;
	public static final int Z_INDEX_BLOW_UP = 15;

	public static final int SLOW_BULLET_DAMAGE = 1;
	public static final int NORMAL_BULLET_DAMAGE = 1;
	public static final int FAST_BULLET_DAMAGE = 1;
	public static final int DRILL_BULLET_DAMAGE = 2;
	public static final int BLOW_BULLET_DAMAGE = 3;

	public static final float NORMAL_BULLET_SPEED = CalcHelper
			.pixels2Meters(GameManager.MAP_SIZE);
	public static final float SLOW_BULLET_SPEED = NORMAL_BULLET_SPEED / 3f * 2f;
	public static final float FAST_BULLET_SPEED = NORMAL_BULLET_SPEED / 2f * 3f;

	public static final float BLOW_RADIUS_RATIO = 5 / 16f;
	public static final Vector2 NORMAL_BULLET_BLOW_RADIUS = new Vector2(
			CalcHelper.pixels2Meters(GameManager.LARGE_CELL_SIZE
					* BLOW_RADIUS_RATIO), 0f);
	public static final Vector2 POWER_BULLET_BLOW_RADIUS = new Vector2(
			NORMAL_BULLET_BLOW_RADIUS.x, NORMAL_BULLET_BLOW_RADIUS.x);

	private static SparseArray<IMapObject> _objectsArray = new SparseArray<IMapObject>();

	/**
	 * Bitmap lưu texture của tất cả các đối tượng
	 */
	private static BitmapTextureAtlas _bitmapTextureAtlas;
	private static Map<ObjectType, TiledTextureRegion> _textureRegionMap = new HashMap<ObjectType, TiledTextureRegion>(
			10);
	private static Map<ObjectType, FixtureDef> _fixtureDefMap = new HashMap<ObjectType, FixtureDef>(
			10);

	private static Vector2 _bulletSize;
	private static IAnimationListener _blowUpListener;

	public static void initAllObjects() {

		initObjectsTexture();
		initObjectsFixture();
		createObjectsArray();
		createObjectsListener();
	}

	private static void initObjectsTexture() {

		_bitmapTextureAtlas = new BitmapTextureAtlas(
				GameManager.Activity.getTextureManager(),
				MAX_RESOURCE_BITMAP_WIDTH, MAX_RESOURCE_BITMAP_HEIGHT,
				TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");

		String strTexture = XMLLoader.getObject(ObjectType.EAGLE.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.EAGLE,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, EAGLE_TEXTURE_POS.x, EAGLE_TEXTURE_POS.y,
						2, 1));

		strTexture = XMLLoader.getObject(ObjectType.BRICK_WALL.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.BRICK_WALL,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, BRICKWALL_TEXTURE_POS.x,
						BRICKWALL_TEXTURE_POS.y, 1, 1));

		strTexture = XMLLoader.getObject(ObjectType.STEEL_WALL.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.STEEL_WALL,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, STEELWALL_TEXTURE_POS.x,
						STEELWALL_TEXTURE_POS.y, 1, 1));

		strTexture = XMLLoader.getObject(ObjectType.BUSH.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.BUSH,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, BUSH_TEXTURE_POS.x, BUSH_TEXTURE_POS.y, 1,
						1));

		strTexture = XMLLoader.getObject(ObjectType.WATER.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.WATER,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, WATER_TEXTURE_POS.x, WATER_TEXTURE_POS.y,
						1, 1));

		strTexture = XMLLoader.getObject(ObjectType.ICE.ordinal())
				.getTextures();
		_textureRegionMap
				.put(ObjectType.ICE, BitmapTextureAtlasTextureRegionFactory
						.createTiledFromAsset(_bitmapTextureAtlas,
								GameManager.Activity.getAssets(), strTexture,
								ICE_TEXTURE_POS.x, ICE_TEXTURE_POS.y, 1, 1));

		strTexture = XMLLoader.getObject(ObjectType.BULLET.ordinal())
				.getTextures();
		TiledTextureRegion bulletTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture,
						BULLET_TEXTURE_POS.x, BULLET_TEXTURE_POS.y, 1, 1);
		_textureRegionMap.put(ObjectType.BULLET, bulletTextureRegion);
		_bulletSize = new Vector2(BULLET_CELL_SIZE, BULLET_CELL_SIZE
				* bulletTextureRegion.getHeight()
				/ bulletTextureRegion.getWidth());

		strTexture = XMLLoader.getObject(ObjectType.BLAST.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.BLAST,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, BLAST_TEXTURE_POS.x, BLAST_TEXTURE_POS.y,
						3, 4));

		strTexture = XMLLoader.getObject(ObjectType.EXPLOSION.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.EXPLOSION,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, EXPLOSION_TEXTURE_POS.x,
						EXPLOSION_TEXTURE_POS.y, 3, 4));

		_bitmapTextureAtlas.load();
	}

	private static void initObjectsFixture() {

		_fixtureDefMap.put(ObjectType.EAGLE, PhysicsFactory.createFixtureDef(
				1f, 0f, 0f, false, CATEGORYBIT_DEFAULT, MASKBITS_DEFAULT,
				GROUP_DEFAULT));

		_fixtureDefMap.put(ObjectType.BRICK_WALL, PhysicsFactory
				.createFixtureDef(1f, 0f, 0f, false, CATEGORYBIT_DEFAULT,
						MASKBITS_DEFAULT, GROUP_DEFAULT));

		_fixtureDefMap.put(ObjectType.STEEL_WALL, PhysicsFactory
				.createFixtureDef(1f, 0f, 0f, false, CATEGORYBIT_DEFAULT,
						MASKBITS_DEFAULT, GROUP_DEFAULT));

		// _bushFixtureDef = PhysicsFactory.createFixtureDef(0f, 0f, 0f, true);

		_fixtureDefMap.put(ObjectType.WATER, PhysicsFactory.createFixtureDef(
				1f, 0f, 0f, false, CATEGORYBIT_WATER, MASKBITS_WATER,
				GROUP_DEFAULT));

		_fixtureDefMap.put(ObjectType.ICE,
				PhysicsFactory.createFixtureDef(0f, 0f, 0f, true));

		_fixtureDefMap.put(ObjectType.BULLET, PhysicsFactory.createFixtureDef(
				BULLET_DENSITY, BULLET_ELASTICITY, BULLET_FRICTION, false,
				CATEGORYBIT_BULLET, MASKBITS_BULLET, GROUP_DEFAULT));
	}

	private static void createObjectsArray() {
		_objectsArray.put(ObjectType.EAGLE.ordinal(), new Eagle(0f, 0f));
		_objectsArray.put(ObjectType.BRICK_WALL.ordinal(),
				new BrickWall(0f, 0f));
		_objectsArray.put(ObjectType.STEEL_WALL.ordinal(),
				new SteelWall(0f, 0f));
		_objectsArray.put(ObjectType.BUSH.ordinal(), new Bush(0f, 0f));
		_objectsArray.put(ObjectType.WATER.ordinal(), new Water(0f, 0f));
		_objectsArray.put(ObjectType.ICE.ordinal(), new Ice(0f, 0f));
		_objectsArray.put(ObjectType.BLAST.ordinal(), new Blast(0f, 0f));
		_objectsArray
				.put(ObjectType.EXPLOSION.ordinal(), new Explosion(0f, 0f));

		createAllBullets();
	}

	/**
	 * Khởi tạo các loại Bullet có trong trò chơi
	 */
	private static void createAllBullets() {
		IBullet bullet = new Bullet();
		_objectsArray.put(ObjectType.BULLET.ordinal(), bullet);

		bullet = new Bullet();
		bullet.initSpecification(SLOW_BULLET_DAMAGE, SLOW_BULLET_SPEED,
				NORMAL_BULLET_BLOW_RADIUS);
		_objectsArray.put(ObjectType.SLOW_BULLET.ordinal(), bullet);

		bullet = new Bullet();
		bullet.initSpecification(FAST_BULLET_DAMAGE, FAST_BULLET_SPEED,
				NORMAL_BULLET_BLOW_RADIUS);
		_objectsArray.put(ObjectType.FAST_BULLET.ordinal(), bullet);

		bullet = new Bullet();
		bullet.initSpecification(DRILL_BULLET_DAMAGE, FAST_BULLET_SPEED,
				POWER_BULLET_BLOW_RADIUS);
		_objectsArray.put(ObjectType.DRILL_BULLET.ordinal(), bullet);

		bullet = new Bullet();
		bullet.initSpecification(BLOW_BULLET_DAMAGE, FAST_BULLET_SPEED,
				POWER_BULLET_BLOW_RADIUS);
		_objectsArray.put(ObjectType.BLOW_BULLET.ordinal(), bullet);
	}

	private static void createObjectsListener() {

		/*
		 * Nổ xong thì hủy đối tượng BlowUp (tan theo mây khói)
		 */
		_blowUpListener = new IAnimationListener() {

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
					int pRemainingLoopCount, int pInitialLoopCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
					int pOldFrameIndex, int pNewFrameIndex) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				DestroyHelper.add((IMapObject) pAnimatedSprite.getParent());
			}
		};
	}

	/**
	 * Hủy toàn bộ Resource trong Factory
	 */
	public static void unloadAll() {
		_bitmapTextureAtlas.unload();
	}

	/**
	 * Tạo một đối tượng mới
	 * 
	 * @param type
	 *            : Loại đối tượng (trong <code>GameManager.ObjectType</code>)
	 * @return Một đối tượng mới cùng loại
	 */
	public static IMapObject createObject(ObjectType type) {
		return createObject(type, 0, 0);
	}

	/**
	 * Tạo một đối tượng mới tại tỿa độ tương ứng
	 * 
	 * @param type
	 *            : Loại đối tượng (trong <code>GameManager.ObjectType</code>)
	 * @return Một đối tượng mới cùng loại tại tỿa độ tương ứng
	 */
	public static IMapObject createObject(ObjectType type, float posX,
			float posY) {
		IMapObject object = _objectsArray.get(type.ordinal()).clone();
		object.setPosition(posX, posY);
		return object;
	}

	/**
	 * Tạo một khối các dối tượng cùng loại
	 * 
	 * @param type
	 *            : Loại đối tượng (trong <code>GameManager.ObjectType</code>)
	 * @param posAndSize
	 *            : Cặp <b>tỿa độ</b> và <b>vị trí</b> của khối đối tượng
	 * @return Một khối đối tượng đã được khởi tạo
	 */
	public static MapObjectBlockDTO createObjectBlock(ObjectType type,
			Pair<Point, Point> posAndSize) {

		IMapObject object = createObject(type);
		MapObjectBlockDTO objectsBlock = new MapObjectBlockDTO((int) object
				.getSprite().getWidth(), (int) object.getSprite().getHeight(),
				posAndSize);

		for (int i = 0; i < posAndSize.second.y; i++) {
			for (int j = 0; j < posAndSize.second.x; j++) {
				objectsBlock.add(object.clone(), i, j);
			}
		}

		return objectsBlock;
	}

	public static BitmapTextureAtlas getBitmapTextureAtlas() {
		return _bitmapTextureAtlas;
	}

	public static IAnimationListener getBlowUpListener() {
		return _blowUpListener;
	}

	public static TiledTextureRegion getTextureRegion(ObjectType type) {
		return _textureRegionMap.get(type);
	}

	public static FixtureDef getFixtureDef(ObjectType type) {
		return _fixtureDefMap.get(type);
	}

	public static Vector2 getBulletSize() {
		return _bulletSize;
	}
}
