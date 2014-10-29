package org.xetang.tank;

import org.xetang.manager.TankManager;
import org.xetang.map.Bullet;
import org.xetang.root.GameEntity;


/**
 * 
 */
public class Tank extends GameEntity {

	Bullet 	mBullet;
	boolean mIsFiring; 		//Xe tăng có đang bắn đạn hay không?
	int 	mDirection; 	//Hướng của xe tăng
	int 	mLevel;			//Cấp độ của xe tăng
	
	
	public Tank(){
		mBullet = new Bullet(this);
		TankManager.register(this);
	}


	public void loadOldData() {
		// TODO Auto-generated method stub
		
	}
	

	

}