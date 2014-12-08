package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;

/**
 * 
 */
public class GlassCannon extends Tank {

	/**
     * 
     */
	public GlassCannon(float px, float py) {

		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("EnemyGlassCannon"));
		this.SetType(ObjectType.EnermyTank);
		this.SetTankType(TankType.GlassCannon);

		this.speed = 2f;
		_maxNumberBullet = 3;
		this.point = 300;
		this.hp = 1;
		mBulletType = ObjectType.FastBullet;
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