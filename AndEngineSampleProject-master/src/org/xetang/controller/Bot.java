package org.xetang.controller;

import java.util.Random;

import org.xetang.manager.GameManager.Direction;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.tank.Tank;

import com.badlogic.gdx.math.Vector2;

public class Bot extends Controller {

	protected Tank mTank;
	protected float FortressPX = 6;
	protected float FortressPY = 12;
	protected Direction mDirection;
	protected ObjectType Collide;

	Random rd;
	
	long preTime = 0;
	Vector2 prePos = new Vector2();
	Vector2 curPos;
	public Bot(Tank tank) {
		mTank = tank;
		tank.setController(this);
		mDirection = Direction.DOWN;
		rd = new Random(System.currentTimeMillis());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.andengine.engine.handler.IUpdateHandler#onUpdate(float)
	 * 
	 * ĂN ĐI KU Thiết lập trí thông minh AI điều khiển xe tăng Thông tin dựa vào
	 * "GameManager.CurrentMapManager" để thực hiện
	 */
	public void Update(float pSecondsElapsed) {
		randomFire();
		curPos = new Vector2(mTank.getX(), mTank.getY());
		//Debug.d(GameManager.TANK_TAG, String.valueOf(curPos.dst(prePos)));
		if (System.currentTimeMillis() - preTime > 1000 && curPos.dst(prePos) < 0.05f){
			preTime = System.currentTimeMillis();
			TurnAround();
		}
		prePos = curPos;
		Move();
	}

	private void randomFire() {
		if (rd.nextFloat() < 0.005f)
			mTank.onFire();
	}


	private void Move() {
		switch (mDirection) {
		case UP:
			mTank.onForward();
			break;
		case DOWN:
			mTank.onBackward();
			break;
		case LEFT:
			mTank.onLeft();
			break;
		case RIGHT:
			mTank.onRight();
			break;
		default:
			TurnAround();
			break;
		}

	}

	private synchronized void TurnAround() {
		int random = 0;
		while (random == mDirection.ordinal())
			random = rd.nextInt(4);
		
		mDirection = Direction.values()[random];
	}



	@Override
	public void onTankDie() {
		super.onTankDie();

		mTank = null;
	}
	
	long preCollideTime = 0;
	@Override
	public synchronized void onCollide(ObjectType type) {
		if (System.currentTimeMillis() - preCollideTime < 300)
		{
			preCollideTime = System.currentTimeMillis();
			return;
		}
		if (type == null || 
				type == ObjectType.BRICK_WALL || 
				type == ObjectType.STEEL_WALL || 
				type == ObjectType.ENEMY_TANK ||
				type == ObjectType.PLAYER_TANK ) {
			TurnAround();
			//Debug.d(GameManager.TANK_TAG, String.format("%s -> %s", String.valueOf(type), String.valueOf(mDirection)) );
		}
	}


	public boolean isYourTank(Tank tank) {
		return tank == mTank;
	}

	public boolean isFree() {
		return !mTank.isAlive();
	}
}
