package org.xetang.controller;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.tank.Tank;

public class Bot extends Controller implements IUpdateHandler {
	
	protected IGameController _tank;
	
	public Bot() {
		
	}
	
	public Bot(IGameController tank) {
		_tank = tank;
	}

	/*
	 * (non-Javadoc)
	 * @see org.andengine.engine.handler.IUpdateHandler#onUpdate(float)
	 * 
	 * ĂN ĐI KU
	 * Thiết lập trí thông minh AI điều khiển xe tăng
	 * Thông tin dựa vào "GameManager.CurrentMapManager" để thực hiện
	 */
	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
		/*
		 * Chọn hướng đi, hành động
		 */
		
		// Thực hiện
		//VD : _tank.OnFire();
	}
	
	@Override
	public void onTankDie() {
		super.onTankDie();

		_tank = null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public boolean isYourTank(Tank tank) {
		return tank == _tank;
	}
	
	public boolean isFree() {
		return _tank == null;
	}
}
