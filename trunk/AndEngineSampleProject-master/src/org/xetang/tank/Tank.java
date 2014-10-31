package org.xetang.tank;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.controller.Bot;
import org.xetang.controller.Controller;
import org.xetang.controller.IGameController;
import org.xetang.manager.TankManager;
import org.xetang.map.Bullet;
import org.xetang.root.GameEntity;


/**
 * 
 */
public class Tank extends GameEntity implements IUpdateHandler, IGameController {

	Controller mController;
	
	Bullet 	mBullet;
	boolean mIsFiring; 		//Xe tăng có đang bắn đạn hay không?
	int 	mDirection; 	//Hướng của xe tăng
	int 	mLevel;			//Cấp độ của xe tăng
	boolean mIsAlive;
		
	public boolean isAlive() {
		return mIsAlive;
	}
	
	public void killSelf(){
		mIsAlive = false;
		mController.onTankDie();
	}


	public Tank(){
		mBullet = new Bullet(this);
		TankManager.register(this);
	}

	
	public void setController(Controller controller){
		this.mController = controller;
	}
	
	public void loadOldData() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onLeft() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onForward() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onBackward() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		
	}
	

	

}