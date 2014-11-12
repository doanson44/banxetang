package org.xetang.map;

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
	public ObjectType getType() {
		return ObjectType.Eagle;
	}

	@Override
	public MapObject clone() {
		return new Eagle(this);
	}

}