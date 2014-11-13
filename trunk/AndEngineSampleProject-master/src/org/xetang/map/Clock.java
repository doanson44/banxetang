package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;



/**
 * 
 */
public class Clock extends Item {

    /**
     * 
     */
    public Clock(Map map) {
    	super((TiledTextureRegion) GameManager.getTexture("Clock"), map);
    }
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	_map.FreezeTime();
    }

}