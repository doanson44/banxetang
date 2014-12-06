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
public class Racer extends Tank {

    /**
     * 
     */
	 public Racer(float px, float py) {
	    	
	    	super(px, py, (TiledTextureRegion) MapObjectFactory2.getTexture("EnemyRacer"));
	    	this.SetType(ObjectType.EnermyTank);
	    	this.SetTankType(TankType.Racer);

	    	this.speed = 3f;
	    	_maxNumberBullet = 2; 
	    	this.SetTankType(TankType.Normal);
	    	this.point = 200;
	    	this.hp = 1;
	 }

}