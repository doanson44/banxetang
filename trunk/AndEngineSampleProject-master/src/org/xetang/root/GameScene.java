package org.xetang.root;

import org.andengine.entity.scene.Scene;
import org.xetang.controller.Console;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameMapManager;

/**
 * 
 */
public class GameScene extends Scene {

	GameMapManager _mapManager;
	Console _console;

	/*
	 * ĂN ĐI KU
	 */
    public GameScene() {
    	GameManager.loadGameData();
    	
    	int iCurrentStage = GameManager.getCurrentStage();
    	_mapManager = new GameMapManager(this, iCurrentStage);
    	_console = new Console(_mapManager.getPlayerTank());
    }

    /* 
     * ĂN ĐI KU
     * Xử lý thông báo thắng cho người chơi
     */
	public void onWin() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * ĂN ĐI KU
	 * Xử lý thông báo thua cho người chơi
	 */
	public void onLose() {
		// TODO Auto-generated method stub
		
	}

    
}