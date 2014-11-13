package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;



/**
 * 
 */
public class Star extends Item {

    /**
     * 
     */
    public Star(Map map) {
    	super(GameManager.TextureRegion("Star"), map);    
    	}

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	mOwner.PowerUp();
    }
}