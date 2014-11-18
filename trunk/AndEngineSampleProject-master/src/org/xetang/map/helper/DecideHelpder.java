package org.xetang.map.helper;

import org.xetang.map.IBullet;
import org.xetang.map.IMapObject;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;

public class DecideHelpder {

	public static boolean canCollide(IMapObject object) {

		if (object.getType() == ObjectType.Water) {
			return false;
		}

		return true;
	}

	public static boolean canDestroy(IMapObject object) {

		if (object.getType() != ObjectType.Water
				&& object.getType() != ObjectType.Eagle) {
			return true;
		}

		return false;
	}

	public static boolean canDestroy(IMapObject object, IBullet bullet) {

		if (!canDestroy(object)) {
			return false;
		}

		switch (object.getType()) {
		case SteelWall:
			if (bullet.getDamage() != MapObjectFactory.FAST_BULLET_DAMAGE) {
				return false;
			}

			break;
		default:
			break;
		}

		return true;
	}
}
