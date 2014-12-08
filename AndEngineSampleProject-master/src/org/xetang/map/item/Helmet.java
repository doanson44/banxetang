package org.xetang.map.item;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.map.Map;
import org.xetang.map.object.MapObjectFactory.ObjectType;



/**
 * 
 */
public class Helmet extends Item {

    /**
     * 
     */
    public Helmet(Map map) {
    	super((TiledTextureRegion) MapObjectFactory2.getTexture("Helmet"), map);

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
    	return ObjectType.Helmet;
    }

}