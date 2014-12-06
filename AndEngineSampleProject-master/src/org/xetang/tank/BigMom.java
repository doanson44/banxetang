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
    	
    	super(px, py, (TiledTextureRegion) MapObjectFactory2.getTexture("EnemyBigMom"));
    	this.SetType(ObjectType.EnermyTank);
    	this.SetTankType(TankType.BigMom);
    	
    	this.speed = 2f;	
    	_maxNumberBullet = 2; 
    	this.point = 400;
    	this.hp = 4;
    	mSprite.setCurrentTileIndex(0);
    	
    	if (isTankBonus) // xu ly nhap nhay
    		mSprite.animate(new long[]{ 200, 200}, 0, 1, true);
    }

}