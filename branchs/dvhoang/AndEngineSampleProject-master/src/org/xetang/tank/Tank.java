package org.xetang.tank;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.math.MathConstants;
import org.xetang.controller.IGameController;
import org.xetang.manager.GameItemManager;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.manager.TankManager;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.map.object.IBlowUp;
import org.xetang.map.object.IBullet;
import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObject;
import org.xetang.map.object.MapObjectFactory;
import org.xetang.map.object.MapObjectFactory.ObjectLayer;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;
import org.xetang.root.GameEntity;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class Tank extends GameEntity implements IGameController,
		IMapObject, IUpdateHandler {

	ArrayList<IBullet> mBullet = new ArrayList<IBullet>();
	protected ObjectType mBulletType = ObjectType.BULLET;
	int _maxNumberBullet = 0;
	float bPosX = 0, bPosY = 0; // Toa do cua Dan

	boolean mIsFiring; // Xe tăng có đang bắn đạn hay không?
	Direction mDirection; // Hướng của xe tăng
	int mLevel; // Cấp độ của xe tăng
	boolean mIsAlive;
	int hp; // máu của xe tăng
	int point; // điểm đạt đc khi giết xe tank
	boolean isTankBonus; // tank có rớt item hay không.
	int mIsFreeze = 0;
	float _SecPerFrame = 0;
	Body _body;

	Shield _shield;
	AnimatedSprite mSprite;

	float speed;

	TankType _TankType;
	ObjectType _objecType;
	FixtureDef _ObjectFixtureDef;
	float _distinctMove = 10;
	ObjectType _typeColide;

	public synchronized boolean isAlive() {
		return mIsAlive;
	}

	public Tank(float px, float py, TiledTextureRegion region) {
		// mBullet = new Bullet(this);
		TankManager.register(this);
		mSprite = new AnimatedSprite(px, py, region,
				GameManager.VertexBufferObject);
		mSprite.setSize(GameManager.LARGE_CELL_SIZE
				- MapObjectFactory.TINY_CELL_SIZE, GameManager.LARGE_CELL_SIZE
				- MapObjectFactory.TINY_CELL_SIZE);
		mSprite.setRotationCenter(mSprite.getWidth() / 2f,
				mSprite.getHeight() / 2f);

		mDirection = Direction.UP;
		_ObjectFixtureDef = PhysicsFactory.createFixtureDef(0.5f, 0, 0);
		setAlive(true);
		CreateBody();
	}

	protected void CreateBody() {

		// TODO Auto-generated method stub
		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, mSprite,
				BodyType.DynamicBody, _ObjectFixtureDef);
		_body.setGravityScale(0);
		_body.setFixedRotation(true);
		_body.setUserData(this);
		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				mSprite, _body, true, true));

		// this.attachChild(mSprite);
	}

	public void KillSelf() {
		Vector2 centerPoint = GetCenterPoint();

		DestroyHelper.add(this);
		DestroyShield();
		IBlowUp explosion = (IBlowUp) MapObjectFactory.createObject(
				ObjectType.EXPLOSION, centerPoint.x, centerPoint.y);

		GameManager.CurrentMap.addObject(explosion, ObjectLayer.BLOW_UP);
		explosion.blowUpAtHere();
	}

	public TiledSprite GetCurrentSprite() {
		return mSprite;
	}

	public void loadOldData() {
		// TODO Auto-generated method stub

	}

	// di chuyển qua trái
	@Override
	public void onLeft() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.UP || mDirection == Direction.DOWN) {
			SetTranform(CalcHelper.CellInMap(mSprite));

		}
		if (mIsFreeze == 0) {
			mDirection = Direction.LEFT;

			SetTranform(-90);
			// _body.setLinearVelocity(-speed, 0);
			_body.setLinearVelocity(-speed, 0f);

		}
	}

	// di chuyển qua phải
	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.UP || mDirection == Direction.DOWN) {
			SetTranform(CalcHelper.CellInMap(mSprite));
		}

		if (mIsFreeze == 0) {
			mDirection = Direction.RIGHT;

			SetTranform(90);
			// _body.setLinearVelocity(speed, 0);
			_body.setLinearVelocity(speed, 0f);
		}
	}

	// // di chuyển lên
	@Override
	public void onForward() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.LEFT || mDirection == Direction.RIGHT) {
			SetTranform(CalcHelper.CellInMap(mSprite));

		}

		if (mIsFreeze == 0) {
			mDirection = Direction.UP;
			SetTranform(0);
			// _body.setLinearVelocity(0, -speed);
			_body.setLinearVelocity(0f, -speed);
		}
	}

	// / di chuyển xuống
	@Override
	public void onBackward() {
		// TODO Auto-generated method stub

		if (mDirection == Direction.LEFT || mDirection == Direction.RIGHT) {
			SetTranform(CalcHelper.CellInMap(mSprite));
		}

		if (mIsFreeze == 0) {
			mDirection = Direction.DOWN;
			SetTranform(-180);
			// _body.setLinearVelocity(0, speed);
			_body.setLinearVelocity(0f, speed);
		}
	}

	@Override
	public void onIdle() {
		// TODO Auto-generated method stub
		if (_body != null)
			_body.setLinearVelocity(0, 0);

	}

	// Hàm set vị trí của Xe tăng vào trọn 1 CELL trong bản đồ 26x26
	public void SetTranform(Vector2 point) {
		float angle = _body.getAngle();
		float x = (point.x + 1) * GameManager.SMALL_CELL_SIZE;
		float y = (point.y + 1) * GameManager.SMALL_CELL_SIZE;

		_body.setTransform(CalcHelper.pixels2Meters(x),
				CalcHelper.pixels2Meters(y), angle);

	}

	// Hàm xoay lại Body của xe tăng cho phù hợp với hướng di chuyển
	public void SetTranform(float degree) {
		_body.setTransform(_body.getTransform().getPosition(), 0);
		_body.setTransform(_body.getTransform().getPosition(), degree
				* MathConstants.DEG_TO_RAD);

	}

	public void Update(float pSecondsElapsed) {
		for (int i = 0; i < mBullet.size(); i++) {
			if (!mBullet.get(i).isAlive())
				mBullet.remove(i);
		}
		if (_shield != null && _shield.IsAlive()) {
			_shield.GetSprite().setX(mSprite.getX());
			_shield.GetSprite().setY(mSprite.getY());
		}

		_SecPerFrame += pSecondsElapsed;
		if (_SecPerFrame > 1) {
			_SecPerFrame = 0;
			if (mIsFreeze > 0) {
				mIsFreeze -= 1;
				Log.i("mIsFreeze", String.valueOf(mIsFreeze));
			}
			if (_shield != null && _shield.IsAlive()) {
				_shield.TimeSurvive++;
				Log.i("TimeSurvive", String.valueOf(_shield.TimeSurvive));

				if (_shield.TimeSurvive == 7) {
					DestroyShield();
				}
			}

		}

	}

	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		Vector2 x = new Vector2(mSprite.getX(), mSprite.getY());
		Vector2 bulletSize = MapObjectFactory.getBulletSize();

		switch (mDirection) {
		case UP:
			bPosX = x.x + (mSprite.getWidth() - bulletSize.x) / 2f;
			bPosY = x.y - bulletSize.y - 1;
			break;
		case DOWN:
			bPosX = x.x + (mSprite.getWidth() - bulletSize.x) / 2f;
			bPosY = x.y + mSprite.getHeight();
			break;
		case LEFT:
			bPosX = x.x - (bulletSize.y + bulletSize.x) / 2f;
			bPosY = x.y + (mSprite.getWidth() - bulletSize.y) / 2f;
			break;
		case RIGHT:
			bPosX = x.x + mSprite.getHeight() + (bulletSize.y - bulletSize.x)
					/ 2f;
			bPosY = x.y + (mSprite.getWidth() - bulletSize.y) / 2f;
			break;
		default:
			break;
		}
	}

	public boolean BeFire() {
		if (isTankBonus)
			GameItemManager.getInstance().CreateRandomItem();
		this.hp--;
		if (hp == 0)
			return true;
		return false;
	}

	public void CreateBullet(ObjectType type, float bPosX2, float bPosY2) {

		if (mBullet.size() < _maxNumberBullet && mIsFreeze == 0) {
			IBullet bullet = (IBullet) MapObjectFactory.createObject(type,
					bPosX2, bPosY2);
			bullet.setTank(this);
			GameManager.CurrentMap.addObject(bullet, ObjectLayer.MOVING);
			mBullet.add(bullet);
			bullet.readyToFire(mDirection);
			bullet.beFired();
		}
	}

	public void CreateShield() {
		// TODO Auto-generated method stub
		_shield = new Shield(mSprite.getX() - 8, mSprite.getY() - 8);
	}

	public void DestroyShield() {
		if (_shield != null) {
			_shield.KillSelf();
			_shield = null;
		}
	}

	public Vector2 GetCenterPoint() {
		Vector2 centerPoint = new Vector2(mSprite.getX() + mSprite.getHeight()
				/ 2, mSprite.getY() + mSprite.getWidth() / 2);
		return centerPoint;
	}

	public void PowerUp() {
		// TODO Auto-generated method stub

	}

	public void FreezeSelf() {
		// TODO Auto-generated method stub
		mIsFreeze = 7;
	}

	public MapObject clone() {
		return null;
	}

	public void SetTankBonus(boolean bool) {
		isTankBonus = bool;
	}

	public boolean GetTankBonus() {
		return isTankBonus;
	}

	@Override
	public void putToWorld() {
		// TODO Auto-generated method stub
		if (_body != null) {
			CreateBody();
		}
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
	public AnimatedSprite getSprite() {
		// TODO Auto-generated method stub
		return mSprite;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return mSprite.getX();
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return mSprite.getY();
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
		if (object != null && object != this) {
			_typeColide = object.getType();
		} else
			_typeColide = null;
	}

	@Override
	public ObjectType getType() {
		// TODO Auto-generated method stub
		return _objecType;
	}

	public ObjectType GetCollide() {
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

	public void SetDirection(Direction direction) {
		mDirection = direction;
	}

	public Shield getShield() {
		// TODO Auto-generated method stub
		return _shield;
	}

}