package org.xetang.map;

import org.xetang.manager.GameManager.Direction;
import org.xetang.tank.Tank;
import android.graphics.Point;

public class Bullet extends MapObject {

	int _speed; //pixel/s
	Tank _tank;
	Point _position;
	Direction _direction;
	int _damage;

	public Bullet(Tank tank) {
		
	}

}