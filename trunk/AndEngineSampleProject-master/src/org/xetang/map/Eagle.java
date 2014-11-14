package org.xetang.map;

import org.andengine.util.debug.Debug;

public class Eagle extends MapObject {

	public Eagle(Eagle water) {
		super(water);
	}

	public Eagle(float posX, float posY) {
		super(MapObjectFactory.getEagleFixtureDef(), MapObjectFactory
				.getEagleTextureRegion(), MapObjectFactory.EAGLE_CELL_PER_MAP,
				posX, posY);
	}

	@Override
	public MapObject clone() {
		return new Eagle(this);
	}

	@Override
	public void doContact(IMapObject object) {
		try {
			if (object.getType() == ObjectType.Bullet) {
				//Đổi sprite
			}
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Eagle;
	}
}