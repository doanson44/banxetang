package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;

import org.xetang.manager.GameItemManager;

import org.xetang.map.MapObjectFactory.ObjectType;



/**
 * 
 */
public class Clock extends Item {

    /**
     * 
     */
    public Clock(Map map) {
    	super((TiledTextureRegion) MapObjectFactory2.getTexture("Clock"), map);
    }
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	GameItemManager.getInstance().FreezeTime();
    }
    

    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.Clock;
    }

}