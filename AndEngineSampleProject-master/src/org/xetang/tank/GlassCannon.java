package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.Map;


/**
 * 
 */
public class GlassCannon extends Tank {

    /**
     * 
     */
	 public GlassCannon(int px, int py, Map map) {
	    	
	    	super(px, py, map, (TiledTextureRegion) GameManager.getTexture("Player1"));
	 }

}