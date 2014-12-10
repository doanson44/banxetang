package org.xetang.controller;

import java.util.List;
import java.util.Random;

import org.xetang.manager.GameManager.Direction;
import org.xetang.manager.GameMapManager;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.tank.Tank;

import com.badlogic.gdx.math.Vector2;

public class Bot extends Controller {

	protected Tank mTank;
	protected float FortressPX = 6;
	protected float FortressPY = 12;
	protected Direction mDirection;
	protected ObjectType Collide;
	private float _SecPerFrame = 0;
	private float _TimeToFire = 0;

	public Bot(Tank tank) {
		mTank = tank;
		mDirection = Direction.DOWN;
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
		MoveToFortress(pSecondsElapsed);
		CountTimeToFire(pSecondsElapsed);
	}

	private void MoveToFortress(float pSecondsElapsed) {
		Vector2 CurrentCell = CalcHelper.CellInMap13(mTank.getSprite());
		float distinctX = FortressPX - CurrentCell.x;
		float distinctY = FortressPY - CurrentCell.y;

		List<Tank> playerTanks = GameMapManager._map.getPlayerTanks();
		if (mTank.GetCollide() == null) {
			if (distinctY > 0)
				mDirection = Direction.DOWN;
			else if (distinctX > 0)
				mDirection = Direction.RIGHT;
			else if (distinctX < 0)
				mDirection = Direction.LEFT;

			if (distinctX == 0) {
				if ((new Random()).nextInt() % 2 == 0)
					mDirection = Direction.LEFT;
				else
					mDirection = Direction.RIGHT;
			}

			if (_TimeToFire % 4 == 0)
				mTank.onFire();

			if (CurrentCell.x == FortressPX || CurrentCell.y == FortressPY) {
				SetDirectionToFire(CurrentCell, new Vector2(FortressPX,
						FortressPY));
			}

			for (int i = 0; i < playerTanks.size(); i++) {
				Vector2 playerPoint = CalcHelper.CellInMap13(playerTanks.get(i)
						.getSprite());
				SetDirectionToFire(CurrentCell, playerPoint);
			}

		} else {
			TurnAround();
		}
		Move();
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
			break;
		}

	}

	private void TurnAround() {
		Random rd = new Random();
		int random = rd.nextInt() % 4;
		if (random == 0)
			mDirection = Direction.DOWN;
		if (random == 1)
			mDirection = Direction.UP;
		if (random == 2)
			mDirection = Direction.LEFT;
		if (random == 3)
			mDirection = Direction.RIGHT;
	}

	private void SetDirectionToFire(Vector2 CurrentCell, Vector2 playerPoint) {
		if (CurrentCell.x == playerPoint.x) {
			if (CurrentCell.y - playerPoint.y > 0)
				mDirection = Direction.UP;
			else
				mDirection = Direction.DOWN;

			mTank.SetDirection(mDirection);
			mTank.onFire();
		}
		if (CurrentCell.y == playerPoint.y) {
			if (CurrentCell.x - playerPoint.x > 0)
				mDirection = Direction.LEFT;
			else
				mDirection = Direction.RIGHT;

			mTank.SetDirection(mDirection);
			mTank.onFire();
		}
	}

	@Override
	public void onTankDie() {
		super.onTankDie();

		mTank = null;
	}

	private void CountTimeToFire(float pSecondsElapsed) {
		_SecPerFrame += pSecondsElapsed;
		if (_SecPerFrame > 1) {
			_SecPerFrame = 0;
			_TimeToFire++;
		}
	}

	public boolean isYourTank(Tank tank) {
		return tank == mTank;
	}

	public boolean isFree() {
		return !mTank.isAlive();
	}
}
