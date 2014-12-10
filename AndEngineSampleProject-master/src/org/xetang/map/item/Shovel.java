package org.xetang.map.item;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameItemManager;
import org.xetang.map.Map;
import org.xetang.map.object.MapObjectFactory.ObjectType;


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
    	GameItemManager.getInstance().MakeSteelWallFortress(); 
    }
    
    @Override
    public void DestroyAffect() {
    	// TODO Auto-generated method stub
    	GameItemManager.getInstance().RetrieveOldWallFortress();
    }
    
    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.SHOVEL;
    }
}