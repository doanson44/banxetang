package org.xetang.map;

public class Bush extends MapObject {

	public Bush(Bush bush) {
		super(bush);
	}

	public Bush(float posX, float posY) {
		super(MapObjectFactory.getBushFixtureDef(), MapObjectFactory
				.getBushTextureRegion(), MapObjectFactory.BUSH_CELL_PER_MAP,
				posX, posY);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Bush;
	}

	@Override
	public MapObject clone() {
		return new Bush(this);
	}

}