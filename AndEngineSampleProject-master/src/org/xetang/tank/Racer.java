package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.TankManager;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;

public class Racer extends Tank {

	public Racer(float px, float py) {

		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("EnermyTank"));
		this.SetType(ObjectType.ENEMY_TANK);
		this.SetTankType(TankType.RACER);

		this.speed = TankManager.FAST_TANK_SPEED;

		CurrentSprite = 4;
		_maxNumberBullet = 2;
		this.point = 200;
		this.hp = 1;
		mBulletType = ObjectType.BULLET;
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