package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.map.MapObjectFactory.ObjectType;


/**
 * 
 */
public class Bomb extends Item {

    /**
     * 
     */
    public Bomb(Map map) {
    	super((TiledTextureRegion) MapObjectFactory2.getTexture("Bomb"), map);
    }
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	
    	// Ham pha huy tat ca xe tang dich 
    	//_map.DestroyAllEnermy();
    }
    
    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.Bomb;
    }
}