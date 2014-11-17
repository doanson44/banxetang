package org.xetang.map.helper;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.util.math.MathConstants;
import org.xetang.manager.GameManager.Direction;
import org.xetang.map.MapObjectFactory.ObjectType;

import com.badlogic.gdx.math.Vector2;

public class CalcHelper {
	
	/*
	 * Để dành cải tiến cho việc đồng bộ thông số cell khi tạo MAP
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
		return direction.ordinal() * MathConstants.PI_HALF
				* MathConstants.RAD_TO_DEG;
	}

	public static float pixels2Meters(float pixels) {
		return pixels / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
	}

	public static Vector2 pixels2Meters(Vector2 pixelsVector) {
		return new Vector2(pixels2Meters(pixelsVector.x),
				pixels2Meters(pixelsVector.y));
	}
}
