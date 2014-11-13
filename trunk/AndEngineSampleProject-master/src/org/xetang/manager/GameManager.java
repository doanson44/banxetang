package org.xetang.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.andengine.opengl.texture.region.BaseTextureRegion;
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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.xetang.main.MainActivity;
import org.xetang.map.model.XMLLoader;
import org.xetang.root.MainMenuScene;

import android.content.res.AssetManager;

public class GameManager {

	public static final String TANK_TAG = "XeTang"; // Dùng để debug


	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;

	public static final int MAP_WIDTH = 832;
	public static final int MAP_HEIGHT = MAP_WIDTH / 4 * 3;

	public static final int CAMERA_X = -(CAMERA_WIDTH - MAP_WIDTH) / 2;
	public static final int CAMERA_Y = -(CAMERA_HEIGHT - MAP_HEIGHT) / 2;

	public static final int LARGE_CELL_WIDTH = MAP_WIDTH / 13;
	public static final int LARGE_CELL_HEIGHT = LARGE_CELL_WIDTH / 4 * 3;
	public static final int SMALL_CELL_WIDTH = LARGE_CELL_WIDTH / 2;
	public static final int SMALL_CELL_HEIGHT = SMALL_CELL_WIDTH / 4 * 3;

	public static final int BORDER_WIDTH = 2;

	public enum Direction {
		Up, Right, Down, Left
	}

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

		XMLLoader.loadAllParameters();
		// MapObjectFactory.initAllObjects();
		
	}

	

	/*
	 * ĂN ĐI KU
	 */
	public static void SwitchToScene(Scene currentScene, Scene newScene) {
		/*
		 * Xử lý Scene cũ
		 */

		
		
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
