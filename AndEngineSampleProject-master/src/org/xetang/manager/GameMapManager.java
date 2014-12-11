package org.xetang.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.controller.Bot;
import org.xetang.map.Frame;
import org.xetang.map.Map;
import org.xetang.map.RightMenu;
import org.xetang.map.item.Item;
import org.xetang.map.model.StageDTO;
import org.xetang.map.model.XMLLoader;
import org.xetang.map.object.MapObjectFactory.TankType;
import org.xetang.root.GameScene;
import org.xetang.tank.Tank;

public class GameMapManager implements IUpdateHandler {

	GameScene _gameScene;
	public static Map _map;
	Frame _frame;
	Queue<Tank> _totalEnemyTanks; // tổng số xe tăng địch còn lại
	Queue<Tank> _totalPlayerTanks; // tổng số xe tăng player còn lại (hiện tại
									// chỉ là 1)

	List<Tank> _totalTankKill = new ArrayList<Tank>();
	List<Item> _totalItemGet = new ArrayList<Item>();
	int maxAvaiableEnemyTank = 4;
	int maxAvaiablePlayerTank = 1;
	int Player1Life = 0;
	List<Bot> _bots;

	public GameMapManager(GameScene gameScene, int iCurrentStage) {
		GameManager.CurrentMapManager = this; // Thiết lập toàn cục
		_gameScene = gameScene;

		loadMapData(iCurrentStage);

		// _frame = new Frame(_map.getEnemyTanks().size());
		// _gameScene.attachChild(_frame);

		createAllBots();
	}

	private void createAllBots() {
		_bots = new ArrayList<Bot>(maxAvaiableEnemyTank);

		for (int i = 0; i < maxAvaiableEnemyTank; i++) {
			// _bots.add(new Bot());
		}
	}

	private void loadMapData(int iCurrentStage) {
		StageDTO stage = XMLLoader.getStage(iCurrentStage);
		stage.setLives(maxAvaiablePlayerTank);
		_map = new Map(iCurrentStage, stage);

		GameManager.CurrentMap = _map;
		_gameScene.attachChild(_map);

		loadMapTanks(stage);
		_map.InitRightMenu(10);
	}

	private void loadMapTanks(StageDTO stage) {

		loadPlayerTanks(stage.getLives());
		loadEnemyTanks(stage.getTanksNameQueue());
	}

	private void loadPlayerTanks(int lives) {
		Player1Life = 2;
		_totalPlayerTanks = new LinkedList<Tank>();

	}

	private void loadEnemyTanks(List<String> tanksNameQueue) {

		Tank tank = null;

		_totalEnemyTanks = new LinkedList<Tank>();
		/*
		 * for (int i = 0; i < tanksNameQueue.size(); i++) { int Position = i%3
		 * + 1; if (tanksNameQueue.get(i).equals("Normal")) { tank =
		 * TankManager.CreateEnemytank(TankType.Normal,Position); } else if
		 * (tanksNameQueue.get(i).equals("Racer")) { tank =
		 * TankManager.CreateEnemytank(TankType.Racer,Position); } else if
		 * (tanksNameQueue.get(i).equals("GlassCannon")) { tank =
		 * TankManager.CreateEnemytank(TankType.GlassCannon,Position); } else if
		 * (tanksNameQueue.get(i).equals("BigMom")) { tank =
		 * TankManager.CreateEnemytank(TankType.BigMom,Position); }
		 * 
		 * _totalEnemyTanks.add(tank); }
		 */

		TankManager.createEnemyTank(_totalEnemyTanks, TankType.GLASS_CANNON, 1,
				true);

		TankManager
				.createEnemyTank(_totalEnemyTanks, TankType.BIG_MOM, 2, true);

		TankManager.createEnemyTank(_totalEnemyTanks, TankType.RACER, 3, true);

	}

	@Override
	public void onUpdate(float pSecondsElapsed) {

		_map.Update(pSecondsElapsed);
		updateTanksAndBots(pSecondsElapsed);
		// updateWinLose();
	}

	private void updateTanksAndBots(float pSecondsElapsed) {
		List<Tank> tanks = _map.getEnemyTanks();

		if (tanks.size() < maxAvaiableEnemyTank && _totalEnemyTanks.size() > 0) {
			addEnemyTankToMap();
		}

		if (_totalPlayerTanks.size() == 0 && Player1Life > 0
				&& _map.getPlayerTanks().size() < maxAvaiablePlayerTank) {
			TankManager.CreatePlayerTank(_totalPlayerTanks, 1);
			Player1Life--;
			RightMenu.UpdateText();
		}

		if (_totalPlayerTanks.size() > 0
				&& _map.getPlayerTanks().size() < maxAvaiablePlayerTank) {
			addPlayerTankToMap();
		}
		for (int i = 0; i < tanks.size(); i++) {
			if (!tanks.get(i).isAlive()) {
				tanks.remove(i);
			}
		}

		for (int i = 0; i < _bots.size(); i++) {
			_bots.get(i).Update(pSecondsElapsed);
			if (_bots.get(i).isFree()) {
				_bots.remove(i);
			}
		}
	}

	private void updateWinLose() {

		if (_map.getEnemyTanks().size() <= 0 && _totalEnemyTanks.size() <= 0) {
			/*
			 * Là thắng
			 */

			_gameScene.onWin();
		}

		if (Player1Life <= 0 || !_map.isEagleAlive()) {
			/*
			 * Là thua
			 */

			_gameScene.onLose();
		}
	}

	private void addEnemyTankToMap() {
		Tank tank = _totalEnemyTanks.poll();
		_map.addEnemyTank(tank);

	}

	private void addPlayerTankToMap() {
		// TODO Auto-generated method stub
		Tank tank = _totalPlayerTanks.poll();
		GameControllerManager.setOnController(tank);
		_map.addPlayerTank(tank);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public void AddBot(Bot bot) {
		_bots.add(bot);
	}

	public Tank getPlayerTank() {
		return _totalPlayerTanks.peek();
	}

	public int getTotalPlayerTank() {
		return _totalPlayerTanks.size();
	}

	public void AddItem(Item item) {
		_totalItemGet.add(item);
	}

	public List<Item> GetTotalItem() {
		return _totalItemGet;
	}

	public void AddTankKill(Tank tank) {
		_totalTankKill.add(tank);
	}

	public List<Tank> GetTotalTankKill() {
		return _totalTankKill;
	}

	public void AddNewLifeForTank() {
		Player1Life++;
	}

	public int GetPlayer1Life() {
		return Player1Life;
	}
}
