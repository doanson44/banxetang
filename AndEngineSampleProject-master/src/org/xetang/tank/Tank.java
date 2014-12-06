package org.xetang.tank;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.controller.Controller;
import org.xetang.controller.IGameController;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.manager.GameMapManager;
import org.xetang.manager.TankManager;
import org.xetang.map.Bullet;
import org.xetang.map.Explosion;
import org.xetang.map.IBlowUp;
import org.xetang.map.IBullet;
import org.xetang.map.IMapObject;
import org.xetang.map.Map;
import org.xetang.map.MapObject;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.MapObjectFactory.TankType;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.map.MapObjectFactory2;
import org.xetang.root.GameEntity;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * 
 */

public class Tank extends GameEntity implements IGameController, IMapObject,
		IUpdateHandler {

	float DEGTORAD = 0.0174532925199432957f;
	float PIXEL_TO_METERS_RATIO = PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

	ArrayList<IBullet> mBullet = new ArrayList<IBullet>();
	int _maxNumberBullet = 0;
	float bPosX = 0, bPosY = 0; // Toa do cua Dan

	boolean mIsFiring; // Xe tăng có đang bắn đạn hay không?
	Direction mDirection; // Hướng của xe tăng
	int mLevel; // Cấp độ của xe tăng
	boolean mIsAlive;
	int hp; // máu của xe tăng
	int point; // điểm đạt đc khi giết xe tank
	int isTankBonus; // tank có rớt item hay không, rớt = 1.
	int mIsFreeze = 0;
	int _SecPerFrame = 0;
	Body _body;

	Shield _shield;
	AnimatedSprite tankSprite;
	float speed; // tốc độ của xe tăng
	TankType _TankType;
	ObjectType _objecType;
	FixtureDef _ObjectFixtureDef;
	float _distinctMove = 10;
	float _CellSize = GameManager.MAP_HEIGHT / 13;
	float SaiSoX = 0, SaiSoY = 0;
	int DistinctWithSprite = 5;
	ObjectType _typeColide;
	
	public synchronized boolean isAlive() {
		return mIsAlive;
	}

	public Tank(float px, float py, TiledTextureRegion region) {
		// mBullet = new Bullet(this);
		TankManager.register(this);
		tankSprite = new AnimatedSprite(px, py, region,
				GameManager.VertexBufferObject);
		tankSprite.setSize(GameManager.LARGE_CELL_WIDTH - GameManager.MAP_WIDTH
				/ MapObjectFactory.TINY_CELL_PER_MAP,
				GameManager.LARGE_CELL_HEIGHT - GameManager.MAP_HEIGHT
						/ MapObjectFactory.TINY_CELL_PER_MAP);
		mDirection = Direction.Up;
		_ObjectFixtureDef = PhysicsFactory.createFixtureDef(0.5f, 0, 0);
		setAlive(true);
		CreateBody();
	}

	protected void CreateBody() {
		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld,
				tankSprite, BodyType.DynamicBody, _ObjectFixtureDef);
		_body.setGravityScale(0);
		_body.setFixedRotation(true);
		_body.setUserData(this);
		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				tankSprite, _body, true, true));
		this.attachChild(tankSprite);
	}

	public void KillSelf() {
		Vector2 centerPoint = new Vector2(tankSprite.getX()
				+ tankSprite.getHeight() / 2, tankSprite.getY()
				+ tankSprite.getWidth() / 2);

		DestroyHelper.add(this);
		IBlowUp explosion = (IBlowUp) MapObjectFactory.createObject(
				ObjectType.Explosion, centerPoint.x, centerPoint.y);

		explosion.blowUpAtHere();
		GameManager.CurrentMap.attachChild((IEntity) explosion);
	}

	public TiledSprite GetCurrentSprite() {
		return tankSprite;
	}

	public void loadOldData() {
		// TODO Auto-generated method stub

	}

	// di chuyển qua trái
	@Override
	public void onLeft() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.Up || mDirection == Direction.Down) {
			SetTranform(CellInMap());

		}
		SaiSoX = -DistinctWithSprite;
		SaiSoY = _CellSize / 2;
		mDirection = Direction.Left;

		SetTranform(270);
		_body.setLinearVelocity(-speed, 0);
	}

	// di chuyển qua phải
	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.Up || mDirection == Direction.Down) {
			SetTranform(CellInMap());
		}

		SaiSoX = DistinctWithSprite;
		SaiSoY = _CellSize / 2;
		mDirection = Direction.Right;

		SetTranform(90);

		_body.setLinearVelocity(speed, 0);

	}

	// // di chuyển lên
	@Override
	public void onForward() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.Left || mDirection == Direction.Right) {
			SetTranform(CellInMap());

		}
		SaiSoX = _CellSize/2;
		SaiSoY = -DistinctWithSprite;
		mDirection = Direction.Up;
		SetTranform(0);
		_body.setLinearVelocity(0, -speed);

	}


	// / di chuyển xuống
	@Override
	public void onBackward() {
		// TODO Auto-generated method stub

		if (mDirection == Direction.Left || mDirection == Direction.Right) {
			SetTranform(CellInMap());
		}
		
		SaiSoX = _CellSize/2;
		SaiSoY = DistinctWithSprite;
		mDirection = Direction.Down;
		SetTranform(180);
		_body.setLinearVelocity(0, speed);

	}

	@Override
	public void onIdle() {
		// TODO Auto-generated method stub
		_body.setLinearVelocity(0, 0);

	}
