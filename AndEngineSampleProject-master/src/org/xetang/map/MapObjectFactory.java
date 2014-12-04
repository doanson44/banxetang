package org.xetang.map;

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
		Eagle, BrickWall, SteelWall, Bush, Water, Ice, Bullet, Blast, Explosion, SlowBullet, FastBullet, 
		Bomb, Clock, Helmet, Shovel, Star, TankItem,  PlayerTank, EnermyTank
	};

	public enum TankType{
		BigMom, GlassCannon, Normal, Racer
	}
	public static final int MAX_RESOURCE_BITMAP_WIDTH = 384;
	public static final int MAX_RESOURCE_BITMAP_HEIGHT = 512;

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

	public static final short CATEGORYBIT_DEFAULT = 1;
	public static final short CATEGORYBIT_WATER = 2;
	public static final short CATEGORYBIT_BULLET = 4;

	public static final short MASKBITS_DEFAULT = CATEGORYBIT_DEFAULT
			| CATEGORYBIT_WATER | CATEGORYBIT_BULLET;
	public static final short MASKBITS_WATER = CATEGORYBIT_DEFAULT
			| CATEGORYBIT_WATER;
	public static final short MASKBITS_BULLET = CATEGORYBIT_DEFAULT
			| CATEGORYBIT_BULLET;

	public static final short GROUP_DEFAULT = 0;

	public static final int Z_INDEX_DEFAULT = 0;
	public static final int Z_INDEX_BUSH = 10;
	public static final int Z_INDEX_BULLET = 5;
	public static final int Z_INDEX_BLAST = 15;
	public static final int Z_INDEX_EXPLOSION = 20;

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

	public static final float BLOW_RADIUS_RATIO_WIDTH = 2 / 8f;
	public static final float BLOW_RADIUS_RATIO_DEPTH = 0f;
	public static final Vector2 NORMAL_BULLET_BLOW_RADIUS = new Vector2(
			CalcHelper.pixels2Meters(GameManager.LARGE_CELL_WIDTH
					* BLOW_RADIUS_RATIO_WIDTH),
			CalcHelper.pixels2Meters(GameManager.LARGE_CELL_HEIGHT
					* BLOW_RADIUS_RATIO_DEPTH));
	public static final Vector2 SLOW_BULLET_BLOW_RADIUS = NORMAL_BULLET_BLOW_RADIUS
			.cpy();
	public static final Vector2 FAST_BULLET_BLOW_RADIUS = new Vector2(
			CalcHelper.pixels2Meters(GameManager.LARGE_CELL_WIDTH
					* BLOW_RADIUS_RATIO_WIDTH),
			CalcHelper.pixels2Meters(GameManager.LARGE_CELL_HEIGHT
					* BLOW_RADIUS_RATIO_WIDTH));

	private static SparseArray<IMapObject> _objectsArray = new SparseArray<IMapObject>();

	/**
	 * Bitmap lÆ°u texture cá»§a táº¥t cáº£ cÃ¡c Ä‘á»‘i tÆ°á»£ng
	 */
	private static BitmapTextureAtlas _bitmapTextureAtlas;
	
	private static TiledTextureRegion _eagleTextureRegion;
	private static TiledTextureRegion _brickWallTextureRegion;
	private static TiledTextureRegion _steelWallTextureRegion;
	private static TiledTextureRegion _bushTextureRegion;
	private static TiledTextureRegion _waterTextureRegion;
	private static TiledTextureRegion _iceTextureRegion;
	private static TiledTextureRegion _bulletTextureRegion;
	private static TiledTextureRegion _blastTextureRegion;
	private static TiledTextureRegion _explosionTextureRegion;
	
	private static FixtureDef _eagleFixtureDef;
	private static FixtureDef _brickWallFixtureDef;
	private static FixtureDef _steelWallFixtureDef;
	// private static FixtureDef _bushFixtureDef;
	private static FixtureDef _waterFixtureDef;
	private static FixtureDef _iceFixtureDef;
	private static FixtureDef _bulletFixtureDef;

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
				MAX_RESOURCE_BITMAP_WIDTH, MAX_RESOURCE_BITMAP_HEIGHT
						* (ObjectType.Explosion.ordinal() + 1),
				TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");

		int yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT
				* ObjectType.Eagle.ordinal();
		String strTexture = XMLLoader.getObject(ObjectType.Eagle.ordinal())
				.getTextures();
		_eagleTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 2, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT
				* ObjectType.BrickWall.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.BrickWall.ordinal())
				.getTextures();
		_brickWallTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT
				* ObjectType.SteelWall.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.SteelWall.ordinal())
				.getTextures();
		_steelWallTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT * ObjectType.Bush.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Bush.ordinal())
				.getTextures();
		_bushTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT * ObjectType.Water.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Water.ordinal())
				.getTextures();
		_waterTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT * ObjectType.Ice.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Ice.ordinal())
				.getTextures();
		_iceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT * ObjectType.Bullet.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Bullet.ordinal())
				.getTextures();
		_bulletTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT * ObjectType.Blast.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Blast.ordinal())
				.getTextures();
		_blastTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 3, 4);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT
				* ObjectType.Explosion.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Explosion.ordinal())
				.getTextures();
		_explosionTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Activity.getAssets(), strTexture, 0,
						yTexturePos, 3, 4);

		_bitmapTextureAtlas.load();
	}

	private static void initObjectsFixture() {

		_eagleFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
				CATEGORYBIT_DEFAULT, MASKBITS_DEFAULT, GROUP_DEFAULT);

		_brickWallFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f,
				false, CATEGORYBIT_DEFAULT, MASKBITS_DEFAULT, GROUP_DEFAULT);

		_steelWallFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f,
				false, CATEGORYBIT_DEFAULT, MASKBITS_DEFAULT, GROUP_DEFAULT);

		// _bushFixtureDef = PhysicsFactory.createFixtureDef(0f, 0f, 0f, true);

		_waterFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
				CATEGORYBIT_WATER, MASKBITS_WATER, GROUP_DEFAULT);
		
		_iceFixtureDef = PhysicsFactory.createFixtureDef(0f, 0f, 0f, true);

		_bulletFixtureDef = PhysicsFactory.createFixtureDef(BULLET_DENSITY,
				BULLET_ELASTICITY, BULLET_FRICTION, false, CATEGORYBIT_BULLET,
				MASKBITS_BULLET, GROUP_DEFAULT);
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
	 * Khá»Ÿi táº¡o cÃ¡c loáº¡i Bullet cÃ³ trong trÃ² chÆ¡i
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
		 * Ná»• xong thÃ¬ há»§y Ä‘á»‘i tÆ°á»£ng BlowUp (tan theo mÃ¢y khÃ³i)
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
	 * Há»§y toÃ n bá»™ Resource trong Factory
	 */
	public static void unloadAll() {
		_bitmapTextureAtlas.unload();
	}

	/**
	 * Táº¡o má»™t Ä‘á»‘i tÆ°á»£ng má»›i
	 * @param type : Loáº¡i Ä‘á»‘i tÆ°á»£ng (trong <code>GameManager.ObjectType</code>)
	 * @return Má»™t Ä‘á»‘i tÆ°á»£ng má»›i cÃ¹ng loáº¡i
	 */
	public static IMapObject createObject(ObjectType type) {
		return createObject(type, 0, 0);
	}


	/**
	 * Táº¡o má»™t Ä‘á»‘i tÆ°á»£ng má»›i táº¡i tá»�a Ä‘á»™ tÆ°Æ¡ng á»©ng
	 * @param type : Loáº¡i Ä‘á»‘i tÆ°á»£ng (trong <code>GameManager.ObjectType</code>)
	 * @return Má»™t Ä‘á»‘i tÆ°á»£ng má»›i cÃ¹ng loáº¡i táº¡i tá»�a Ä‘á»™ tÆ°Æ¡ng á»©ng
	 */
	public static IMapObject createObject(ObjectType type, float posX,
			float posY) {
		IMapObject object = _objectsArray.get(type.ordinal()).clone();
		object.setPosition(posX, posY);
		return object;
	}

	/**
	 * Táº¡o má»™t khá»‘i cÃ¡c dá»‘i tÆ°á»£ng cÃ¹ng loáº¡i
	 * @param type : Loáº¡i Ä‘á»‘i tÆ°á»£ng (trong <code>GameManager.ObjectType</code>)
	 * @param posAndSize : Cáº·p <b>tá»�a Ä‘á»™</b> vÃ  <b>vá»‹ trÃ­</b> cá»§a khá»‘i Ä‘á»‘i tÆ°á»£ng
	 * @return Má»™t khá»‘i Ä‘á»‘i tÆ°á»£ng Ä‘Ã£ Ä‘Æ°á»£c khá»Ÿi táº¡o
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

	public static TiledTextureRegion getEagleTextureRegion() {
		return _eagleTextureRegion;
	}

	public static TiledTextureRegion getBrickWallTextureRegion() {
		return _brickWallTextureRegion;
	}

	public static TiledTextureRegion getSteelWallTextureRegion() {
		return _steelWallTextureRegion;
	}

	public static TiledTextureRegion getBushTextureRegion() {
		return _bushTextureRegion;
	}

	public static TiledTextureRegion getWaterTextureRegion() {
		return _waterTextureRegion;
	}

	public static TiledTextureRegion getIceTextureRegion() {
		return _iceTextureRegion;
	}

	public static TiledTextureRegion getBulletTextureRegion() {
		return _bulletTextureRegion;
	}

	public static TiledTextureRegion getBlastTextureRegion() {
		return _blastTextureRegion;
	}

	public static TiledTextureRegion getExplosionTextureRegion() {
		return _explosionTextureRegion;
	}

	public static FixtureDef getEagleFixtureDef() {
		return _eagleFixtureDef;
	}

	public static FixtureDef getBrickWallFixtureDef() {
		return _brickWallFixtureDef;
	}

	public static FixtureDef getSteelWallFixtureDef() {
		return _steelWallFixtureDef;
	}

	// public static FixtureDef getBushFixtureDef() {
	// return _bushFixtureDef;
	// }

	public static FixtureDef getWaterFixtureDef() {
		return _waterFixtureDef;
	}

	public static FixtureDef getIceFixtureDef() {
		return _iceFixtureDef;
	}

	public static FixtureDef getBulletFixtureDef() {
		return _bulletFixtureDef;
	}

	public static IAnimationListener getBlowUpListener() {
		return _blowUpListener;
	}
}
