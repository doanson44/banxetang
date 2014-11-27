package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;


/**
 * 
 */
public class Shovel extends Item {

    /**
     * 
     */
    public Shovel(Map map) {
    	super((TiledTextureRegion) MapObjectFactory2.getTexture("Shovel"), map);
    }

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	_map.MakeStoneWallFortress(); 
    }
    

    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.Shovel;
    }
}