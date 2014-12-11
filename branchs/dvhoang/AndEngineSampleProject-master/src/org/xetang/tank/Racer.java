package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.TankManager;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;

public class Racer extends Tank {

	public Racer(float px, float py) {

		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("EnemyRacer"));
		this.SetType(ObjectType.ENERMY_TANK);
		this.SetTankType(TankType.RACER);

		this.speed = TankManager.FAST_TANK_SPEED;

		_maxNumberBullet = 2;
		this.point = 200;
		this.hp = 1;
		mBulletType = ObjectType.BULLET;
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