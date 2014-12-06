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

	public Player1(float px, float py, TiledTextureRegion region) {
    	super(px, py,(TiledTextureRegion) MapObjectFactory2.getTexture("Player1"));
		// TODO Auto-generated constructor stub
    	this.tankSprite.setCurrentTileIndex(0);
    	this.speed = 1f;
    	_maxNumberBullet = 1;
    	this.SetTankType(TankType.Normal);
    	this.hp = 1;
	}

	public void LevelUp()
	{
		if (tankSprite.getCurrentTileIndex()<3) 
		this.tankSprite.setCurrentTileIndex(tankSprite.getCurrentTileIndex()+1);
		this.mLevel = tankSprite.getCurrentTileIndex()+1;
		switch (mLevel)
		{
		case 2:
			this.speed = 2f;
			_maxNumberBullet = 1;
			this.hp = 2;
		case 3:
			this.speed = 3.5f;
			_maxNumberBullet = 2;
			this.hp = 3;
		case 4:
			this.speed = 5f;
			_maxNumberBullet = 2;
			this.hp = 4;
		}
	}
	
	public void OnHit()
	{
		int tmp = hp;
		tmp--;
		if (tmp==0) KillSelf();
		else LevelDown(tmp);
	}
	
	private void LevelDown(int tmp) {
		this.tankSprite.setCurrentTileIndex(tankSprite.getCurrentTileIndex()-1);
		this.mLevel = tankSprite.getCurrentTileIndex()+1;
		switch (tmp)
		{
		case 1:
			this.speed = 1f;
			_maxNumberBullet = 1;
			this.hp = 1;
		case 2:
			this.speed = 2f;
			_maxNumberBullet = 1;
			this.hp = 2;
		case 3:
			this.speed = 3.5f;
			_maxNumberBullet = 2;
			this.hp = 3;
		}
	}

	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		super.onFire();
    	CreateBullet(ObjectType.Bullet, bPosX, bPosY);	

	}
	
}
