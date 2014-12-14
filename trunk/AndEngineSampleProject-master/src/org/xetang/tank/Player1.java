package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.TankManager;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectType;

public class Player1 extends Tank {

	public Player1(float px, float py) {
		this(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("PlayerTank"));
		CurrentSprite = 0;
		Animate();
	}

	public Player1(float px, float py, TiledTextureRegion region) {
		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("PlayerTank"));
		// TODO Auto-generated constructor stub
		this.mSprite.setCurrentTileIndex(0);

		this.speed = TankManager.NORMAL_TANK_SPEED;

		
		
		_maxNumberBullet = 1;
		mBulletType = ObjectType.SLOW_BULLET;
		this.hp = 1;
	}

	@Override
	public void PowerUp() {
		if (mSprite.getCurrentTileIndex() < 3)
			this.mSprite.setCurrentTileIndex(mSprite.getCurrentTileIndex() + 1);

		this.mLevel = mSprite.getCurrentTileIndex() + 1;
		switch (mLevel) {
		case 2: // Đạn bay nhanh như đạn của GlassCannon
			_maxNumberBullet = 1;
			mBulletType = ObjectType.FAST_BULLET;
			// this.hp = 2;
			CurrentSprite += 4;
			break;
		case 3:
			_maxNumberBullet = 2;
			// this.hp = 3;
			CurrentSprite += 4;
			break;
		case 4:
			mBulletType = ObjectType.BLOW_BULLET;
			// this.hp = 4;

			CurrentSprite += 4;
			break;
		}
	}

	public void OnHit() {
		int tmp = hp;
		tmp--;
		if (tmp == 0)
			KillSelf();
		else
			LevelDown(tmp);
	}

	private void LevelDown(int tmp) {
		this.mSprite.setCurrentTileIndex(mSprite.getCurrentTileIndex() - 1);
		this.mLevel = mSprite.getCurrentTileIndex() + 1;
		switch (tmp) {
		case 1:
			mBulletType = ObjectType.SLOW_BULLET;
			break;
		case 2: // Đạn bay nhanh như đạn của GlassCannon
			_maxNumberBullet = 1;
			mBulletType = ObjectType.FAST_BULLET;
			// this.hp = 2;
			break;
		case 3:
			_maxNumberBullet = 2;
			// this.hp = 3;
			break;
		}
	}

	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		super.onFire();
		CreateBullet(mBulletType, bPosX, bPosY);

	}
	
	@Override
	public void work() {
		super.work();
		CreateShield();
	}

}
