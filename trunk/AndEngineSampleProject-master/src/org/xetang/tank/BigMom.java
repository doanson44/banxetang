package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.Map;
import org.xetang.map.MapObjectFactory.TankType;
import org.xetang.map.MapObjectFactory2;
import org.xetang.map.MapObjectFactory.ObjectType;


/**
 * 
 */
public class BigMom extends Tank {

    /**
     * 
     */
    public BigMom(float px, float py) {
    	
    	super(px, py, (TiledTextureRegion) MapObjectFactory2.getTexture("Bigmom"));
    	this.SetType(ObjectType.EnermyTank);
    	this.SetTankType(TankType.BigMom);
    }

}