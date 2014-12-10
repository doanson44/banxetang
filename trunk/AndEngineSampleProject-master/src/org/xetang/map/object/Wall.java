package org.xetang.map.object;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class Wall extends MapObject {

	public Wall(Wall wall) {
		super(wall);

		_sprite = new TiledSprite(wall.getX(), wall.getY(), wall.getSprite()
				.getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(wall.getSprite().getWidth(), wall.getSprite()
				.getHeight());

		attachChild(_sprite);
	}

	public Wall(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, float posX, float posY,
			float width) {
		super(objectFixtureDef, objectTextureRegion, posX, posY, width);
	}
}