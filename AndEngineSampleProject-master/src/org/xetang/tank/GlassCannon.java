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
	    	
	    	super(px, py,(TiledTextureRegion) MapObjectFactory2.getTexture("EnemyGlassCannon"));
	    	this.SetType(ObjectType.EnermyTank);
	    	this.SetTankType(TankType.GlassCannon);
	    	
	    	this.speed = 2f;
	    	_maxNumberBullet = 3; 
	    	this.point = 300;
	    	this.hp = 1;
	    	
	    	mSprite.setCurrentTileIndex(0);
	    	
	    	if (isTankBonus) // xu ly nhap nhay
	    		mSprite.animate(new long[]{ 200, 200}, 0, 1, true);
	 }

}