package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
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
    	_map.FreezeTime();
    }
    

    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.Clock;
    }

}