package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.Map;
import org.xetang.map.MapObjectFactory2;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.MapObjectFactory.TankType;


/**
 * 
 */
public class GlassCannon extends Tank {

    /**
     * 
     */
	 public GlassCannon(float px, float py) {
	    	
	    	super(px, py,(TiledTextureRegion) MapObjectFactory2.getTexture("Player1"));
	    	this.SetType(ObjectType.EnermyTank);
	    	this.SetTankType(TankType.GlassCannon);
	 }

}