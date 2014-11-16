package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;



/**
 * 
 */
public class Star extends Item {

    /**
     * 
     */
    public Star(Map map) {
    	super((TiledTextureRegion) GameManager.getTexture("Star"), map);    
    	}

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	mOwner.PowerUp();
    }
}