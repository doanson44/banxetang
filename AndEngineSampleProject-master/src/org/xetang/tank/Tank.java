package org.xetang.tank;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.math.MathConstants;
import org.xetang.controller.Controller;
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

	int CurrentSprite = 0;
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

	Controller mController;

	Shield _shield;
	AnimatedSprite mSprite;

	float speed;

	TankType _TankType;
	ObjectType _objecType;
	FixtureDef _ObjectFixtureDef;
	float _distinctMove = 10;
	ObjectType _typeColide;

	Flicker mFlicker;
	boolean mIsAppearing = true; // xe tăng đang xuát hiện?

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

		mSprite.setVisible(false);
		mDirection = Direction.UP;
		_ObjectFixtureDef = PhysicsFactory.createFixtureDef(0.5f, 0, 0);
		setAlive(true);

		createFlicker();
	}

	private void createFlicker() {
		mFlicker = new Flicker(this.getX(), this.getY());
		mFlicker.Animate();
		mFlicker.SetTank(this);
		mIsAppearing = true;
	}

	protected void CreateBody() {
		/*
		 * GameManager.Context.runOnUpdateThread(new Runnable() {
		 * 
		 * @Override public void run() { _body =
		 * PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, mSprite,
		 * BodyType.DynamicBody, _ObjectFixtureDef); _body.setGravityScale(0);
		 * _body.setFixedRotation(true); _body.setUserData(this);
		 * GameManager.PhysicsWorld.registerPhysicsConnector(new
		 * PhysicsConnector( mSprite, _body, true, true));
		 * 
		 * // this.attachChild(mSprite); mSprite.setVisible(true); } });
		 */
		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, mSprite,
				BodyType.DynamicBody, _ObjectFixtureDef);
		_body.setGravityScale(0);
		_body.setFixedRotation(true);
		_body.setUserData(this);
		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				mSprite, _body, true, true));

		// this.attachChild(mSprite);
		mSprite.setVisible(true);

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
		if (!isReady() || mIsFreeze > 0) {
			if (_body != null)
				_body.setLinearVelocity(0f, 0f);
			return;
		}

		if (mDirection == Direction.UP || mDirection == Direction.DOWN) {
			SetTranform(CalcHelper.CellInMap(mSprite));

		}
		if (mIsFreeze == 0) {
			mDirection = Direction.LEFT;

			SetTranform(-90);
			_body.setLinearVelocity(-speed, 0f);
			if (!mSprite.isAnimationRunning())
				mSprite.animate(new long[] { 100, 100 }, CurrentSprite,
						CurrentSprite + 1, true);

		}
	}

	// di chuyển qua phải
	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		if (!isReady() || mIsFreeze > 0) {
			if (_body != null)
				_body.setLinearVelocity(0f, 0f);
			return;
		}

		if (mDirection == Direction.UP || mDirection == Direction.DOWN) {
			SetTranform(CalcHelper.CellInMap(mSprite));
		}

		if (mIsFreeze == 0) {
			mDirection = Direction.RIGHT;

			SetTranform(90);
			_body.setLinearVelocity(speed, 0f);
			if (!mSprite.isAnimationRunning())
				mSprite.animate(new long[] { 100, 100 }, CurrentSprite,
						CurrentSprite + 1, true);
		}
	}

	// // di chuyển lên
	@Override
	public void onForward() {
		// TODO Auto-generated method stub
		if (!isReady() || mIsFreeze > 0) {
			if (_body != null)
				_body.setLinearVelocity(0f, 0f);
			return;
		}

		if (mDirection == Direction.LEFT || mDirection == Direction.RIGHT) {
			SetTranform(CalcHelper.CellInMap(mSprite));

		}

		if (mIsFreeze == 0) {
			mDirection = Direction.UP;
			SetTranform(0);
			_body.setLinearVelocity(0f, -speed);
			if (!mSprite.isAnimationRunning())
				mSprite.animate(new long[] { 100, 100 }, CurrentSprite,
						CurrentSprite + 1, true);
		}
	}

	// / di chuyển xuống
	@Override
	public void onBackward() {
		// TODO Auto-generated method stub

		if (!isReady() || mIsFreeze > 0) {
			if (_body != null)
				_body.setLinearVelocity(0f, 0f);
			return;
		}

		if (mDirection == Direction.LEFT || mDirection == Direction.RIGHT) {
			SetTranform(CalcHelper.CellInMap(mSprite));
		}

		if (mIsFreeze == 0) {
			mDirection = Direction.DOWN;
			SetTranform(-180);
			_body.setLinearVelocity(0f, speed);
			if (!mSprite.isAnimationRunning())
				mSprite.animate(new long[] { 100, 100 }, CurrentSprite,
						CurrentSprite + 1, true);
		}
	}

	@Override
	public void onIdle() {
		// TODO Auto-generated method stub
		if (_body != null)
			_body.setLinearVelocity(0, 0);

		mSprite.stopAnimation();
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

	long preTime = 0;
	long miliTimeElapsed = 0;

	public void Update(float pSecondsElapsed) {
		for (int i = 0; i < mBullet.size(); i++) {
			if (!mBullet.get(i).isAlive())
				mBullet.remove(i);
		}
		if (_shield != null && _shield.IsAlive()) {
			_shield.GetSprite().setX(
					mSprite.getX() - MapObjectFactory.TINY_CELL_SIZE / 2f);
			_shield.GetSprite().setY(
					mSprite.getY() - MapObjectFactory.TINY_CELL_SIZE / 2f);
		}

		miliTimeElapsed += System.currentTimeMillis() - preTime;
		preTime = System.currentTimeMillis();

		_SecPerFrame += pSecondsElapsed;
		_SecPerFrame += miliTimeElapsed / 1000;
		miliTimeElapsed %= 1000;
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
		if (!isReady() || mIsFreeze > 0) {

			return;
		}

		Vector2 x = new Vector2(mSprite.getX(), mSprite.getY());
		Vector2 bulletSize = MapObjectFactory.getBulletSize();

		switch (mDirection) {
		case UP:
			bPosX = x.x + (mSprite.getWidth() - bulletSize.x) / 2f;
			bPosY = x.y;
			break;
		case DOWN:
			bPosX = x.x + (mSprite.getWidth() - bulletSize.x) / 2f;
			bPosY = x.y + mSprite.getHeight() - bulletSize.y;
			break;
		case LEFT:
			bPosX = x.x;
			bPosY = x.y + (mSprite.getWidth() - bulletSize.y) / 2f;
			break;
		case RIGHT:
			bPosX = x.x + mSprite.getHeight() - bulletSize.y;
			bPosY = x.y + (mSprite.getWidth() - bulletSize.y) / 2f;
			break;
		default:
			break;
		}

		CreateBullet(mBulletType, bPosX, bPosY);
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

			if (getType() == ObjectType.PLAYER_TANK)
				GameManager.getSound("fire").play();
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
		mSprite.stopAnimation();
	}

	public void Animate() {
		if (mSprite.isAnimationRunning())
			mSprite.stopAnimation();

		mSprite.animate(new long[] { 100, 100 }, CurrentSprite,
				CurrentSprite + 1, true);
	}

	public MapObject clone() {
		return null;
	}

	public void SetTankBonus(boolean bool) {
		isTankBonus = bool;
		if (isTankBonus) {// xu ly nhap nhay
			CurrentSprite += 1;
			Animate();
		}
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
	public void setAlive(boolean alive) {
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

		if (mController != null)
			if (object == null)
				mController.onCollide(null);
			else
				mController.onCollide(object.getType());

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

	public TankType getTankType() {
		return _TankType;
	}

	public void SetDirection(Direction direction) {
		mDirection = direction;
	}

	public Shield getShield() {
		// TODO Auto-generated method stub
		return _shield;
	}

	public int getBonusPoint() {
		return 0;
	}

	/**
	 * Ra lệnh cho xe tank làm việc đi
	 */
	public void work() {
		/*
		 * Runnable run = new Runnable() {
		 * 
		 * @Override public void run() { CreateBody(); } };
		 * 
		 * GameManager.Context.runOnUpdateThread(run);
		 */
		mIsAppearing = true;
		mFlicker.setAppearing();

	}

	public void setAppearingDone() {
		mIsAppearing = false;
		CreateBody();

	}

	@Override
	public boolean isReady() {
		return !mIsAppearing;
	}

	public void setController(Controller controller) {
		mController = controller;
	}

	public Sprite getAppearingSprite() {
		return mFlicker.GetSprite();
	}

	public int getHP() {
		return hp;
	}

}