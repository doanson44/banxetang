package org.xetang.map.item;

import java.util.Random;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameItemManager;
import org.xetang.manager.GameManager;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObject;
import org.xetang.map.object.MapObjectFactory;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.root.GameEntity;
import org.xetang.tank.Tank;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * 
 */
public class Item extends GameEntity implements IMapObject {
	Tank _mOwner = null; // Xe tăng nhặt đc vật phẩm này
	int type;
	int _TimeSurvive = 0;
	int _TimeAffect = 0;

	static int _TotalTimeAffect = 10;
	static int _TotalTimeSurvive = 10;
	boolean mIsOutOfDate = false; 

	float _Score = 500;
	float _SecPerFrame = 0;
	float _alpha = 1;
	float _CellWidth = GameManager.LARGE_CELL_SIZE;
	float _CellHeight = GameManager.LARGE_CELL_SIZE;
	public TiledSprite _sprite = null;
	Body _body;
	boolean _isAlive = false;
	boolean _isActive = false;
	FixtureDef _fixtureDef;

	public Item() {
		_sprite = new TiledSprite(GetRandomPx(), GetRandomPy(),
				(ITiledTextureRegion) MapObjectFactory2.getTexture("Items"),
				GameManager.VertexBufferObject);
		_sprite.setSize(_CellWidth, _CellHeight);
		_isAlive = true;
		// this.attachChild(_sprite);
		CreateBody();
		setAlive(true);
	}

	public int GetRandomPx() {
		return new Random()
				.nextInt((int) (GameManager.MAP_SIZE - GameManager.LARGE_CELL_SIZE));
	}

	public int GetRandomPy() {
		return new Random()
				.nextInt((int) (GameManager.MAP_SIZE - GameManager.LARGE_CELL_SIZE));
	}

	public void affect() {
		// ....
	}

	protected void CreateBody() {
		_fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0, true,
				GameItemManager.CATEGORYBITS_ITEM,
				GameItemManager.MASKBITS_ITEM, MapObjectFactory.GROUP_DEFAULT);
		;
		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, _sprite,
				BodyType.StaticBody, _fixtureDef);
		_body.setUserData(this);
		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				_sprite, _body, true, true));

	}

	public void setOwner(Tank tank) {
		_mOwner = tank;
	}

	long preTime;
	

	public void update(float pSecondsElapsed) {

		Animate();
		_SecPerFrame += pSecondsElapsed;
		if (_SecPerFrame > 1) {
			_SecPerFrame = 0;
			_TimeSurvive++;
			if (_mOwner != null) {
				_TimeAffect++;
				//Log.i(GameManager.TANK_TAG, "Time Affect: " + String.valueOf(_TimeAffect));

			}
			//Log.i(GameManager.TANK_TAG, "Time Affect: " + String.valueOf(_mOwner));
			if (_TimeSurvive > _TotalTimeSurvive && _mOwner == null) {
				DestroyHelper.add(this);
				mIsOutOfDate = true;
			}
			if (_TimeAffect > _TotalTimeAffect) {
				DestroyAffect();
				mIsOutOfDate = true;
			}	
		}

		
	}

	Boolean flag = false;

	public void Animate() {
		if (_TimeSurvive > 4 && !flag || _alpha >= 1) {
			_alpha -= 0.08f;
			flag = false;
		}
		if (_alpha < 0.4 || flag) {
			flag = true;
			_alpha += 0.08f;
		}
		_sprite.setAlpha(_alpha);
	}

	public void DestroyAffect() {

	}

	public MapObject clone() {
		return null;
	}

	@Override
	public void putToWorld() {
		// TODO Auto-generated method stub
		if (_body == null) {
			CreateBody();
		}
	}

	@Override
	public void putToWorld(float posX, float posY) {
		// TODO Auto-generated method stub

		setPosition(posX, posY);
		putToWorld();

	}

	@Override
	public void setPosition(float posX, float posY) {
		// TODO Auto-generated method stub
		_sprite.setPosition(posX, posY);

		if (_body != null) {
			_body.setTransform(posX, posY, _body.getAngle());
		}
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return _sprite.getX();
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return _sprite.getY();
	}

	@Override
	public void transform(float x, float y) {
		// TODO Auto-generated method stub
		setPosition(_sprite.getX() + x, _sprite.getY() + y);
	}

	@Override
	public float getCellWidth() {
		// TODO Auto-generated method stub
		return _CellWidth;
	}

	@Override
	public float getCellHeight() {
		// TODO Auto-generated method stub
		return _CellHeight;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return _isAlive;
	}

	@Override
	public void setAlive(boolean alive) {
		// TODO Auto-generated method stub
		_isAlive = alive;
	}

	@Override
	public TiledSprite getSprite() {
		// TODO Auto-generated method stub
		return _sprite;
	}

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return _body;
	}

	@Override
	public FixtureDef getObjectFixtureDef() {
		return _fixtureDef;
	}

	@Override
	public void doContact(IMapObject object) {
		// TODO Auto-generated method stub
		try {
			Debug.d("Collsion", object.getType().name());
			if (object.getType() == ObjectType.PLAYER_TANK && !_isActive) {

				_mOwner = (Tank) object;
				affect();
				_isActive = true;
				GameItemManager.getInstance().pickupItem(this);

				DestroyHelper.add(this);
				GameManager.getSound("bonus").play();
				//Log.i(GameManager.TANK_TAG, "isOutOfDate: " +  String.valueOf(mIsOutOfDate));
			}
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
	}

	@Override
	public ObjectType getType() {
		return ObjectType.TANK_ITEM;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public float GetScore() {
		return _Score;
	}

	public int getBonusPoint() {
		return 100;
	}

	public boolean isOutOfDate() {
		return mIsOutOfDate;
	}
}