package org.xetang.manager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.xetang.main.MainActivity;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.content.Context;
import android.content.res.AssetManager;

public class GameManager {

	public static final String TANK_TAG = "XeTang"; //Dùng để debug
	
	public static Camera Camera;
	public static Scene Scene;
	public static TextureManager TextureManager;
	public static AssetManager AssetManager;
	public static MainActivity Context;
	public static VertexBufferObjectManager VertexBufferObject;
	public static PhysicsWorld PhysicsWorld;
	
	private static int mStage; //Màn chơi hiện tại
	private static int mPlayTimes; //Số lần chơi game, mỗi khi gameover tính 1 lần
	private static int mHighestScore; //Số điểm cao nhất đạt được
	
	
	static{
		mStage 			= 1;
		mPlayTimes 		= 0;
		mHighestScore 	= 0;
	}
	
	public static void loadGameData() {
		//Load thông tin màn chơi hiện tại
		//...
		
		//fake
		mStage = 2;
		mPlayTimes = 2;
		mHighestScore = 1000;
	}

	public static void newGame() {
		GameMapManager.loadMapData(1);
		TankManager.createPlayerTank();
		TankManager.generateOpponentTank(4);
		GameControllerManager.setupControls();
		
	}
	
	public static void createScene() {
		createWalls();
		GameManager.newGame();	
	}

	public static void loadResource() {
		GameMapManager.loadResource();
		TankManager.loadResouceAllTank();
		GameItemManager.loadResource();
		GameControllerManager.loadResource();
		
	}
	
	private static void createWalls() {
		final VertexBufferObjectManager vertexBufferObjectManager = GameManager.VertexBufferObject;
		final Rectangle ground = new Rectangle(0, 420 - 2, 420, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, 420, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, 420, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(420 - 2, 0, 2, 420, vertexBufferObjectManager);
		//final Rectangle shelf = new Rectangle(300, 200, 100, 2, vertexBufferObjectManager);
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		//PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, shelf, BodyType.StaticBody, wallFixtureDef);

		
		GameManager.Scene.attachChild(ground);
		GameManager.Scene.attachChild(roof);
		GameManager.Scene.attachChild(left);
		GameManager.Scene.attachChild(right);
	}

}
