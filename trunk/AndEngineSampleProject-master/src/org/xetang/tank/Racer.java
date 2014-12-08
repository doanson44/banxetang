package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
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

		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("EnemyRacer"));
		this.SetType(ObjectType.EnermyTank);
		this.SetTankType(TankType.Racer);

		this.speed = 3f;
		_maxNumberBullet = 2;
		this.point = 200;
		this.hp = 1;
		mBulletType = ObjectType.Bullet;
		mSprite.setCurrentTileIndex(0);

	}

	@Override
	public void SetTankBonus(boolean bool) {
		// TODO Auto-generated method stub
		isTankBonus = bool;
		if (isTankBonus) // xu ly nhap nhay
			mSprite.animate(new long[] { 200, 200 }, 0, 1, true);
	}

	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		super.onFire();
		CreateBullet(mBulletType, bPosX, bPosY);
	}
}