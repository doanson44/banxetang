package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;




/**
 * 
 */
public class TankItem extends Item {

    /**
     * 
     */
    public TankItem(Map map) {
    	super((TiledTextureRegion) MapObjectFactory2.getTexture("Tank"), map); 
    }

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	//_map.AddExtraLife();
    }
}