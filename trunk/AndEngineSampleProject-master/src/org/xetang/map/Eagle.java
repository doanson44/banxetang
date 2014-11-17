package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;

public class Eagle extends MapObject {

	public Eagle(Eagle eagle) {
		super(eagle);

		_sprite = new TiledSprite(eagle.getX(), eagle.getY(), eagle.getSprite()
				.getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(eagle.getSprite().getWidth(), eagle.getSprite()
				.getHeight());
		
		attachChild(_sprite);
	}

	public Eagle(float posX, float posY) {
		super(MapObjectFactory.getEagleFixtureDef(), MapObjectFactory
				.getEagleTextureRegion(), MapObjectFactory.EAGLE_CELL_PER_MAP,
				posX, posY);
	}

	@Override
	public IMapObject clone() {
		return new Eagle(this);
	}

	@Override
	public void doContact(IMapObject object) {
		try {
			if (object.getType() == ObjectType.Bullet) {
				TiledSprite x = (TiledSprite) _sprite;
				x.setCurrentTileIndex(1);
			}
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Eagle;
	}
}