package org.xetang.controller;

import org.xetang.map.object.MapObjectFactory.ObjectType;

/*
 * Có thể là Bot hoặc người chơi
 */
public class Controller {
	protected IGameController mController;
	
	
	public void onTankDie() {
		
		
	}
	
	public void setOnController(IGameController controller) {
		this.mController = controller;
	}

	public void onCollide(ObjectType type) {
		
	}
}
