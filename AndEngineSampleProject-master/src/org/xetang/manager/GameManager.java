package org.xetang.manager;

import java.util.Hashtable;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xetang.main.MainActivity;

import android.content.res.AssetManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameManager {

	public static final String TANK_TAG = "XeTang"; // Dùng để debug

	public enum Direction {
		Up, Right, Down, Left
	}
	public static final int CAMERA_WIDTH = 1080;
	public static final int CAMERA_HEIGHT = CAMERA_WIDTH / 10 * 6;

	public static final int MAP_WIDTH = 832;
	public static final int MAP_HEIGHT = MAP_WIDTH / 4 * 3;

	public static final int CAMERA_X = -(CAMERA_WIDTH - MAP_WIDTH) / 2;
	public static final int CAMERA_Y = -(CAMERA_HEIGHT - MAP_HEIGHT) / 2;

	public static final int LARGE_CELL_WIDTH = MAP_WIDTH / 13;
	public static final int LARGE_CELL_HEIGHT = LARGE_CELL_WIDTH / 4 * 3;
	public static final int SMALL_CELL_WIDTH = LARGE_CELL_WIDTH / 2;
	public static final int SMALL_CELL_HEIGHT = SMALL_CELL_WIDTH / 4 * 3;

	public static final int BORDER_WIDTH = 2;

	public static BitmapTextureAtlas mBitmapTexture;
	public static Camera Camera;
	public static Scene Scene;
	public static TextureManager TextureManager;
	public static AssetManager AssetManager;
	public static MainActivity Context;
	public static VertexBufferObjectManager VertexBufferObject;
	public static PhysicsWorld PhysicsWorld = new PhysicsWorld(new Vector2(0,
			0), false);

	public static GameMapManager CurrentMapManager;

	static Hashtable<String, TiledTextureRegion> Textures;
	public static int mStage; // Màn chơi hiện tại
	public static int mPlayTimes; // Số lần chơi game, mỗi khi gameover tính 1
									// lần
	public static int mHighestScore; // Số điểm cao nhất đạt được
	public static int mLifeLeft; // Số mạng còn lại của người chơi

	static {
		mStage = 1;
		mPlayTimes = 0;
		mHighestScore = 0;
	}

	public static void loadGameData() {
		// Load thông tin màn chơi hiện tại
		// ...

		// fake
		mStage = 2;
		mPlayTimes = 2;
		mHighestScore = 1000;
	}

	// public static void newGame() {
	// GameMapManager.loadMapData(1);
	// TankManager.createPlayerTank();
	// TankManager.generateOpponentTank(4);
	// GameControllerManager.setupControls();
	//
	// }
	//
	// public static void createScene() {
	// createWalls();
	// GameManager.newGame();
	// }

	public static void loadResource() {
		// GameMapManager.loadResource();
		// TankManager.loadResouceAllTank();
		// GameItemManager.loadResource();
		// GameControllerManager.loadResource();
		
		Textures = new Hashtable<String, TiledTextureRegion>();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mBitmapTexture = new BitmapTextureAtlas(GameManager.TextureManager, 15, 105, TextureOptions.DEFAULT);

		TiledTextureRegion bomb = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTexture,Context,
					"item/bomb.png", 0, 0,1, 1);

		TiledTextureRegion tank = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, Context, "item/tank.png", 0, 15,1,1);
		TiledTextureRegion clock = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, Context, "item/clock.png",0,30, 1,1);
		TiledTextureRegion gun = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, Context, "item/gun.png", 0, 45,1,1);
		TiledTextureRegion hat = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, Context, "item/helmet.png", 0, 60,1,1);

		TiledTextureRegion shovel = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, Context, "item/shovel.png", 0, 75,1,1);
		TiledTextureRegion star = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, Context, "item/star.png", 0, 90,1,1);
		mBitmapTexture.load();

		
		BitmapTextureAtlas playerTExtureAtlas = new BitmapTextureAtlas(GameManager.TextureManager, 60, 15, TextureOptions.DEFAULT);
		
		TiledTextureRegion player1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTExtureAtlas, Context, "Player1/player1.png", 0, 0,4,1);
		
		playerTExtureAtlas.load();
		
		Textures.put("Bomb", bomb);
		Textures.put("Clock", clock);
		Textures.put("Gun", gun);
		Textures.put("Helmet", hat);
		Textures.put("Shovel", shovel);
		Textures.put("Star", star);
		Textures.put("Tank", tank);
		Textures.put("Player1", player1);

	}

	private static void createWalls() {
		final VertexBufferObjectManager vertexBufferObjectManager = GameManager.VertexBufferObject;
		final Rectangle ground = new Rectangle(0, 420 - 2, 420, 2,
				vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, 420, 2,
				vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, 420,
				vertexBufferObjectManager);
		final Rectangle right = new Rectangle(420 - 2, 0, 2, 420,
				vertexBufferObjectManager);
		// final Rectangle shelf = new Rectangle(300, 200, 100, 2,
		// vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0,
				0.5f, 0.5f);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, ground,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, roof,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, left,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, right,
				BodyType.StaticBody, wallFixtureDef);
		// PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, shelf,
		// BodyType.StaticBody, wallFixtureDef);

		GameManager.Scene.attachChild(ground);
		GameManager.Scene.attachChild(roof);
		GameManager.Scene.attachChild(left);
		GameManager.Scene.attachChild(right);
	}

	/*
	 * ĂN ĐI KU
	 */
	public static void SwitchToScene(Scene currentScene, Scene newScene) {
		/*
		 * Xử lý Scene cũ
		 */

		GameManager.Context.setScene(newScene);
	}

	public static int getCurrentStage() {
		return mStage;
	}

	public static TiledTextureRegion TextureRegion(String key) {
		// TODO Auto-generated method stub

		return Textures.get(key);
	}
}
