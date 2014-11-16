package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;



/**
 * 
 */
public class TankItem extends Item {

    /**
     * 
     */
    public TankItem(Map map) {
    	super((TiledTextureRegion) GameManager.getTexture("Tank"), map); 
    }

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	_map.AddExtraLife();
    }
}