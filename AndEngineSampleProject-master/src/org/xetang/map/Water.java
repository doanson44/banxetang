package org.xetang.map;

public class Water extends MapObject {

	public Water(Water water) {
		super(water);
	}

	public Water(float posX, float posY) {
		super(MapObjectFactory.getWaterFixtureDef(), MapObjectFactory
				.getWaterTextureRegion(), MapObjectFactory.WATER_CELL_PER_MAP,
				posX, posY);
	}

	@Override
	public MapObject clone() {
		return new Water(this);
	}

	@Override
	public void doContact(IMapObject object) {
		
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Water;
	}
}