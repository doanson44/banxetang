package org.xetang.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.controller.Bot;
import org.xetang.map.IBlowUp;
import org.xetang.map.IBullet;
import org.xetang.map.Map;
import org.xetang.map.model.StageDTO;
import org.xetang.map.model.XMLLoader;
import org.xetang.root.Frame;
import org.xetang.root.GameScene;
import org.xetang.tank.BigMom;
import org.xetang.tank.GlassCannon;
import org.xetang.tank.Normal;
import org.xetang.tank.Racer;
import org.xetang.tank.Tank;

public class GameMapManager implements IUpdateHandler {

	GameScene _gameScene;
	Map _map;
	Frame _frame;
	Queue<Tank> _totalEnermyTanks; // tổng số xe tăng địch còn lại
	Queue<Tank> _totalPlayerTanks; // tổng số xe tăng player còn lại (hiện tại
									// chỉ là 1)

	int maxAvaiableEnermyTank = 4;
	int maxAvaiablePlayerTank = 1;

	List<Bot> _bots;

	public GameMapManager(GameScene gameScene, int iCurrentStage) {
		GameManager.CurrentMapManager = this; // Thiết lập toàn cục
		_gameScene = gameScene;

		loadMapData(iCurrentStage);

		_frame = new Frame(iCurrentStage);
		_gameScene.attachChild(_frame);

		createAllBots();
	}

	private void createAllBots() {
		_bots = new ArrayList<Bot>(maxAvaiableEnermyTank);

		for (int i = 0; i < maxAvaiableEnermyTank; i++) {
			_bots.add(new Bot());
		}
	}

	private void loadMapData(int iCurrentStage) {
		StageDTO stage = XMLLoader.getStage(iCurrentStage);
		stage.setLives(maxAvaiablePlayerTank);
		_map = new Map(iCurrentStage, stage);
		
		_gameScene.attachChild(_map);

		loadMapTanks(stage);
	}

	private void loadMapTanks(StageDTO stage) {

		loadPlayerTanks(stage.getLives());
	//	loadEnermyTanks(stage.getTanksNameQueue());

	}

	private void loadPlayerTanks(int lives) {

		_totalPlayerTanks = new LinkedList<Tank>();
		for (int i = 0; i < lives; i++) {
			Tank tank = new Normal(0, 0, _map);
			_totalPlayerTanks.add(tank);
			_map.addPlayerTank(tank);
			_map.attachChild(tank.GetCurrentSprite());
		}

	}

	private void loadEnermyTanks(List<String> tanksNameQueue) {

		Tank tank = null;

		_totalEnermyTanks = new LinkedList<Tank>();
		for (int i = 0; i < tanksNameQueue.size(); i++) {
			if (tanksNameQueue.get(i).equals("Normal")) {
				tank = new Normal(0, 0, _map);
			} else if (tanksNameQueue.get(i).equals("Racer")) {
				tank = new Racer(0, 0, _map);
			} else if (tanksNameQueue.get(i).equals("GlassCannon")) {
				tank = new GlassCannon(0, 0, _map);
			} else if (tanksNameQueue.get(i).equals("BigMom")) {
				tank = new BigMom(0, 0, _map);
			}

			_totalEnermyTanks.add(tank);
		}
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		_frame.update(_map.getTotalEnermyTanks().size());

		_map.Update(pSecondsElapsed);
	//	 updateTanksAndBots();
	//	 updateWinLose();
	}

	private void updateTanksAndBots() {
		List<Tank> tanks = _map.getTotalEnermyTanks();

		if (tanks.size() < maxAvaiableEnermyTank
				&& _totalEnermyTanks.size() > 0) {
			addEnermyTankToMap();
		}

		for (int i = 0; i < tanks.size(); i++) {
			if (!tanks.get(i).isAlive()) {
				tanks.remove(i);
			}
		}

		for (int i = 0; i < _bots.size(); i++) {
			if (_bots.get(i).isFree()) {
				_bots.remove(i);
			}
		}
	}

	private void updateWinLose() {

		if (_map.getTotalEnermyTanks().size() <= 0
				&& _totalEnermyTanks.size() <= 0) {
			/*
			 * Là thắng
			 */

			_gameScene.onWin();
		}

		if (_map.getTotalPlayerTanks() <= 0 || !_map.isEagleAlive()) {
			/*
			 * Là thua
			 */

			_gameScene.onLose();
		}
	}

	private void addEnermyTankToMap() {
		Tank tank = _totalEnermyTanks.poll();
		Bot bot = new Bot(tank);

		_map.addEnermyTank(tank);
		_bots.add(bot);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public Tank getPlayerTank() {
		return _totalPlayerTanks.peek();
	}

	public void addBullet(IBullet bullet) {
		_map.addBullet(bullet);
	}

	public void addBlast(IBlowUp blast) {
		_map.addBlast(blast);
	}
}
