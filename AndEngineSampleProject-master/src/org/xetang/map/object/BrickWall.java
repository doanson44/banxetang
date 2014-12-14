package org.xetang.map.object;

import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.map.object.MapObjectFactory.ObjectType;

public class BrickWall extends Wall {

	public BrickWall(BrickWall brickWall) {
		super(brickWall);
	}

	public BrickWall(float posX, float posY) {
		super(MapObjectFactory.getFixtureDef(ObjectType.BRICK_WALL),
				MapObjectFactory.getTextureRegion(ObjectType.BRICK_WALL), posX,
				posY, MapObjectFactory.BRICK_WALL_CELL_SIZE);
	}

	@Override
	public MapObject clone() {
		return new BrickWall(this);
	}

	@Override
	public void doContact(IMapObject object) {
		try {
			if (object.getType() == ObjectType.BULLET) {

				if (((IBullet) object).getTank().getType() == ObjectType.PLAYER_TANK
						&& object.getType() == ObjectType.BRICK_WALL)
					GameManager.getSound("brick").play();
			}
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
	}

	@Override
	public ObjectType getType() {
		return ObjectType.BRICK_WALL;
	}
}