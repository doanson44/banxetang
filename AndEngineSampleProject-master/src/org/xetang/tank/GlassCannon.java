package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.TankManager;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;

public class GlassCannon extends Tank {

	public GlassCannon(float px, float py) {

		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("EnermyTank"));
		this.SetType(ObjectType.ENEMY_TANK);
		this.SetTankType(TankType.GLASS_CANNON);

		this.speed = TankManager.NORMAL_TANK_SPEED;
		CurrentSprite = 8;
		_maxNumberBullet = 3;
		this.point = 300;
		this.hp = 1;
		mBulletType = ObjectType.DRILL_BULLET;
		//mSprite.setCurrentTileIndex(CurrentSprite);
		Animte();
	}


	@Override
	public void onFire() {
		super.onFire();
	}
	
	@Override
	public int getBonusPoint() {
	
		return 300;
	}
}