package org.xetang.root;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.xetang.controller.Console;
import org.xetang.manager.GameItemManager;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.manager.GameMapManager;
import org.xetang.map.IBullet;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

public class GameScene extends Scene implements IOnSceneTouchListener {

	GameMapManager _mapManager;
	Console _console;

	public GameScene() {
		GameManager.loadGameData();
		initScene();

		int iCurrentStage = GameManager.getCurrentStage();
		_mapManager = new GameMapManager(this, iCurrentStage);
		_console = new Console(_mapManager.getPlayerTank());
		
	
		registerUpdateHandler(_mapManager);
		registerUpdateHandler(GameItemManager.getInstance());
	}

	private void initScene() {
		createBackground();
		createPhysics();
	}

	private void createBackground() {
		setBackground(new Background(Color.BLACK));
	}

	private void createPhysics() {
		setOnSceneTouchListener(this);

		GameManager.PhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);

		registerUpdateHandler(GameManager.PhysicsWorld);

	}

	/*
	 * ĂN ĐI KU Xử lý thông báo thắng cho người chơi
	 */
	public void onWin() {
		// TODO Auto-generated method stub

	}

	/*
	 * ĂN ĐI KU Xử lý thông báo thua cho người chơi
	 */
	public void onLose() {
		// TODO Auto-generated method stub

	}

	int count = 0;

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (!pSceneTouchEvent.isActionDown()) {
			return false;
		}

		return true;
	}

	public void onSwitched() {
		_console = new Console(_mapManager.getPlayerTank());
		
	}
}