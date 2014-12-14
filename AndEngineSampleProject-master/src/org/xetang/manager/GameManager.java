package org.xetang.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
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
import org.xetang.main.GameActivity;
import org.xetang.map.Map;
import org.xetang.map.item.Item;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.model.XMLLoader;
import org.xetang.map.object.MapObjectFactory;
import org.xetang.root.GameScene;
import org.xetang.root.HighScoreScene;
import org.xetang.root.RoundScene;
import org.xetang.tank.Tank;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;

public class GameManager {

	public static final String TANK_TAG = "XeTang"; // Dùng để debug

	public static final int CAMERA_WIDTH = 1060;
	public static final int CAMERA_HEIGHT = CAMERA_WIDTH / 10 * 6;

	public static final int MAP_GRID = 13;

	public static final float BORDER_THICK = 3f;

	public static final float MAP_RATIO = 1f;
	public static final float MAP_SIZE = CAMERA_HEIGHT - BORDER_THICK * 4;

	public static final float CAMERA_X = -(CAMERA_WIDTH - MAP_SIZE) / 2 - 64;
	public static final float CAMERA_Y = -(CAMERA_HEIGHT - MAP_SIZE) / 2;

	public static final float LARGE_CELL_SIZE = MAP_SIZE / MAP_GRID;
	public static final float SMALL_CELL_SIZE = LARGE_CELL_SIZE / 2;

	public static final String ACTION_SCENE_OPEN = "open";
	public static final String ACTION_SCENE_CLOSE = "close";

	public enum Direction {
		UP, RIGHT, DOWN, LEFT, NONE
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
	static Hashtable<String, Sound> Sounds;
	static Hashtable<String, Music> Musics;
	static boolean IsBackgroundSound = true;
	public static boolean IsEffectSound = true;
	public static HashMap<String, Scene> SampleScene;
	public static Engine Engine;

	/**********************/

	public static GameMapManager CurrentMapManager;
	public static Map CurrentMap;

	public static boolean PlaceOnScreenControlsAtDifferentVerticalLocations = false;

	private static int mStage; // Màn chơi hiện tại
	private static int mHighestScore; // Số điểm cao nhất đạt được
	private static int mReachedStage;

	static {
		mStage = 7;
		mReachedStage = 2;
		mHighestScore = 0;
	}

	/**
	 * Load toàn bộ thông tin của trò chơi
	 */
	public static void loadGameData() {
		// Load thông tin màn chơi hiện tại
		// ...
		SharedPreferences preferences = Context
				.getPreferences(android.content.Context.MODE_PRIVATE);
		mStage = preferences.getInt("current stage", 1);
		mReachedStage = preferences.getInt("reached stage", 4);
		mHighestScore = preferences.getInt("high score", 0);

	}

	/**
	 * Khởi tạo Resource cho toàn bộ trò chơi
	 */
	public static void loadResource() {
		Textures = new Hashtable<String, BaseTextureRegion>();
		Sounds = new Hashtable<String, Sound>();
		Musics = new Hashtable<String, Music>();
		Fonts = new Hashtable<String, Font>();
		SampleScene = new HashMap<String, Scene>();

		// Load Fonts
		loadFonts();

		// load Musics
		loadMusics();

		// load sound effext
		loadSound();

		// load Textures
		loadTextures();

		XMLLoader.loadAllParameters();
		MapObjectFactory.initAllObjects();
		MapObjectFactory2.InitTextures();

		GameControllerManager.loadResource();

		SampleScene.put("game", new GameScene());
		SampleScene.put("highscore", new HighScoreScene());
		SampleScene.put("round", new RoundScene(GameManager.Context));
	}

