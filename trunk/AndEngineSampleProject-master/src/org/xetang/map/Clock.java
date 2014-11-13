package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;



/**
 * 
 */
public class Clock extends Item {

    /**
     * 
     */
    public Clock(Map map) {
    	super(GameManager.TextureRegion("Clock"), map);
    }
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	_map.FreezeTime();
    }

}