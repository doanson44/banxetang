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
    	super(px, py,(TiledTextureRegion) MapObjectFactory2.getTexture("Player1"));

    	this.speed = 3f;
    	_maxNumberBullet = 1;
    	this.SetTankType(TankType.Normal);
    }
    
    @Override
    public void onFire() {
    	// TODO Auto-generated method stub
    	super.onFire();
    	CreateBullet(ObjectType.Bullet, bPosX, bPosY);	
    }
  
}