/*
	// Ham kiem tra xem cell co chua' vat can hay k
	public ObjectType CheckCell(int x, int y) {
		Vector2 centerPoint = new Vector2(tankSprite.getX()
				+ tankSprite.getHeight() / 2, tankSprite.getY()
				+ tankSprite.getWidth() / 2);

		int centerX = (int) (centerPoint.x / _CellSize);
		int centerY = (int) (centerPoint.y / _CellSize);
		MoveCallBack callback = new MoveCallBack();
		float Cellx = ((centerX + x) * _CellSize + SaiSoX) / PIXEL_TO_METERS_RATIO;

		float Celly = ((centerY + y) * _CellSize + SaiSoY) / PIXEL_TO_METERS_RATIO;

		GameManager.PhysicsWorld
				.QueryAABB(callback, Cellx, Celly, Cellx, Celly);

		IMapObject object = callback.CheckExist();
		if (object != null && object != this)
			return object.getType();
		return null;
	}*/

	// Hàm trả về vị trí CELL của xe tăng trong Map 26x26
	public Vector2 CellInMap() {
		Vector2 centerPoint = new Vector2(tankSprite.getX()
				+ tankSprite.getHeight() / 2, tankSprite.getY()
				+ tankSprite.getWidth() / 2);

		return new Vector2((Math.round(centerPoint.x / (_CellSize / 2)) - 1),
				Math.round(centerPoint.y / (_CellSize / 2)) - 1);
	}
	
	public Vector2 CellInMap13(){
		Vector2 centerPoint = new Vector2(tankSprite.getX()
				+ tankSprite.getHeight() / 2, tankSprite.getY()
				+ tankSprite.getWidth() / 2);

		int centerX = (int) (centerPoint.x / _CellSize);
		int centerY = (int) (centerPoint.y / _CellSize);
		return new Vector2(centerX,centerY );
	}

	// Hàm set vị trí của Xe tăng vào trọn 1 CELL trong bản đồ 26x26
	public void SetTranform(Vector2 point) {
		float angle = _body.getAngle();
		float x = point.x * _CellSize / 2 + _CellSize / 2;
		float y = point.y * _CellSize / 2 + _CellSize / 2;

		_body.setTransform((x) / PIXEL_TO_METERS_RATIO, (y)
				/ PIXEL_TO_METERS_RATIO, angle);

	}

	// Hàm xoay lại Body của xe tăng cho phù hợp với hướng di chuyển
	public void SetTranform(float degree) {
		_body.setTransform(_body.getTransform().getPosition(), 0);
		_body.setTransform(_body.getTransform().getPosition(), degree
				* DEGTORAD);

	}

	public void Update(float pSecondsElapsed) {
		for (IBullet bullet : mBullet) {
			if (!bullet.isAlive())
				mBullet.remove(bullet);
		}

		if (_shield != null && _shield.IsAlive()) {
			_shield.GetSprite().setX(tankSprite.getX());
			_shield.GetSprite().setY(tankSprite.getY());
		}

		if (mIsFreeze > 0) {
			_SecPerFrame += pSecondsElapsed;
			if (_SecPerFrame > 1) {
				mIsFreeze -= 1;
			}
			_body.setLinearVelocity(0, 0);
		}

	}

	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		float distinct = 0;
		Vector2 x = new Vector2(tankSprite.getX(), tankSprite.getY());
		switch (mDirection) {
		case Down:
			bPosX = x.x + tankSprite.getHeight() / 2 - 5;
			bPosY = x.y + tankSprite.getHeight() + distinct;
			break;
		case Left:
			bPosX = x.x - distinct;
			bPosY = x.y + tankSprite.getWidth() / 2 - 5;
			break;
		case Right:
			bPosX = x.x + tankSprite.getWidth() + distinct;
			bPosY = x.y + tankSprite.getWidth() / 2 - 5;
			break;
		case Up:
			bPosX = x.x + tankSprite.getWidth() / 2 - 5;
			bPosY = x.y - distinct;
			break;
		default:
			break;
		}
	}

	public void CreateBullet(ObjectType type, float bPosX2, float bPosY2) {

		if (mBullet.size() < _maxNumberBullet) {
			IBullet bullet = (IBullet) MapObjectFactory.createObject(type,
					bPosX2, bPosY2);
			bullet.setTank(this);
			GameManager.CurrentMapManager.addBullet(bullet);
			mBullet.add(bullet);
			bullet.readyToFire(mDirection);
			bullet.beFired();
		}
	}

	public void CreateShield() {
		// TODO Auto-generated method stub
		_shield = new Shield(tankSprite.getX() - 5, tankSprite.getY() - 5);
	}

	public void DestroyShield() {
		_shield.KillSelf();
		_shield = null;
	}

	public void PowerUp() {
		// TODO Auto-generated method stub

	}

	public void FreezeSelf() {
		// TODO Auto-generated method stub
		mIsFreeze = 3;
	}

	public MapObject clone() {
		return null;
	}

	@Override
	public void putToWorld() {
		// TODO Auto-generated method stub

	}

	@Override
	public void putToWorld(float posX, float posY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transform(float x, float y) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getCellWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCellHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public synchronized void setAlive(boolean alive) {
		mIsAlive = alive;
	}

	@Override
	public TiledSprite getSprite() {
		// TODO Auto-generated method stub
		return tankSprite;
	}

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return _body;
	}

	@Override
	public FixtureDef getObjectFixtureDef() {
		// TODO Auto-generated method stub
		return _ObjectFixtureDef;
	}

	@Override
	public void doContact(IMapObject object) {
		// TODO Auto-generated method stub
		if(object != null && object != this){
			_typeColide = object.getType();
		}
		else
			_typeColide = null;
	}

	@Override
	public ObjectType getType() {
		// TODO Auto-generated method stub
		return _objecType;
	}

	public ObjectType GetCollide (){
		return _typeColide;
	}
	public void SetType(ObjectType type) {
		_objecType = type;
	}

	public void SetTankType(TankType type) {
		_TankType = type;
	}

	public TankType GetTankType() {
		return _TankType;
	}

	public void SetDirection(Direction direction){
		mDirection = direction;
	}
	public Shield getShield() {
		// TODO Auto-generated method stub
		return _shield;
	}

}