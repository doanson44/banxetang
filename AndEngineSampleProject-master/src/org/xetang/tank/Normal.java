package org.xetang.tank;


import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.map.Bullet;
import org.xetang.map.IBullet;
import org.xetang.map.Map;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.MapObjectFactory2;

import com.badlogic.gdx.math.Vector2;


/**
 * 
 */
public class Normal extends Tank {

    /**
     * 
     */
    public Normal(int px, int py, Map map) {
    	super(px, py,map,(TiledTextureRegion) MapObjectFactory2.getTexture("Player1"));
    	this.speed = 3f;
    }
    
    @Override
    public void onFire() {
    	// TODO Auto-generated method stub
    	super.onFire();
    	float bPosX = 0 , bPosY = 0;
    	
    	Vector2 x = new Vector2(tankSprite.getX(),tankSprite.getY());
    	switch (mDirection) {
		case Down:
			bPosX = x.x + tankSprite.getHeight()/2;
			bPosY = x.y + tankSprite.getHeight();
			break;
		case Left:
			bPosX = x.x;
			bPosY = x.y + tankSprite.getWidth()/2;
			break;
		case Right:
			bPosX = x.x +  tankSprite.getWidth() ;
			bPosY = x.y + tankSprite.getWidth()/2;
			break;
		case Up:
			bPosX = x.x + tankSprite.getWidth()/2;
			bPosY = x.y;
			break;
		default:
			break;
		}
    	
		IBullet bullet = (IBullet) MapObjectFactory.createObject(
				ObjectType.Bullet, bPosX,bPosY);
		bullet.setTank(this);
		bullet.readyToFire(mDirection);
		bullet.beFired();
		GameManager.CurrentMapManager.addBullet(bullet);
    }
  
}