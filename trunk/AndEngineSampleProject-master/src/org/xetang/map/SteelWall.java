package org.xetang.map;

import org.andengine.util.debug.Debug;

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
	public MapObject clone() {
		return new SteelWall(this);
	}

	@Override
	public void doContact(IMapObject object) {
		try {
			if (object.getType() == ObjectType.Bullet) {
//				 if (Loại xe tăng đủ mạnh) {
//					_sprite.setVisible(false);
//					_body.getFixtureList().get(0).setSensor(true);
//					 DestroyHelper.add(this);
//				 }
			}
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
	}

	@Override
	public ObjectType getType() {
		return ObjectType.SteelWall;
	}
}