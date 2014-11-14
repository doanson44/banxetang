package org.xetang.map;

import org.andengine.util.debug.Debug;
import org.xetang.map.helper.DestroyHelper;

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
	public MapObject clone() {
		return new BrickWall(this);
	}

	@Override
	public void doContact(IMapObject object) {
		try {
			if (object.getType() == ObjectType.Bullet) {
				_sprite.setVisible(false);
				_body.getFixtureList().get(0).setSensor(true);
				DestroyHelper.add(this);
			}
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
	}

	@Override
	public ObjectType getType() {
		return ObjectType.BrickWall;
	}
}