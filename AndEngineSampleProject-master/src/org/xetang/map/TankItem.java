package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
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
    	super(GameManager.TextureRegion("Tank"), map); 
    }

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	_map.AddExtraLife();
    }
}