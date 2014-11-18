package org.xetang.map;

import org.xetang.manager.GameManager.Direction;
import org.xetang.tank.Tank;

import com.badlogic.gdx.math.Vector2;

public interface IBullet extends IMapObject {
	
	public void initSpecification(int damage, Vector2 speed, Vector2 blowRadius);
	
	public void setTank(Tank tank);
	
	public int getDamage();
	
	public Direction getDirection();
	
	public Vector2 getBlowRadius();
	
	public Vector2 getTopPointUnit();
	
	public void readyToFire(Direction direction);
	
	public void beFired();
	
	public Vector2 getTopPoint();
}
