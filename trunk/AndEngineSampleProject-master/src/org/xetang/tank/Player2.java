package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.Bullet;
import org.xetang.map.IBullet;
import org.xetang.map.Map;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.MapObjectFactory.TankType;
import org.xetang.map.MapObjectFactory2;
public class Player2 extends Player1 {

	public Player2(float px, float py, TiledTextureRegion region) {
    	super(px, py,(TiledTextureRegion) MapObjectFactory2.getTexture("Player2"));
    	this.tankSprite.setCurrentTileIndex(0);
	}
}
