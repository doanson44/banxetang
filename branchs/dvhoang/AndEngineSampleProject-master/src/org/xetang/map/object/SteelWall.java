package org.xetang.map.object;

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
		// try {
		// if (object.getType() == ObjectType.Bullet
		// && DecideHelpder.canDestroy(this, (IBullet) object)) {
		// _sprite.setVisible(false);
		// _body.getFixtureList().get(0).setSensor(true);
		// // DestroyHelper.add(this);
		// }
		// } catch (Exception e) {
		// Debug.d("Collsion", "Nothing to contact!");
		// }
	}

	@Override
	public ObjectType getType() {
		return ObjectType.STEEL_WALL;
	}
}