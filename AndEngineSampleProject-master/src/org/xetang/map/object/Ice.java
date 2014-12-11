package org.xetang.map.object;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;
import org.xetang.map.object.MapObjectFactory.ObjectType;

public class Ice extends MapObject {

	public Ice(Ice ice) {
		super(ice);

		_sprite = new TiledSprite(ice.getX(), ice.getY(), ice.getSprite()
				.getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(ice.getSprite().getWidth(), ice.getSprite().getHeight());
		_sprite.setUserData(this);

		// attachChild(_sprite);
	}

	public Ice(float posX, float posY) {
		super(MapObjectFactory.getFixtureDef(ObjectType.ICE), MapObjectFactory
				.getTextureRegion(ObjectType.ICE), posX, posY,
				MapObjectFactory.ICE_CELL_SIZE);
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
		return ObjectType.ICE;
	}
}