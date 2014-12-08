package org.xetang.map.object;

import java.util.HashMap;
import java.util.Map;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.extension.physics.box2d.PhysicsConnector;
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
		Eagle, BrickWall, SteelWall, Bush, Water, Ice, Bullet, Blast, Explosion, SlowBullet, FastBullet, Bomb, Clock, Helmet, Shovel, Star, TankItem, PlayerTank, EnermyTank
	};

	public enum ObjectLayer {
		Construction, Moving, Wrapper, BlowUp
	};

	public enum TankType {
		BigMom, GlassCannon, Normal, Racer
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

	public static final int EAGLE_CELL_PER_MAP = (int) (GameManager.MAP_HEIGHT / GameManager.LARGE_CELL_HEIGHT);
	public static final int BRICK_WALL_CELL_PER_MAP = EAGLE_CELL_PER_MAP * 4;
	public static final int STEEL_WALL_CELL_PER_MAP = EAGLE_CELL_PER_MAP * 2;
	public static final int BUSH_CELL_PER_MAP = EAGLE_CELL_PER_MAP;
	public static final int WATER_CELL_PER_MAP = EAGLE_CELL_PER_MAP;
	public static final int ICE_CELL_PER_MAP = EAGLE_CELL_PER_MAP;
	public static final int BULLET_CELL_PER_MAP = EAGLE_CELL_PER_MAP * 5;
	public static final int BLAST_CELL_PER_MAP = (int) (EAGLE_CELL_PER_MAP / 1.5f);
	public static final int EXPLOSION_CELL_PER_MAP = EAGLE_CELL_PER_MAP / 3;
	public static final int TINY_CELL_PER_MAP = EAGLE_CELL_PER_MAP * 8;

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
	public static final short MASKBITS_BULLET = CATEGORYBIT_DEFAULT
			| CATEGORYBIT_BULLET;

	private static final short GROUP_DEFAULT = 0;

	public static final int Z_INDEX_CONSTRUCTION = 0;
	public static final int Z_INDEX_WRAPPER = 10;
	public static final int Z_INDEX_MOVING = 5;
	public static final int Z_INDEX_BLOW_UP = 15;

	public static final int SLOW_BULLET_DAMAGE = 1;
	public static final int NORMAL_BULLET_DAMAGE = 1;
	public static final int FAST_BULLET_DAMAGE = 2;

	public static final Vector2 NORMAL_BULLET_SPEED = new Vector2(
			GameManager.MAP_WIDTH
					/ PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
			GameManager.MAP_HEIGHT
					/ PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
	public static final Vector2 SLOW_BULLET_SPEED = NORMAL_BULLET_SPEED.cpy()
			.div(2f);
	public static final Vector2 FAST_BULLET_SPEED = NORMAL_BULLET_SPEED.cpy()
			.mul(2f);

	public static final float BLOW_RADIUS_RATIO = 2 / 8f;
	public static final Vector2 NORMAL_BULLET_BLOW_RADIUS = new Vector2(
			CalcHelper.pixels2Meters(GameManager.LARGE_CELL_WIDTH
					* BLOW_RADIUS_RATIO), 0f);
	public static final Vector2 SLOW_BULLET_BLOW_RADIUS = NORMAL_BULLET_BLOW_RADIUS
			.cpy();
	public static final Vector2 FAST_BULLET_BLOW_RADIUS = new Vector2(
			CalcHelper.pixels2Meters(GameManager.LARGE_CELL_WIDTH
					* BLOW_RADIUS_RATIO),
			CalcHelper.pixels2Meters(GameManager.LARGE_CELL_HEIGHT
					* BLOW_RADIUS_RATIO));

	private static SparseArray<IMapObject> _objectsArray = new SparseArray<IMapObject>();

	/**
	 * Bitmap lưu texture của tất cả các đối tượng
	 */
	private static BitmapTextureAtlas _bitmapTextureAtlas;

	private static Map<ObjectType, TiledTextureRegion> _textureRegionMap = new HashMap<ObjectType, TiledTextureRegion>(
			10);
	private static Map<ObjectType, FixtureDef> _fixtureDefMap = new HashMap<ObjectType, FixtureDef>(
			10);

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

		String strTexture = XMLLoader.getObject(ObjectType.Eagle.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.Eagle,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, EAGLE_TEXTURE_POS.x, EAGLE_TEXTURE_POS.y,
						2, 1));

		strTexture = XMLLoader.getObject(ObjectType.BrickWall.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.BrickWall,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, BRICKWALL_TEXTURE_POS.x,
						BRICKWALL_TEXTURE_POS.y, 1, 1));

		strTexture = XMLLoader.getObject(ObjectType.SteelWall.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.SteelWall,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, STEELWALL_TEXTURE_POS.x,
						STEELWALL_TEXTURE_POS.y, 1, 1));

		strTexture = XMLLoader.getObject(ObjectType.Bush.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.Bush,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, BUSH_TEXTURE_POS.x, BUSH_TEXTURE_POS.y, 1,
						1));

		strTexture = XMLLoader.getObject(ObjectType.Water.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.Water,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, WATER_TEXTURE_POS.x, WATER_TEXTURE_POS.y,
						1, 1));

		strTexture = XMLLoader.getObject(ObjectType.Ice.ordinal())
				.getTextures();
		_textureRegionMap
				.put(ObjectType.Ice, BitmapTextureAtlasTextureRegionFactory
						.createTiledFromAsset(_bitmapTextureAtlas,
								GameManager.Activity.getAssets(), strTexture,
								ICE_TEXTURE_POS.x, ICE_TEXTURE_POS.y, 1, 1));

		strTexture = XMLLoader.getObject(ObjectType.Bullet.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.Bullet,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, BULLET_TEXTURE_POS.x, BULLET_TEXTURE_POS.y,
						1, 1));

		strTexture = XMLLoader.getObject(ObjectType.Blast.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.Blast,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, BLAST_TEXTURE_POS.x, BLAST_TEXTURE_POS.y,
						3, 4));

		strTexture = XMLLoader.getObject(ObjectType.Explosion.ordinal())
				.getTextures();
		_textureRegionMap.put(ObjectType.Explosion,
				BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						_bitmapTextureAtlas, GameManager.Activity.getAssets(),
						strTexture, EXPLOSION_TEXTURE_POS.x,
						EXPLOSION_TEXTURE_POS.y, 3, 4));

		_bitmapTextureAtlas.load();
	}

	private static void initObjectsFixture() {

		_fixtureDefMap.put(ObjectType.Eagle, PhysicsFactory.createFixtureDef(
				1f, 0f, 0f, false, CATEGORYBIT_DEFAULT, MASKBITS_DEFAULT,
				GROUP_DEFAULT));

		_fixtureDefMap.put(ObjectType.BrickWall, PhysicsFactory
				.createFixtureDef(1f, 0f, 0f, false, CATEGORYBIT_DEFAULT,
						MASKBITS_DEFAULT, GROUP_DEFAULT));

		_fixtureDefMap.put(ObjectType.SteelWall, PhysicsFactory
				.createFixtureDef(1f, 0f, 0f, false, CATEGORYBIT_DEFAULT,
						MASKBITS_DEFAULT, GROUP_DEFAULT));

		// _bushFixtureDef = PhysicsFactory.createFixtureDef(0f, 0f, 0f, true);

		_fixtureDefMap.put(ObjectType.Water, PhysicsFactory.createFixtureDef(
				1f, 0f, 0f, false, CATEGORYBIT_WATER, MASKBITS_WATER,
				GROUP_DEFAULT));

		_fixtureDefMap.put(ObjectType.Ice,
				PhysicsFactory.createFixtureDef(0f, 0f, 0f, true));

		_fixtureDefMap.put(ObjectType.Bullet, PhysicsFactory.createFixtureDef(
				BULLET_DENSITY, BULLET_ELASTICITY, BULLET_FRICTION, false,
				CATEGORYBIT_BULLET, MASKBITS_BULLET, GROUP_DEFAULT));
	}

	private static void createObjectsArray() {
		_objectsArray.put(ObjectType.Eagle.ordinal(), new Eagle(0f, 0f));
		_objectsArray
				.put(ObjectType.BrickWall.ordinal(), new BrickWall(0f, 0f));
		_objectsArray
				.put(ObjectType.SteelWall.ordinal(), new SteelWall(0f, 0f));
		_objectsArray.put(ObjectType.Bush.ordinal(), new Bush(0f, 0f));
		_objectsArray.put(ObjectType.Water.ordinal(), new Water(0f, 0f));
		_objectsArray.put(ObjectType.Ice.ordinal(), new Ice(0f, 0f));
		_objectsArray.put(ObjectType.Blast.ordinal(), new Blast(0f, 0f));
		_objectsArray
				.put(ObjectType.Explosion.ordinal(), new Explosion(0f, 0f));

		createAllBullets();
	}

	/**
	 * Khởi tạo các loại Bullet có trong trò chơi
	 */
	private static void createAllBullets() {
		IBullet bullet = new Bullet(0f, 0f);
		_objectsArray.put(ObjectType.Bullet.ordinal(), bullet);

		bullet = new Bullet(0f, 0f);
		bullet.initSpecification(SLOW_BULLET_DAMAGE, SLOW_BULLET_SPEED,
				SLOW_BULLET_BLOW_RADIUS);
		_objectsArray.put(ObjectType.SlowBullet.ordinal(), bullet);

		bullet = new Bullet(0f, 0f);
		bullet.initSpecification(FAST_BULLET_DAMAGE, FAST_BULLET_SPEED,
				FAST_BULLET_BLOW_RADIUS);
		_objectsArray.put(ObjectType.FastBullet.ordinal(), bullet);
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
	 * Tạo một đối tượng mới tại t�?a độ tương ứng
	 * 
	 * @param type
	 *            : Loại đối tượng (trong <code>GameManager.ObjectType</code>)
	 * @return Một đối tượng mới cùng loại tại t�?a độ tương ứng
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
	 *            : Cặp <b>t�?a độ</b> và <b>vị trí</b> của khối đối tượng
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
}
