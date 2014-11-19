package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;



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
    public void doContact(IMapObject object) {
    	// TODO Auto-generated method stub
    	try {
			if (object.getType() == ObjectType.PlayerTank) {
				affect();
			}
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
    }
    @Override
    public ObjectType getType() {
    	// TODO Auto-generated method stub
    	return ObjectType.Star;
    }
}