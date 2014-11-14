package org.xetang.manager;

<<<<<<< .mine
import org.andengine.engine.Engine;
=======
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

>>>>>>> .r33
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.audio.music.*;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
<<<<<<< .mine
=======
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
>>>>>>> .r33
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.xetang.main.MainActivity;
<<<<<<< .mine
import org.xetang.map.MapObjectFactory;
import org.xetang.map.model.XMLLoader;
=======
import org.xetang.map.MapObjectFactory;
import org.xetang.map.model.XMLLoader;
import org.xetang.root.GameScene;
import org.xetang.root.MainMenuScene;
>>>>>>> .r33

import android.content.res.AssetManager;

public class GameManager {

	public static final String TANK_TAG = "XeTang"; // Dùng để debug

	public static final int CAMERA_WIDTH = 1080;
	public static final int CAMERA_HEIGHT = CAMERA_WIDTH / 10 * 6;

	public static final float BORDER_WIDTH = 3f;

	public static final float MAP_RATIO = 1f;
	public static final float MAP_HEIGHT = CAMERA_HEIGHT - BORDER_WIDTH * 4;
	public static final float MAP_WIDTH = (int) (MAP_HEIGHT * MAP_RATIO);

	public static final float CAMERA_X = -(CAMERA_WIDTH - MAP_WIDTH) / 2;
	public static final float CAMERA_Y = -(CAMERA_HEIGHT - MAP_HEIGHT) / 2;

	public static final float LARGE_CELL_WIDTH = MAP_WIDTH / 13;
	public static final float LARGE_CELL_HEIGHT = MAP_HEIGHT / 13;
	public static final float SMALL_CELL_WIDTH = LARGE_CELL_WIDTH / 2;
	public static final float SMALL_CELL_HEIGHT = LARGE_CELL_HEIGHT / 2;

	public static final int PIXEL_PER_METER = 32; 
	
	public static final float NORMAL_BULLET_SPPED_WIDTH = MAP_WIDTH / PIXEL_PER_METER / 2;
	public static final float NORMAL_BULLET_SPPED_HEIGHT = MAP_HEIGHT / PIXEL_PER_METER / 2;

	public static final float FAST_BULLET_SPPED_WIDTH = NORMAL_BULLET_SPPED_WIDTH * 2;
	public static final float FAST_BULLET_SPPED_HEIGHT = NORMAL_BULLET_SPPED_HEIGHT * 2;

	public enum Direction {
		Up, Right, Down, Left
	}

	public static MainActivity Activity;
	public static Camera Camera;
	public static Scene Scene;
	public static Engine Engine;
	public static TextureManager TextureManager;
	public static AssetManager AssetManager;
	public static MainActivity Context;
	public static VertexBufferObjectManager VertexBufferObject;
	public static PhysicsWorld PhysicsWorld;
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

<<<<<<< .mine
	public static boolean PlaceOnScreenControlsAtDifferentVerticalLocations = false;
=======

>>>>>>> .r33
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

	public static void loadResource() {
		Textures = new Hashtable<String, BaseTextureRegion>();
		Musics = new Hashtable<String, Music>();
		Fonts = new Hashtable<String, Font>();
<<<<<<< .mine
=======
		
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
>>>>>>> .r33

<<<<<<< .mine
		XMLLoader.loadAllParameters();
		MapObjectFactory.initAllObjects();
=======
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

>>>>>>> .r33
	}

	/*
	 * ĂN ĐI KU
	 */
	public static void SwitchToScene(int index) {
		/*
		 * Xử lý Scene cũ
		 */
<<<<<<< .mine

		GameManager.Scene = newScene;
=======
		if(index >=0 && index < ListScene.size() && GameManager.Engine.getScene() != ListScene.get(index)){
			GameManager.Engine.setScene(ListScene.get(index));
			GameManager.Scene = ListScene.get(index);
			if(index==0)
				((MainMenuScene)GameManager.Scene).onSwitched();
			else
				((GameScene)GameManager.Scene).onSwitched();
		}
>>>>>>> .r33
	}

	public static int getCurrentStage() {
		return mStage;
	}
<<<<<<< .mine
=======

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
>>>>>>> .r33

	public static Music getMusic(String key) {
		return Musics.get(key);
	}

	public static Font getFont(String key) {
		return Fonts.get(key);
	}
	/**********************/
}
