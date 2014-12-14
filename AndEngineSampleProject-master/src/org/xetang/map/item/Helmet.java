package org.xetang.map.item;

import org.xetang.map.object.MapObjectFactory.ObjectType;



/**
 * 
 */
public class Helmet extends Item {

    /**
     * 
     */
    public Helmet() {
    	super();
    	_sprite.setCurrentTileIndex(0);

    }
    
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	_mOwner.CreateShield();
    }
    
    @Override
    public void DestroyAffect() {
    	// TODO Auto-generated method stub
    	if(_mOwner != null)
    		_mOwner.DestroyShield();
    }
    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.HELMET;
    }

}