package org.xetang.map;


import org.xetang.root.GameEntity;

/**
 * 
 */
public class MapObject extends GameEntity {
	
	protected boolean _isStatic; //Vật thể tĩnh hay động
	
	public boolean isStatic() {
		return _isStatic;
	}

	public void onHit(Bullet bullet){
		
	}
}