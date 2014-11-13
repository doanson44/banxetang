package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;



/**
 * 
 */
public class Helmet extends Item {

    /**
     * 
     */
    public Helmet(Map map) {
    	super((TiledTextureRegion) GameManager.getTexture("Helmet"), map);

    }
    
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	mOwner.CreateShield();
    }

}