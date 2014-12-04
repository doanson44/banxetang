package org.xetang.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.manager.GameManager.Direction;
import org.xetang.manager.GameMapManager;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.tank.Tank;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class Bot extends Controller {

	protected Tank mTank;
	protected float FortressPX = 6;
	protected float FortressPY = 12;
	protected Direction mDirection;
	protected ObjectType Collide;

	public Bot() {

	}

	public Bot(Tank tank) {
		mTank = tank;
		mDirection = Direction.Down;
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
		MoveToFortress();
	}

	private void MoveToFortress() {
		Vector2 CurrentCell = mTank.CellInMap13();
		float distinctX = FortressPX - CurrentCell.x;
		float distinctY = FortressPY - CurrentCell.y;

		List<Tank> playerTanks = GameMapManager._map.getPlayerTanks();
		if (mTank.GetCollide() == null) {
			if (distinctY > 0)
				mDirection = Direction.Down;
			else if (distinctX > 0)
				mDirection = Direction.Right;
			else if (distinctX < 0)
				mDirection = Direction.Left;

			if (distinctX == 0) {
				if ((new Random()).nextInt() % 2 == 0)
					mDirection = Direction.Left;
				else
					mDirection = Direction.Right;
			}

			if (CurrentCell.x == FortressPX || CurrentCell.y == FortressPY){
				SetDirectionToFire(CurrentCell, new Vector2(FortressPX,FortressPY));
			}
			
			for (int i = 0; i < playerTanks.size(); i++) {
				Vector2 playerPoint = playerTanks.get(i).CellInMap13();
				SetDirectionToFire(CurrentCell, playerPoint);
			}

		} else {
			TurnAround();
		}
		Move();
	}

	private void Move() {
		switch (mDirection) {
		case Up:
			mTank.onForward();
			break;
		case Down:
			mTank.onBackward();
			break;
		case Left:
			mTank.onLeft();
			break;
		case Right:
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
			mDirection = Direction.Down;
		if (random == 1)
			mDirection = Direction.Up;
		if (random == 2)
			mDirection = Direction.Left;
		if (random == 3)
			mDirection = Direction.Right;
	}

	private void SetDirectionToFire(Vector2 CurrentCell, Vector2 playerPoint ){
		if (CurrentCell.x == playerPoint.x) {
			if(CurrentCell.y - playerPoint.y > 0)
				mDirection = Direction.Up;
			else
				mDirection = Direction.Down;
			
			mTank.SetDirection(mDirection);
			mTank.onFire();
		}
		if (CurrentCell.y == playerPoint.y) {
			if(CurrentCell.x - playerPoint.x > 0)
				mDirection = Direction.Left;
			else
				mDirection = Direction.Right;
			
			mTank.SetDirection(mDirection);
			mTank.onFire();
		}
	}
	@Override
	public void onTankDie() {
		super.onTankDie();

		mTank = null;
	}

	public boolean isYourTank(Tank tank) {
		return tank == mTank;
	}

	public boolean isFree() {
		return !mTank.isAlive();
	}
}
