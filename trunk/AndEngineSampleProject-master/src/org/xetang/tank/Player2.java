package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.map.item.MapObjectFactory2;
public class Player2 extends Player1 {
	public Player2(float px, float py){
		this(px,py,(TiledTextureRegion) MapObjectFactory2.getTexture("Player2"));
	}
	public Player2(float px, float py, TiledTextureRegion region) {
    	super(px, py,(TiledTextureRegion) MapObjectFactory2.getTexture("Player2"));
    	this.mSprite.setCurrentTileIndex(0);
	}
	
	
}
