package org.xetang.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.audio.music.*;
import org.andengine.engine.Engine;
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
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.xetang.main.MainActivity;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.model.XMLLoader;
import org.xetang.root.GameScene;
import org.xetang.root.MainMenuScene;

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
/**
	 * @editor: Nhân Bạch
	 * @date: 13/11/2014
	 * @brief: Thêm 1 số biến phục vụ cho việc làm MENU 
	 */
	public static FontManager FontManager;
	public static MusicManager MusicManager;
	static Hashtable<String, BaseTextureRegion> Textures;
	static Hashtable<String, Font> Fonts;
	static Hashtable<String, Music> Musics;
	public static boolean IsBackgroundSound = true;
	public static boolean IsEffectSound = true;
	public static List<Scene> ListScene = new ArrayList<Scene>();
	public static Engine Engine;

	/**********************/

	public static GameMapManager CurrentMapManager;


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
		mStage = 1;
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
		Textures = new Hashtable<String, BaseTextureRegion>();
		Musics = new Hashtable<String, Music>();
		Fonts = new Hashtable<String, Font>();
		
		// Load Fonts
		loadFonts();

		// load Musics
		loadMusics();
		
		//load Textures
		loadTextures();	
		
		GameControllerManager.loadResource();
		
		XMLLoader.loadAllParameters();
		MapObjectFactory.initAllObjects();

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
		
		ListScene.add(new MainMenuScene());
		ListScene.add(new GameScene());

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
	public static void SwitchToScene(int index) {
		/*
		 * Xử lý Scene cũ
		 */
		if(index >=0 && index < ListScene.size() && GameManager.Engine.getScene() != ListScene.get(index)){
			GameManager.Engine.setScene(ListScene.get(index));
			GameManager.Scene = ListScene.get(index);
			if(index==0)
				((MainMenuScene)GameManager.Scene).onSwitched();
			else
				((GameScene)GameManager.Scene).onSwitched();
		}
	}

	public static int getCurrentStage() {
		return mStage;
	}

/**
	 * @editor: Nhân Bạch
	 * @date: 13/11/2014
	 * @brief: Thêm 1 số hàm phục vụ cho việc làm MENU 
	 */
	private static void loadTextures() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(GameManager.TextureManager, 100, 50, TextureOptions.BILINEAR);
		TiledTextureRegion t = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas, GameManager.AssetManager, "sound.png",0 ,0, 2, 1);
		Textures.put("sound", t);
		atlas.load();
	}

	private static void loadMusics() {
		try {
			MusicFactory.setAssetBasePath("music/");
			Music m;
			m = MusicFactory.createMusicFromAsset(GameManager.MusicManager,
					GameManager.Context, "menu.mp3");
			Musics.put("menu", m);
			m = MusicFactory.createMusicFromAsset(GameManager.MusicManager,
					GameManager.Context, "blop.mp3");
			Musics.put("blop", m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadFonts() {
		FontFactory.setAssetBasePath("font/");
		Font f = FontFactory.createFromAsset(GameManager.FontManager,
				GameManager.TextureManager, 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				GameManager.AssetManager, "font1.ttf", 64f, true,
				Color.WHITE_ABGR_PACKED_INT);
		Fonts.put("font1", f);
		f = FontFactory.createFromAsset(GameManager.FontManager,
				GameManager.TextureManager,
				(int) GameManager.Camera.getWidth(),
				(int) GameManager.Camera.getHeight(),
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				GameManager.AssetManager, "font2.ttf", 24f, true,
				Color.WHITE_ABGR_PACKED_INT);
		Fonts.put("font2", f);
	}
	
	public static BaseTextureRegion getTexture(String key) {
		return Textures.get(key);
	}

	public static Music getMusic(String key) {
		return Musics.get(key);
	}

	public static Font getFont(String key) {
		return Fonts.get(key);
	}
	/**********************/
}
