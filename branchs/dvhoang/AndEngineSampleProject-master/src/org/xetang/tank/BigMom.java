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

		_maxNumberBullet = 2;
		this.point = 400;
		this.hp = 4;
		mBulletType = ObjectType.BULLET;
		mSprite.setCurrentTileIndex(0);

	}

	@Override
	public void SetTankBonus(boolean bool) {
		// TODO Auto-generated method stub
		isTankBonus = bool;
		if (isTankBonus) // xu ly nhap nhay
			mSprite.animate(new long[] { 200, 200 }, 2, 3, true);
	}

	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		super.onFire();
		CreateBullet(mBulletType, bPosX, bPosY);
	}

	@Override
	public boolean BeFire() {
		// TODO Auto-generated method stub
		if (isTankBonus) {
			GameItemManager.getInstance().CreateRandomItem();
			mSprite.stopAnimation();
			isTankBonus = false;
		}
		this.hp--;
		if (hp == 0)
			return true;

		mSprite.setCurrentTileIndex(4 - hp);
		return false;
	}

}