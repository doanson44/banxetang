package org.xetang.tank;

import org.xetang.manager.GameManager;
import org.xetang.map.Map;


/**
 * 
 */
public class Racer extends Tank {

    /**
     * 
     */
	 public Racer(int px, int py, Map map) {
	    	
	    	super(px, py, map, GameManager.TextureRegion("Player1"));
	 }

}