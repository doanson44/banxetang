package org.xetang.map.helper;

import org.xetang.map.object.IBullet;
import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObjectFactory;

public class DecideHelpder {

	public static boolean canDestroy(IMapObject object) {

		switch (object.getType()) {
		case Eagle:
		case Water:
		case Ice:
		case Bomb:
		case Clock:
		case Helmet:
		case Shovel:
		case Star:
		case TankItem:
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
