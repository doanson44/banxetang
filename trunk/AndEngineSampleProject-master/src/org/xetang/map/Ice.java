package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;

public class Ice extends MapObject {

	public Ice(Ice ice) {
		super(ice);

		_sprite = new TiledSprite(ice.getX(), ice.getY(), ice.getSprite()
				.getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(ice.getSprite().getWidth(), ice.getSprite().getHeight());

		attachChild(_sprite);
	}

	public Ice(float posX, float posY) {
		super(MapObjectFactory.getFixtureDef(ObjectType.Ice), MapObjectFactory
				.getTextureRegion(ObjectType.Ice),
				MapObjectFactory.ICE_CELL_PER_MAP, posX, posY);
	}

	@Override
	public MapObject clone() {
		return new Ice(this);
	}

	@Override
	public void doContact(IMapObject object) {

	}

	@Override
	public ObjectType getType() {
		return ObjectType.Ice;
	}
}