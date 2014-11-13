package org.xetang.tank;

import org.xetang.manager.GameManager;
import org.xetang.map.Map;


/**
 * 
 */
public class BigMom extends Tank {

    /**
     * 
     */
    public BigMom(int px, int py, Map map) {
    	
    	super(px, py, map, GameManager.TextureRegion("Player1"));
    }

}