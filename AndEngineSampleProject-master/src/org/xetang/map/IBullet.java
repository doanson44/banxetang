package org.xetang.map;

import org.xetang.manager.GameManager.Direction;

public interface IBullet extends IMapObject {
	
	public void readyToFire(Direction direction);
	
	public void beFired();
}
