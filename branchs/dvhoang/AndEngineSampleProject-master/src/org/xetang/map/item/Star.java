package org.xetang.map.item;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.map.Map;
import org.xetang.map.object.MapObjectFactory.ObjectType;



/**
 * 
 */
public class Star extends Item {

    /**
     * 
     */
    public Star(Map map) {
    	super((TiledTextureRegion) MapObjectFactory2.getTexture("Star"), map);    
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