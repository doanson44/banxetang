package org.xetang.map.item;

import org.xetang.manager.GameItemManager;
import org.xetang.map.object.MapObjectFactory.ObjectType;


/**
 * 
 */
public class Bomb extends Item {

    /**
     * 
     */
    public Bomb() {
    	super();
    	_sprite.setCurrentTileIndex(4);
    }
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	
    	// Ham pha huy tat ca xe tang dich 
    	GameItemManager.getInstance().DestroyAllEnermy();
    }
    
    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.BOMB;
    }
}