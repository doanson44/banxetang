package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.TankManager;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;

public class Normal extends Tank {

	public Normal(float px, float py) {
		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("EnermyTank"));

		this.speed = TankManager.SLOW_TANK_SPEED;
		CurrentSprite = 0;
		_maxNumberBullet = 1;
		this.SetTankType(TankType.NORMAL);
		this.point = 100;
		this.hp = 1;
		mBulletType = ObjectType.SLOW_BULLET;
		//mSprite.setCurrentTileIndex(CurrentSprite);
		Animte();
	}


	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		super.onFire();
		CreateBullet(mBulletType, bPosX, bPosY);
	}

}