package org.xetang.tank;


import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.Bullet;
import org.xetang.map.IBullet;
import org.xetang.map.Map;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.MapObjectFactory.TankType;
import org.xetang.map.MapObjectFactory2;

import com.badlogic.gdx.math.Vector2;


/**
 * 
 */
public class Normal extends Tank {

    /**
     * 
     */
    public Normal(float px, float py) {
    	super(px, py,(TiledTextureRegion) MapObjectFactory2.getTexture("EnemyNormal"));

    	this.speed = 1f;
    	_maxNumberBullet = 1;
    	this.SetTankType(TankType.Normal);
    	this.point = 100;
    	this.hp = 1;
    	mSprite.setCurrentTileIndex(0);
    	
    	if (isTankBonus) // xu ly nhap nhay
    		mSprite.animate(new long[]{ 200, 200}, 0, 1, true);
    }
    
    @Override
    public void onFire() {
    	// TODO Auto-generated method stub
    	super.onFire();
    	CreateBullet(ObjectType.Bullet, bPosX, bPosY);	
    }
  
}