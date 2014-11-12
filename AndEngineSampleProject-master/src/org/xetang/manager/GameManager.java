package org.xetang.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xetang.main.MainActivity;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.model.XMLLoader;

import android.content.res.AssetManager;

public class GameManager {

	public static final String TANK_TAG = "XeTang"; // Dùng để debug

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
		// GameMapManager.loadResource();
		// TankManager.loadResouceAllTank();
		// GameItemManager.loadResource();
		// GameControllerManager.loadResource();

		XMLLoader.loadAllParameters();
		MapObjectFactory.initAllObjects();
	}

	/*
	 * ĂN ĐI KU
	 */
	public static void SwitchToScene(Scene currentScene, Scene newScene) {
		/*
		 * Xử lý Scene cũ
		 */

		GameManager.Scene = newScene;
	}

	public static int getCurrentStage() {
		return mStage;
	}
}
