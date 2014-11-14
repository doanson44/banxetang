package org.xetang.map;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObject.ObjectType;
import org.xetang.map.model.MapObjectBlockDTO;
import org.xetang.map.model.XMLLoader;

import android.graphics.Point;
import android.util.Pair;
import android.util.SparseArray;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class MapObjectFactory {

	public static final int MAX_RESOURCE_BITMAP_WIDTH = 64;
	public static final int MAX_RESOURCE_BITMAP_HEIGHT = 64;

	public static final int EAGLE_CELL_PER_MAP = 13;
	public static final int BRICK_WALL_CELL_PER_MAP = 52;
	public static final int STEEL_WALL_CELL_PER_MAP = 26;
	public static final int BUSH_CELL_PER_MAP = 13;
	public static final int WATER_CELL_PER_MAP = 13;
	public static final int BULLET_CELL_PER_MAP = 65;

	public static final float BULLET_DENSITY = 0.5f;
	public static final float BULLET_ELASTICITY = 0.5f;
	public static final float BULLET_FRICTION = 1f;

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

	private static SparseArray<IMapObject> _objectsArray = new SparseArray<IMapObject>();

	protected static BitmapTextureAtlas _bitmapTextureAtlas;
	protected static TiledTextureRegion _eagleTextureRegion;
	protected static TiledTextureRegion _brickWallTextureRegion;
	protected static TiledTextureRegion _steelWallTextureRegion;
	protected static TiledTextureRegion _bushTextureRegion;
	protected static TiledTextureRegion _waterTextureRegion;
	protected static TiledTextureRegion _bulletTextureRegion;
	protected static FixtureDef _eagleFixtureDef;
	protected static FixtureDef _brickWallFixtureDef;
	protected static FixtureDef _steelWallFixtureDef;
	protected static FixtureDef _bushFixtureDef;
	protected static FixtureDef _waterFixtureDef;
	protected static FixtureDef _bulletFixtureDef;

	public static void initAllObjects() {

		initObjectsTexture();
		initObjectsFixture();
		createObjectsArray();
	}

	private static void initObjectsTexture() {
		_bitmapTextureAtlas = new BitmapTextureAtlas(
				GameManager.Context.getTextureManager(),
				MAX_RESOURCE_BITMAP_WIDTH, MAX_RESOURCE_BITMAP_HEIGHT
						* ObjectType.values().length, TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");

		int yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT
				* ObjectType.Eagle.ordinal();
		String strTexture = XMLLoader.getObject(ObjectType.Eagle.ordinal())
				.getTextures();
		_eagleTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Context.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT
				* ObjectType.BrickWall.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.BrickWall.ordinal())
				.getTextures();
		_brickWallTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Context.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT
				* ObjectType.SteelWall.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.SteelWall.ordinal())
				.getTextures();
		_steelWallTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Context.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT * ObjectType.Bush.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Bush.ordinal())
				.getTextures();
		_bushTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Context.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT * ObjectType.Water.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Water.ordinal())
				.getTextures();
		_waterTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Context.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		yTexturePos = MAX_RESOURCE_BITMAP_HEIGHT * ObjectType.Bullet.ordinal();
		strTexture = XMLLoader.getObject(ObjectType.Bullet.ordinal())
				.getTextures();
		_bulletTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(_bitmapTextureAtlas,
						GameManager.Context.getAssets(), strTexture, 0,
						yTexturePos, 1, 1);

		_bitmapTextureAtlas.load();
	}

	private static void initObjectsFixture() {

		_eagleFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
				CATEGORYBIT_DEFAULT, MASKBITS_DEFAULT, GROUP_DEFAULT);

		_brickWallFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f,
				false, CATEGORYBIT_DEFAULT, MASKBITS_DEFAULT, GROUP_DEFAULT);

		_steelWallFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f,
				false, CATEGORYBIT_DEFAULT, MASKBITS_DEFAULT, GROUP_DEFAULT);

		_bushFixtureDef = PhysicsFactory.createFixtureDef(0f, 0f, 0f, true);

		_waterFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
				CATEGORYBIT_WATER, MASKBITS_WATER, GROUP_DEFAULT);

		_bulletFixtureDef = PhysicsFactory.createFixtureDef(BULLET_DENSITY,
				BULLET_ELASTICITY, BULLET_FRICTION, false, CATEGORYBIT_BULLET,
				MASKBITS_BULLET, GROUP_DEFAULT);
	}

	private static void createObjectsArray() {
		_objectsArray.put(ObjectType.Eagle.ordinal(), new Eagle(0, 0));
		_objectsArray.put(ObjectType.BrickWall.ordinal(), new BrickWall(0, 0));
		_objectsArray.put(ObjectType.SteelWall.ordinal(), new SteelWall(0, 0));
		_objectsArray.put(ObjectType.Bush.ordinal(), new Bush(0, 0));
		_objectsArray.put(ObjectType.Water.ordinal(), new Water(0, 0));
		_objectsArray.put(ObjectType.Bullet.ordinal(), new Bullet(0, 0));
	}

	public static void unloadAll() {
		_bitmapTextureAtlas.unload();
	}

	public static IMapObject createObject(ObjectType type) {
		return _objectsArray.get(type.ordinal()).clone();
	}

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

	public static TiledTextureRegion getBulletTextureRegion() {
		return _bulletTextureRegion;
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

	public static FixtureDef getBushFixtureDef() {
		return _bushFixtureDef;
	}

	public static FixtureDef getWaterFixtureDef() {
		return _waterFixtureDef;
	}

	public static FixtureDef getBulletFixtureDef() {
		return _bulletFixtureDef;
	}
}
