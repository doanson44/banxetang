package org.xetang.map.object;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;
import org.xetang.map.object.MapObjectFactory.ObjectType;

public class Bush extends MapObject {

	public Bush(Bush bush) {
		super(bush);

		_sprite = new TiledSprite(bush.getX(), bush.getY(), bush.getSprite()
				.getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(bush.getSprite().getWidth(), bush.getSprite()
				.getHeight());

		attachChild(_sprite);
	}

	public Bush(float posX, float posY) {
		super(null, MapObjectFactory.getTextureRegion(ObjectType.Bush),
				MapObjectFactory.BUSH_CELL_PER_MAP, posX, posY,
				MapObjectFactory.Z_INDEX_WRAPPER);
	}

	@Override
	public MapObject clone() {
		return new Bush(this);
	}

	@Override
	protected void createBody() {

	}

	@Override
	public void doContact(IMapObject object) {

	}

	@Override
	public ObjectType getType() {
		return ObjectType.Bush;
	}
}