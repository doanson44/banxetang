package org.xetang.tank;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
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

public class Tank extends GameEntity implements IGameController, IMapObject {

	float DEGTORAD = 0.0174532925199432957f;
	float PIXEL_TO_METERS_RATIO = PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
	Controller mController;

	ArrayList<IBullet> mBullet = new ArrayList<IBullet>();
	int _maxNumberBullet = 0;
	float bPosX = 0, bPosY = 0; // Toa do cua Dan

	boolean mIsFiring; // Xe tăng có đang bắn đạn hay không?
	Direction mDirection; // Hướng của xe tăng
	int mLevel; // Cấp độ của xe tăng
	boolean mIsAlive;

	int mIsFreeze = 0;
	int _SecPerFrame = 0;
	Body _body;
	Map _map;

	Shield _shield;
	TiledSprite tankSprite;
	float speed; // tốc độ của xe tăng
	ObjectType _type;
	FixtureDef _ObjectFixtureDef;
	float _distinctMove = 10;
	float _CellSize = GameManager.MAP_HEIGHT / 13;

	public synchronized boolean isAlive() {
		return mIsAlive;
	}

	public void killSelf() {
		mIsAlive = false;
		Vector2 centerPoint = new Vector2(tankSprite.getX()
				+ tankSprite.getHeight() / 2, tankSprite.getY()
				+ tankSprite.getWidth() / 2);

		IBlowUp explosion = (IBlowUp) MapObjectFactory.createObject(
				ObjectType.Explosion, centerPoint.x, centerPoint.y);

		explosion.blowUpAtHere();
		_map.attachChild((IEntity) explosion);
		mController.onTankDie();
	}

	public TiledSprite GetCurrentSprite() {
		return tankSprite;
	}

	public Tank(int px, int py, Map map, TiledTextureRegion region) {
		// mBullet = new Bullet(this);
		_map = map;
		TankManager.register(this);
		tankSprite = new TiledSprite(px, py, region,
				GameManager.VertexBufferObject);
		tankSprite.setSize(GameManager.LARGE_CELL_WIDTH - GameManager.MAP_WIDTH
				/ MapObjectFactory.TINY_CELL_PER_MAP,
				GameManager.LARGE_CELL_HEIGHT - GameManager.MAP_HEIGHT
						/ MapObjectFactory.TINY_CELL_PER_MAP);
		mDirection = Direction.Down;
		_ObjectFixtureDef = PhysicsFactory.createFixtureDef(0.5f, 0, 0);

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

	public void setController(Controller controller) {
		this.mController = controller;
	}

	public void loadOldData() {
		// TODO Auto-generated method stub

	}

	// di chuyển qua trái
	@Override
	public void onLeft() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.Up || mDirection == Direction.Down) {
			SetTranform(Direction.Left);
		}
		mDirection = Direction.Left;

		SetTranform(90);
		_body.setLinearVelocity(-speed, 0);
	}

	// di chuyển qua phải
	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.Up || mDirection == Direction.Down) {
			SetTranform(Direction.Right);
		}
		mDirection = Direction.Right;

		SetTranform(270);

		_body.setLinearVelocity(speed, 0);

	}

	// // di chuyển lên
	@Override
	public void onForward() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.Left || mDirection == Direction.Right) {
			SetTranform(Direction.Up);
		}
		mDirection = Direction.Up;
		SetTranform(180);
		_body.setLinearVelocity(0, -speed);

	}

	// / di chuyển xuống
	@Override
	public void onBackward() {
		// TODO Auto-generated method stub

		if (mDirection == Direction.Left || mDirection == Direction.Right) {
			SetTranform(Direction.Down);
		}
		mDirection = Direction.Down;

		_body.setTransform(_body.getTransform().getPosition(), 0 * DEGTORAD);
		_body.setLinearVelocity(0, speed);

	}

	@Override
	public void onIdle() {
		// TODO Auto-generated method stub
		_body.setLinearVelocity(0, 0);

	}

	public void SetTranform(Direction current) {
		Vector2 centerPoint = new Vector2(tankSprite.getX()
				+ tankSprite.getHeight() / 2, tankSprite.getY()
				+ tankSprite.getWidth() / 2);

		SetTranform(CellInMap(centerPoint, 0, 0));
		Vector2 cell = null;
		/*
		 * switch (current) { case Down: cell = CellInMap(centerPoint, 0, 1);
		 * if(!CheckCell(cell)){ SetTranform(CellInMap(centerPoint, 0, 0)); }
		 * break; case Up: cell = CellInMap(centerPoint, 0, -1);
		 * if(!CheckCell(cell)){ SetTranform(CellInMap(centerPoint, 0, 0)); }
		 * break; case Left: cell = CellInMap(centerPoint, -1, 0);
		 * if(!CheckCell(cell)){ SetTranform(CellInMap(centerPoint, 0, 0)); }
		 * break; case Right: cell = CellInMap(centerPoint, 1, 0);
		 * if(!CheckCell(cell)){ SetTranform(CellInMap(centerPoint, 0, 0)); }
		 * break; default: break; }
		 */

	}

	// Ham kiem tra xem cell co chua' vat can hay k
	public boolean CheckCell(Vector2 cell) {
		MoveCallBack callback = new MoveCallBack();
		float Cellx = (cell.x * _CellSize / 2 + _CellSize / 2)
				/ PIXEL_TO_METERS_RATIO;
		float Celly = (cell.y * _CellSize / 2 + _CellSize / 2)
				/ PIXEL_TO_METERS_RATIO;
		GameManager.PhysicsWorld
				.QueryAABB(callback, Cellx, Celly, Cellx, Celly);
		if (callback.CheckExist()) {
			Log.i("callback", String.valueOf(cell.x) + String.valueOf(cell.y));
			return true;
		}
		return false;
	}

	// Ham tra ve vi tri cua Cell muon tim
	public Vector2 CellInMap(Vector2 v, int x, int y) {
		return new Vector2((Math.round(v.x / (_CellSize / 2)) - 1 + x),
				Math.round(v.y / (_CellSize / 2)) - 1 + y);
	}

	public void SetTranform(Vector2 point) {
		float angle = _body.getAngle();
		float x = point.x * _CellSize / 2 + _CellSize / 2;
		float y = point.y * _CellSize / 2 + _CellSize / 2;

		_body.setTransform((x) / PIXEL_TO_METERS_RATIO, (y)
				/ PIXEL_TO_METERS_RATIO, angle);

	}

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
		_shield = new Shield(tankSprite.getX() - 5, tankSprite.getY() - 5, _map);
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
	}

	@Override
	public ObjectType getType() {
		// TODO Auto-generated method stub
		return ObjectType.PlayerTank;
	}

	public void SetType(ObjectType type) {
		_type = type;
	}

	public Shield getShield() {
		// TODO Auto-generated method stub
		return _shield;
	}

}