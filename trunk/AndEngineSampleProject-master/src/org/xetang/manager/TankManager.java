package org.xetang.manager;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.tank.Tank;

public class TankManager implements IUpdateHandler{

	private static TankManager mInstance;
	
	public static Tank Player;

	public static void generateOpponents(int i) {
		// TODO Auto-generated method stub
		
	}

	public static void loadResouceAllTank() {
		// TODO Auto-generated method stub
		
	}

	public static TankManager getInstance() {
		if(mInstance==null)
			mInstance = new TankManager();
		return mInstance;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public static void register(Tank tank) {
		// TODO Auto-generated method stub
		
	}

	public static void loadOldData() {
		// TODO Auto-generated method stub
		
	}

	public static void createPlayerTank() {
		// TODO Auto-generated method stub
		
	}

	public static void generateOpponentTank(int i) {
		// TODO Auto-generated method stub
		
	}

}
