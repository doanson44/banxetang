package org.xetang.map.object;

import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class Wall extends MapObject {

	public Wall(Wall wall) {
		super(wall);
	}

	public Wall(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, float posX, float posY,
			float width) {
		super(objectFixtureDef, objectTextureRegion, posX, posY, width);
	}
}