package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameMapManager;



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