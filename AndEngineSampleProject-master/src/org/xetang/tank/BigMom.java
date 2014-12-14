package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameItemManager;
import org.xetang.manager.TankManager;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;

public class BigMom extends Tank {

	public BigMom(float px, float py) {

		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("EnemyBigMom"));
		this.SetType(ObjectType.ENEMY_TANK);
		this.SetTankType(TankType.BIG_MOM);

		this.speed = TankManager.NORMAL_TANK_SPEED;
		CurrentSprite = 0;
		_maxNumberBullet = 2;
		this.point = 400;
		this.hp = 4;
		mBulletType = ObjectType.BULLET;
		Animate();

	}

	@Override
	public void SetTankBonus(boolean bool) {
		// TODO Auto-generated method stub
		isTankBonus = bool;
		if (isTankBonus){ // xu ly nhap nhay
			CurrentSprite = 7;
			Animate();
		}
	}

	@Override
	public void onFire() {
		super.onFire();
	}

	@Override
	public boolean BeFire() {
		// TODO Auto-generated method stub
		if (isTankBonus) {
			GameItemManager.getInstance().CreateRandomItem();
			mSprite.stopAnimation();
			isTankBonus = false;
			CurrentSprite = 0;
		}
		this.hp--;
		if (hp == 0)
			return true;
		CurrentSprite += 2;
		Animate();
		return false;
	}

	@Override
	public int getBonusPoint() {
		return 400;
	}
}