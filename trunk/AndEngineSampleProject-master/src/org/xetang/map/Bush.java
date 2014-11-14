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
	public MapObject clone() {
		return new Bush(this);
	}

	@Override
	public void doContact(IMapObject object) {
		
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Bush;
	}
}