	/**
	 * Hủy toàn bộ Resource của trò chơi
	 */
	public static void unloadResources() {

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
	public static void switchToScene(String name, Object data) {
		/*
		 * Xử lý Scene cũ
		 */

		if (SampleScene.containsKey(name)
				&& GameManager.Engine.getScene() != SampleScene.get(name)) {

			// raise event close
			if (GameManager.Scene instanceof HighScoreScene)
				((HighScoreScene) GameManager.Scene).onSwitched(
						ACTION_SCENE_CLOSE, data);
			else if (GameManager.Scene instanceof GameScene)
				((GameScene) GameManager.Scene).onSwitched(ACTION_SCENE_CLOSE);
			else if (GameManager.Scene instanceof RoundScene)
				((RoundScene) GameManager.Scene).onSwitched(ACTION_SCENE_CLOSE);

			if (name == "game")
				SampleScene.put(name, new GameScene());
			else if (name == "highscore")
				SampleScene.put(name, new HighScoreScene());

			// setup new Scene
			GameManager.Engine.setScene(SampleScene.get(name));
			GameManager.Scene = SampleScene.get(name);

			// raise event open
			if (name == "highscore")
				((HighScoreScene) GameManager.Scene).onSwitched(
						ACTION_SCENE_OPEN, data);
			else if (name == "game")
				((GameScene) GameManager.Scene).onSwitched(ACTION_SCENE_OPEN);
			else if (name == "round")
				((RoundScene) GameManager.Scene).onSwitched(ACTION_SCENE_OPEN);

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

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/tank/");
		atlas = new BitmapTextureAtlas(GameManager.TextureManager, 112 * 4, 48,
				TextureOptions.BILINEAR);
		t = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas,
				GameManager.Context, "sample_normal_48.png", 0, 0, 1, 1);
		Textures.put("sample normal tank", t);
		t = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas,
				GameManager.Context, "sample_racer_48.png", 112, 0, 1, 1);
		Textures.put("sample racer tank", t);
		t = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas,
				GameManager.Context, "sample_cannon_48.png", 224, 0, 1, 1);
		Textures.put("sample cannon tank", t);
		t = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas,
				GameManager.Context, "sample_bigmom_48.png", 336, 0, 1, 1);
		Textures.put("sample bigmom tank", t);
		atlas.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		atlas = new BitmapTextureAtlas(GameManager.TextureManager, 100, 50,
				TextureOptions.BILINEAR);
		t = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas,
				AssetManager, "sound.png", 0, 0, 2, 1);
		Textures.put("sound", t);
		atlas.load();
	}

	private static void loadMusics() {
		try {
			MusicFactory.setAssetBasePath("sfx/");
			String[] files = { "gameover.ogg", "gamestart.ogg",
					"amazing_score.mp3" };
			Music m;
			for (int i = 0; i < files.length; i++) {
				m = MusicFactory.createMusicFromAsset(GameManager.MusicManager,
						Context, files[i]);
				Musics.put(getFileName(files[i]), m);

			}
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

	private static void loadSound() {
		try {
			SoundFactory.setAssetBasePath("sfx/");
			String[] files = { "background.ogg", "bonus.ogg", "brick.ogg",
					"explosion.ogg", "fire.ogg", "score.ogg", "steel.ogg" };
			Sound m;
			SoundManager soundManager = new SoundManager(50);
			for (int i = 0; i < files.length; i++) {
				m = SoundFactory.createSoundFromAsset(soundManager, Context,
						files[i]);
				Sounds.put(getFileName(files[i]), m);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getFileName(String file) {
		file = file.replaceAll("^.*(/|\\\\)", "");
		file = file.replaceAll("\\.[a-zA-Z0-9]*$", "");
		return file;
	}

	private static void loadFonts() {
		/*
		 * FontFactory.setAssetBasePath("font/"); Font font1 =
		 * FontFactory.createFromAsset(GameManager.FontManager,
		 * GameManager.TextureManager, 256, 256,
		 * TextureOptions.BILINEAR_PREMULTIPLYALPHA, GameManager.AssetManager,
		 * "font1.ttf", 64f, true, Color.WHITE_ABGR_PACKED_INT); font1.load();
		 * Fonts.put("font1", font1);
		 * 
		 * Font font2 = FontFactory.createFromAsset(GameManager.FontManager,
		 * GameManager.TextureManager, (int) GameManager.Camera.getWidth(),
		 * (int) GameManager.Camera.getHeight(),
		 * TextureOptions.BILINEAR_PREMULTIPLYALPHA, GameManager.AssetManager,
		 * "font2.ttf", 12f, true, Color.WHITE_ABGR_PACKED_INT); font2.load();
		 * Fonts.put("font2", font2);
		 * 
		 * Font fontInMap = FontFactory.createFromAsset(GameManager.FontManager,
		 * GameManager.TextureManager, (int) GameManager.Camera.getWidth(),
		 * (int) GameManager.Camera.getHeight(),
		 * TextureOptions.BILINEAR_PREMULTIPLYALPHA, GameManager.AssetManager,
		 * "font2.ttf", LARGE_CELL_SIZE, true, Color.WHITE_ABGR_PACKED_INT);
		 * fontInMap.load(); Fonts.put("fontInMap", fontInMap);
		 */
	}

	public static Sound getSound(String key) {
		return Sounds.get(key);
	}

	public static Music getMusic(String key) {
		return Musics.get(key);
	}

	public static Font getFont(String name, float size, int color) {
		Font f;
		FontFactory.setAssetBasePath("font/");
		f = FontFactory.createFromAsset(GameManager.FontManager,
				GameManager.TextureManager,
				(int) GameManager.Camera.getWidth(),
				(int) GameManager.Camera.getHeight(),
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				GameManager.AssetManager, String.format("%s.ttf", name), size,
				true, color);
		f.load();
		return f;
	}

	/**********************/

	public static BaseTextureRegion getTexture(String string) {
		return Textures.get(string);
	}

	public static List<Tank> getDefenseTank(String string) {
		List<Tank> tanks = new ArrayList<Tank>();
		return tanks;
	}

	public static int getHighScore() {
		return mHighestScore;
	}

	public static List<Item> getPickUpItem(String player) {
		List<Item> items = new ArrayList<Item>();
		return items;
	}

	/**
	 * Mở tắt âm thanh
	 * 
	 * @param pVolumn
	 *            : âm lượng muốn mở từ 0.0 -> 1.0
	 */
	public static void turnOnOffMusic(float pVolumn) {
		Enumeration<String> keys = Sounds.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			Sounds.get(key).setVolume(pVolumn);
		}

		keys = Musics.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			Musics.get(key).setVolume(pVolumn);
		}
	}

	public static void clearAll() {

	}

	public static void onDestroyResources() {
		for (String key : Sounds.keySet()) {
			Sounds.get(key).release();
		}
		for (String key : Musics.keySet()) {
			Musics.get(key).release();
		}

	}

	/**
	 * Play sound một cách an toàn [Deprecated]
	 * 
	 * @param name
	 * @param isLooping
	 */
	public static void playSoundSafely(String name, boolean isLooping) {
		final Sound s = GameManager.getSound(name);
		s.setLooping(true);

		GameManager.Context.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				s.play();
			}
		});
	}

	/**
	 * Lưu dữ liệu trước khi thoát game
	 */
	public static void saveData() {
		Editor editor = GameManager.Context.getPreferences(
				android.content.Context.MODE_PRIVATE).edit();
		editor.putInt("current stage", mStage);
		editor.putInt("reached stage", mReachedStage);
		editor.putInt("high score", mHighestScore);
		editor.commit();
	}

	/**
	 * Qua màn chơi kế tiếp
	 */
	public static void nextStage() {
		mStage++;
		mReachedStage = (mStage > mReachedStage) ? mStage : mReachedStage;
	}

	public static void seekStage(int index) {
		mStage = index;
	}

	public static int getReachedStage() {
		return mReachedStage;
	}

	public static void newHighScore(int curHighScore) {
		if (curHighScore > mHighestScore)
			mHighestScore = curHighScore;
	}

}
