package org.xetang.map.item;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.root.GameEntity;

public class EnermyIcon extends GameEntity {

	protected TiledSprite _mSprite;
	protected static int _mSize = (int) GameManager.SMALL_CELL_SIZE;

	public EnermyIcon(float px, float py) {
		_mSprite = new TiledSprite(px, py,
				(ITiledTextureRegion) MapObjectFactory2
						.getTexture("Enermytank"),
				GameManager.VertexBufferObject);
		_mSprite.setSize(_mSize, _mSize);
		attachChild(_mSprite);
	}

	public TiledSprite GetSprite() {
		return _mSprite;
	}

	public static int GetSize() {
		return _mSize;
	}

}
