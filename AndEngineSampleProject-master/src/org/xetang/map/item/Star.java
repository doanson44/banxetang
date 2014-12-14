package org.xetang.map.item;

import org.xetang.map.object.MapObjectFactory.ObjectType;



/**
 * 
 */
public class Star extends Item {

    /**
     * 
     */
    public Star() {
    	super();
    	_sprite.setCurrentTileIndex(3);  
    	}

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	_mOwner.PowerUp();
    }
    

    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.STAR;
    }
}