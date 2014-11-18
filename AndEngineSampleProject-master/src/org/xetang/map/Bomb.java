package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.helper.DestroyHelper;


/**
 * 
 */
public class Bomb extends Item {

    /**
     * 
     */
    public Bomb(Map map) {
    	super((TiledTextureRegion) GameManager.getTexture("Bomb"), map);
    }
    
    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	
    	// Ham pha huy tat ca xe tang dich 
    	_map.DestroyAllEnermy();
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
    	return ObjectType.Bomb;
    }
}