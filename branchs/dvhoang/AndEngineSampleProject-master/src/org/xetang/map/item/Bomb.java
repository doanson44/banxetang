package org.xetang.map.item;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameItemManager;
import org.xetang.map.Map;
import org.xetang.map.object.MapObjectFactory.ObjectType;


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
    	GameItemManager.getInstance().DestroyAllEnermy();
    }
    
    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.BOMB;
    }
}