package org.xetang.tank;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.Bullet;
import org.xetang.map.IBullet;
import org.xetang.map.Map;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.MapObjectFactory.TankType;
import org.xetang.map.MapObjectFactory2;

import com.badlogic.gdx.math.Vector2;

public class Player1 extends Tank {

	public Player1(float px, float py) {
		this(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("Player1"));
	}
	public Player1(float px, float py, TiledTextureRegion region) {
		super(px, py, (TiledTextureRegion) MapObjectFactory2
				.getTexture("Player1"));
		// TODO Auto-generated constructor stub
		this.mSprite.setCurrentTileIndex(0);
		this.speed = 2f;
		_maxNumberBullet = 1;
		mBulletType = ObjectType.SlowBullet;
		this.hp = 1;
		CreateShield();
	}



	@Override
	public void PowerUp() {
		if (mSprite.getCurrentTileIndex() < 3)
			this.mSprite.setCurrentTileIndex(mSprite.getCurrentTileIndex() + 1);
		
		this.mLevel = mSprite.getCurrentTileIndex() + 1;
		switch (mLevel) {
		case 2:
			this.speed = 3f;
			_maxNumberBullet = 1;
			//this.hp = 2;
			mBulletType = ObjectType.Bullet;
			break;
		case 3:
			this.speed = 3.5f;
			_maxNumberBullet = 2;
		//	this.hp = 3;
			break;
		case 4:
			this.speed = 4.5f;
			_maxNumberBullet = 2;
			mBulletType = ObjectType.FastBullet;
			//this.hp = 4;
			
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
			this.speed = 1f;
			_maxNumberBullet = 1;
			this.hp = 1;
			break;
		case 2:
			this.speed = 2f;
			_maxNumberBullet = 1;
			this.hp = 2;
			break;
		case 3:
			this.speed = 3.5f;
			_maxNumberBullet = 2;
			this.hp = 3;
			break;
		}
	}

	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		super.onFire();
		CreateBullet(mBulletType, bPosX, bPosY);

	}

}
