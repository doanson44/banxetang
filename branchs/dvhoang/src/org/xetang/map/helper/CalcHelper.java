package org.xetang.map.helper;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.map.object.MapObjectFactory.ObjectType;

import com.badlogic.gdx.math.Vector2;

public class CalcHelper {

	public static final float PI_IN_DEG = 180;
	public static final float PI_HALF_IN_DEG = PI_IN_DEG / 2;
	public static final float PI_TWICE_IN_DEG = PI_IN_DEG * 2;

	/*
	 * Để dành cải tiến cho việc đồng bộ thông số cell khi tạo MAP
	 */
	public static int getObjectsPerCell(int objectID) {
		switch (ObjectType.values()[objectID]) {
		case BRICK_WALL:
			return 4;

		case STEEL_WALL:
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
		return pixels / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	}

	public static Vector2 pixels2Meters(Vector2 pixelsVector) {
		return new Vector2(pixels2Meters(pixelsVector.x),
				pixels2Meters(pixelsVector.y));
	}

	// Hàm trả về vị trí CELL của xe tăng trong Map 26x26
	public static Vector2 CellInMap(AnimatedSprite mSprite) {
		Vector2 centerPoint = new Vector2(mSprite.getX() + mSprite.getWidth()
				/ 2, mSprite.getY() + mSprite.getHeight() / 2);

		return new Vector2((Math.round(centerPoint.x
				/ GameManager.SMALL_CELL_SIZE) - 1), Math.round(centerPoint.y
				/ GameManager.SMALL_CELL_SIZE) - 1);
	}

	public static Vector2 CellInMap13(AnimatedSprite mSprite) {
		Vector2 centerPoint = new Vector2(mSprite.getX() + mSprite.getWidth()
				/ 2, mSprite.getY() + mSprite.getHeight() / 2);

		int centerX = (int) (centerPoint.x / GameManager.LARGE_CELL_SIZE);
		int centerY = (int) (centerPoint.y / GameManager.LARGE_CELL_SIZE);
		return new Vector2(centerX, centerY);
	}
}
