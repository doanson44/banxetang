package org.xetang.map;

public class SteelWall extends Wall {

	public SteelWall(SteelWall steelWall) {
		super(steelWall);
	}

	public SteelWall(float posX, float posY) {
		super(MapObjectFactory.getSteelWallFixtureDef(), MapObjectFactory
				.getSteelWallTextureRegion(),
				MapObjectFactory.STEEL_WALL_CELL_PER_MAP, posX, posY);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.SteelWall;
	}

	@Override
	public MapObject clone() {
		return new SteelWall(this);
	}
}