package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class Wall extends MapObject {

	public Wall(Wall wall) {
		super(wall);
	}

	public Wall(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, int piecePerMap,
			float posX, float posY) {
		super(objectFixtureDef, objectTextureRegion, piecePerMap, posX, posY);
	}
}