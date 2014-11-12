package org.xetang.map;

public class BrickWall extends Wall {

	public BrickWall(BrickWall brickWall) {
		super(brickWall);
	}

	public BrickWall(float posX, float posY) {
		super(MapObjectFactory.getBrickWallFixtureDef(), MapObjectFactory
				.getBrickWallTextureRegion(),
				MapObjectFactory.BRICK_WALL_CELL_PER_MAP, posX, posY);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.BrickWall;
	}

	@Override
	public MapObject clone() {
		return new BrickWall(this);
	}
}