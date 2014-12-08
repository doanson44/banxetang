package org.xetang.tank;


import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;


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
    	mBulletType = ObjectType.SlowBullet;
    	mSprite.setCurrentTileIndex(0);
    	
    }
    @Override
    public void SetTankBonus(boolean bool) {
    	// TODO Auto-generated method stub
    	isTankBonus = bool;
    	if (isTankBonus) // xu ly nhap nhay
    		mSprite.animate(new long[]{ 200, 200}, 0,1, true);
    }
    @Override
    public void onFire() {
    	// TODO Auto-generated method stub
    	super.onFire();
    	CreateBullet(mBulletType, bPosX, bPosY);	
    }
  
}