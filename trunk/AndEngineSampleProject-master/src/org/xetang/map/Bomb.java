package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;


/**
 * 
 */
public class Bomb extends Item {

    /**
     * 
     */
    public Bomb(Map map) {
    	super((TiledTextureRegion) GameManager.getTexture("Bomb"), map);
    }
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	
    	// Ham pha huy tat ca xe tang dich 
    	_map.DestroyAllEnermy();
    }
}