package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;

public class Water extends MapObject {

	public Water(Water water) {
		super(water);

		_sprite = new TiledSprite(water.getX(), water.getY(), water.getSprite()
				.getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(water.getSprite().getWidth(), water.getSprite()
				.getHeight());
		
		attachChild(_sprite);
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