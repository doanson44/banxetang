package org.xetang.map.helper;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.xetang.manager.GameManager.Direction;
import org.xetang.map.MapObjectFactory.ObjectType;

import com.badlogic.gdx.math.Vector2;

public class CalcHelper {

	public static final float PI_IN_DEG = 180;
	public static final float PI_HALF_IN_DEG = PI_IN_DEG / 2;
	public static final float PI_TWICE_IN_DEG = PI_IN_DEG * 2;

	/*
	 * Ä�á»ƒ dÃ nh cáº£i tiáº¿n cho viá»‡c Ä‘á»“ng bá»™ thÃ´ng sá»‘ cell khi
	 * táº¡o MAP
	 */
	public static int getObjectsPerCell(int objectID) {
		switch (ObjectType.values()[objectID]) {
		case BrickWall:
			return 4;

		case SteelWall:
			return 2;

		default:
			break;
		}

		return 1;
	}

	public static float direction2Degrees(Direction direction) {
		return direction2Degrees(direction, false);
	}

	public static float direction2Degrees(Direction direction,
			boolean isCounterClockwise) {
		if (!isCounterClockwise) {
			return direction.ordinal() * PI_HALF_IN_DEG;
		}

		return PI_TWICE_IN_DEG - direction.ordinal() * PI_HALF_IN_DEG;
	}

	public static float pixels2Meters(float pixels) {
		return pixels / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
	}

	public static Vector2 pixels2Meters(Vector2 pixelsVector) {
		return new Vector2(pixels2Meters(pixelsVector.x),
				pixels2Meters(pixelsVector.y));
	}
}
