package org.xetang.map;

import org.xetang.tank.Tank;



/**
 * 
 */
public class Item {
	Tank mOwner; //Xe tăng nhặt đc vật phẩm này
	
	public void affect(){
		//Xử lý tác dụng của vật phẩm
		//....
	}
	
	public void setOwner(Tank tank){
		mOwner = tank;
	}
}