package org.xetang.map.object;

import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.map.object.MapObjectFactory.ObjectType;

public class SteelWall extends Wall {

	public SteelWall(SteelWall steelWall) {
		super(steelWall);
	}

	public SteelWall(float posX, float posY) {
		super(MapObjectFactory.getFixtureDef(ObjectType.STEEL_WALL),
				MapObjectFactory.getTextureRegion(ObjectType.STEEL_WALL), posX,
				posY, MapObjectFactory.STEEL_WALL_CELL_SIZE);
	}

	@Override
	public MapObject clone() {
		return new SteelWall(this);
	}

	@Override
	public void doContact(IMapObject object) {
		try {
			if (object.getType() == ObjectType.BULLET) {

				if (((IBullet) object).getTank().getType() == ObjectType.PLAYER_TANK
						&& object.getType() == ObjectType.STEEL_WALL)
					GameManager.getSound("steel").play();
			}
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
	}

	@Override
	public ObjectType getType() {
		return ObjectType.STEEL_WALL;
	}
}