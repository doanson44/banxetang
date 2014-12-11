package org.xetang.map.helper;

import org.xetang.map.object.IBullet;
import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObjectFactory;

public class DecideHelpder {

	public static boolean canDestroy(IMapObject object) {

		if (object == null) {
			return false;
		}

		switch (object.getType()) {
		case EAGLE:
		case WATER:
		case ICE:
		case BOMB:
		case CLOCK:
		case HELMET:
		case SHOVEL:
		case STAR:
		case TANK_ITEM:
			return false;

		default:
			break;
		}

		return true;
	}

	public static boolean canDestroy(IMapObject object, IBullet bullet) {

		if (!canDestroy(object)) {
			return false;
		}

		switch (object.getType()) {
		case STEEL_WALL:
			if (bullet.getDamage() != MapObjectFactory.BLOW_BULLET_DAMAGE) {
				return false;
			}

			break;
		default:
			break;
		}

		return true;
	}
}
