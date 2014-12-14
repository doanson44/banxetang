package org.xetang.map.item;

import org.xetang.manager.GameItemManager;
import org.xetang.map.object.MapObjectFactory.ObjectType;



/**
 * 
 */
public class Clock extends Item {

    /**
     * 
     */
    public Clock() {
    	super();
    	_sprite.setCurrentTileIndex(1);
    }
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	GameItemManager.getInstance().FreezeTime();
    }
    

    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.CLOCK;
    }

}