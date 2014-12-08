package org.xetang.tank;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.controller.IGameController;
import org.xetang.manager.GameItemManager;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.manager.TankManager;
import org.xetang.map.IBlowUp;
import org.xetang.map.IBullet;
import org.xetang.map.IMapObject;
import org.xetang.map.MapObject;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectLayer;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.MapObjectFactory.TankType;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.helper.DestroyHelper;
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
	protected ObjectType mBulletType = ObjectType.Bullet;
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
	float speed; // tốc độ của xe tăng
	TankType _TankType;
	ObjectType _objecType;
	FixtureDef _ObjectFixtureDef;
	float _distinctMove = 10;
	public static float CellSize = CalcHelper.CellSize;
	ObjectType _typeColide;

	public synchronized boolean isAlive() {
		return mIsAlive;
	}

	public Tank(float px, float py, TiledTextureRegion region) {
		// mBullet = new Bullet(this);
		TankManager.register(this);
		mSprite = new AnimatedSprite(px, py, region,
				GameManager.VertexBufferObject);
		mSprite.setSize(GameManager.LARGE_CELL_WIDTH - GameManager.MAP_WIDTH
				/ MapObjectFactory.TINY_CELL_PER_MAP,
				GameManager.LARGE_CELL_HEIGHT - GameManager.MAP_HEIGHT
						/ MapObjectFactory.TINY_CELL_PER_MAP);

		mDirection = Direction.Up;
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

		this.attachChild(mSprite);
	}

	public void KillSelf() {
		Vector2 centerPoint = GetCenterPoint();

		DestroyHelper.add(this);
		IBlowUp explosion = (IBlowUp) MapObjectFactory.createObject(
				ObjectType.Explosion, centerPoint.x, centerPoint.y);

		explosion.blowUpAtHere();
		GameManager.CurrentMap.attachChild((IEntity) explosion);
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
		if (mDirection == Direction.Up || mDirection == Direction.Down) {
			SetTranform(CalcHelper.CellInMap(mSprite));

		}
		if (mIsFreeze == 0) {
			mDirection = Direction.Left;

			SetTranform(270);
			_body.setLinearVelocity(-speed, 0);

		}
	}

	// di chuyển qua phải
	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.Up || mDirection == Direction.Down) {
			SetTranform(CalcHelper.CellInMap(mSprite));
		}

		if (mIsFreeze == 0) {
			mDirection = Direction.Right;

			SetTranform(90);
			_body.setLinearVelocity(speed, 0);
		}
	}

	// // di chuyển lên
	@Override
	public void onForward() {
		// TODO Auto-generated method stub
		if (mDirection == Direction.Left || mDirection == Direction.Right) {
			SetTranform(CalcHelper.CellInMap(mSprite));

		}

		if (mIsFreeze == 0) {
			mDirection = Direction.Up;
			SetTranform(0);
			_body.setLinearVelocity(0, -speed);
		}
	}

	// / di chuyển xuống
	@Override
	public void onBackward() {
		// TODO Auto-generated method stub

		if (mDirection == Direction.Left || mDirection == Direction.Right) {
			SetTranform(CalcHelper.CellInMap(mSprite));
		}

		if (mIsFreeze == 0) {
			mDirection = Direction.Down;
			SetTranform(180);
			_body.setLinearVelocity(0, speed);
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
		float x = point.x * CellSize / 2 + CellSize / 2;
		float y = point.y * CellSize / 2 + CellSize / 2;

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

				if (_shield.TimeSurvive == 7) {
					DestroyShield();
				}
			}

		}

	}

	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		float distinct = 0;
		Vector2 x = new Vector2(mSprite.getX(), mSprite.getY());
		switch (mDirection) {
		case Down:
			bPosX = x.x + mSprite.getHeight() / 2 - 5;
			bPosY = x.y + mSprite.getHeight() + distinct;
			break;
		case Left:
			bPosX = x.x - distinct;
			bPosY = x.y + mSprite.getWidth() / 2 - 5;
			break;
		case Right:
			bPosX = x.x + mSprite.getWidth() + distinct;
			bPosY = x.y + mSprite.getWidth() / 2 - 5;
			break;
		case Up:
			bPosX = x.x + mSprite.getWidth() / 2 - 5;
			bPosY = x.y - distinct;
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
			GameManager.CurrentMap.addObject((IEntity) bullet,
					ObjectLayer.Moving);
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
		_shield.KillSelf();
		_shield = null;
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