package org.xetang.manager;

import org.andengine.engine.handler.IUpdateHandler;

public class GameItemManager implements IUpdateHandler {

	private static GameItemManager mInstance;
	
	private GameItemManager(){
		
	}
	
	public static GameItemManager getInstance(){
		if(mInstance==null)
			mInstance = new GameItemManager();
		return mInstance;
	}
	
	public static void loadResource() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public static void loadOldData() {
		// TODO Auto-generated method stub
		
	}

}
