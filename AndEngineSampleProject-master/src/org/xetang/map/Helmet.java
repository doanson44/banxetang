package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;



/**
 * 
 */
public class Helmet extends Item {

    /**
     * 
     */
    public Helmet(Map map) {
    	super(GameManager.TextureRegion("Helmet"), map);

    }
    
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	mOwner.CreateShield();
    }

}