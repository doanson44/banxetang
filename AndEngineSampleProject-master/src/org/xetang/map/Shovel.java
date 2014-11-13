package org.xetang.map;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;


/**
 * 
 */
public class Shovel extends Item {

    /**
     * 
     */
    public Shovel(Map map) {
    	super((TiledTextureRegion) GameManager.getTexture("Shovel"), map);
    }

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	_map.MakeStoneWallFortress(); 
    }
}