package org.xetang.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.xetang.main.GameActivity;
import org.xetang.map.Map;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory2;
import org.xetang.map.model.XMLLoader;
import org.xetang.root.GameScene;
import org.xetang.root.MainMenuScene;

import android.content.res.AssetManager;

public class GameManager {

	public static final String TANK_TAG = "XeTang"; // Dùng để debug

	public static final int CAMERA_WIDTH = 1060;
	public static final int CAMERA_HEIGHT = CAMERA_WIDTH / 10 * 6;

	public static final int MAP_GRID_WIDTH = 13;
	public static final int MAP_GRID_HEIGHT = 13;

	public static final float BORDER_WIDTH = 3f;

	public static final float MAP_RATIO = 1f;
	public static final float MAP_HEIGHT = CAMERA_HEIGHT - BORDER_WIDTH * 4;
	public static final float MAP_WIDTH = (int) (MAP_HEIGHT * MAP_RATIO);

	public static final float CAMERA_X = -(CAMERA_WIDTH - MAP_WIDTH) / 2;
	public static final float CAMERA_Y = -(CAMERA_HEIGHT - MAP_HEIGHT) / 2;

	public static final float LARGE_CELL_WIDTH = MAP_WIDTH / MAP_GRID_WIDTH;
	public static final float LARGE_CELL_HEIGHT = MAP_HEIGHT / MAP_GRID_HEIGHT;
	public static final float SMALL_CELL_WIDTH = LARGE_CELL_WIDTH / 2;
	public static final float SMALL_CELL_HEIGHT = LARGE_CELL_HEIGHT / 2;

	public enum Direction {
		Up, Right, Down, Left, None
	}

	public static GameActivity Activity;
	public static Camera Camera;
	public static Scene Scene;
	public static TextureManager TextureManager;
	public static AssetManager AssetManager;
	public static GameActivity Context;
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
	public static HashMap<String, Scene> ListScene = new HashMap<String, Scene>();
	public static Engine Engine;
	public static int mReachedStage;

	/**********************/

	public static GameMapManager CurrentMapManager;
	public static Map CurrentMap;

	public static boolean PlaceOnScreenControlsAtDifferentVerticalLocations = false;

	public static int mStage; // Màn chơi hiện tại
	public static int mPlayTimes; // Số lần chơi game, mỗi khi gameover tính 1
									// lần
	public static int mHighestScore; // Số điểm cao nhất đạt được
	public static int mLifeLeft; // Số mạng còn lại của người chơi

	static {
		mStage = 2;
		mReachedStage = 2;
		mPlayTimes = 0;
		mHighestScore = 0;
	}

	/**
	 * Load toàn bộ thông tin của trò chơi
	 */
	public static void loadGameData() {
		// Load thông tin màn chơi hiện tại
		// ...

		// fake
		mStage = GameManager.Context.getIntent().getIntExtra("stage", 1);
		mPlayTimes = 2;
		mHighestScore = 1000;
	}

	/**
	 * Khởi tạo Resource cho toàn bộ trò chơi
	 */
	public static void loadResource() {
		Textures = new Hashtable<String, BaseTextureRegion>();
		Musics = new Hashtable<String, Music>();
		Fonts = new Hashtable<String, Font>();

		// Load Fonts
		loadFonts();

		// load Musics
		loadMusics();

		// load Textures
		loadTextures();

		XMLLoader.loadAllParameters();
		MapObjectFactory.initAllObjects();
		MapObjectFactory2.InitTextures();

		GameControllerManager.loadResource();

		ListScene.put("game", new GameScene());

	}

	/**
	 * Hủy toàn bộ Resource của trò chơi
	 */
	public static void unloadResource() {

		MapObjectFactory.unloadAll();
	}

	/**
	 * Đổi cảnh trong game
	 * 
	 * @param name
	 *            : Tên cảnh
	 */
	/*
	 * ĂN ĐI KU
	 */
	public static void switchToScene(String name) {
		/*
		 * Xử lý Scene cũ
		 */
		// cheat

		if (ListScene.containsKey(name)
				&& GameManager.Engine.getScene() != ListScene.get(name)) {
			GameManager.Engine.setScene(ListScene.get(name));
			GameManager.Scene = ListScene.get(name);
			if (name == "mainmenu")
				((MainMenuScene) GameManager.Scene).onSwitched();
			else if (name == "game")
				((GameScene) GameManager.Scene).onSwitched();
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
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 100, 50, TextureOptions.BILINEAR);
		TiledTextureRegion t = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(atlas, GameManager.AssetManager,
						"sound.png", 0, 0, 2, 1);
		Textures.put("sound", t);
		atlas.load();
	}

	private static void loadMusics() {
		try {
			MusicFactory.setAssetBasePath("sfx/");
			Music m;
			m = MusicFactory.createMusicFromAsset(GameManager.MusicManager,
					GameManager.Context, "menu.mp3");
			Musics.put("menu", m);
			m = MusicFactory.createMusicFromAsset(GameManager.MusicManager,
					GameManager.Context, "blop.mp3");
			Musics.put("blop", m);
			m = MusicFactory.createMusicFromAsset(GameManager.MusicManager,
					GameManager.Context, "bonus.ogg");
			Musics.put("bonus", m);

			m = MusicFactory.createMusicFromAsset(GameManager.MusicManager,
					GameManager.Context, "fire.ogg");
			Musics.put("fire", m);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadFonts() {
		FontFactory.setAssetBasePath("font/");
		Font font1 = FontFactory.createFromAsset(GameManager.FontManager,
				GameManager.TextureManager, 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				GameManager.AssetManager, "font1.ttf", 64f, true,
				Color.WHITE_ABGR_PACKED_INT);
		font1.load();
		Fonts.put("font1", font1);
		Font font2 = FontFactory.createFromAsset(GameManager.FontManager,
				GameManager.TextureManager,
				(int) GameManager.Camera.getWidth(),
				(int) GameManager.Camera.getHeight(),
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				GameManager.AssetManager, "font2.ttf", 24f, true,
				Color.WHITE_ABGR_PACKED_INT);
		font2.load();
		Fonts.put("font2", font2);
	}

	public static Music getMusic(String key) {
		return Musics.get(key);
	}

	public static Font getFont(String key) {
		return Fonts.get(key);
	}

	/**********************/

	public static BaseTextureRegion getTexture(String string) {
		// TODO Auto-generated method stub
		return Textures.get(string);
	}
}
