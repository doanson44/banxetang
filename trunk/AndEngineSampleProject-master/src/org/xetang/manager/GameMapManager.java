package org.xetang.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.util.debug.Debug;
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
	
	RightMenu _RightMenu;


	public GameMapManager(GameScene gameScene, int iCurrentStage) {
		GameManager.CurrentMapManager = this; // Thiết lập toàn cục
		_gameScene = gameScene;

		loadMapData(iCurrentStage);

		InitRightMenu(_totalEnemyTanks.size());
		// _frame = new Frame(_map.getEnemyTanks().size());
		// _gameScene.attachChild(_frame);

		createAllBots();
	}
	
	public void InitRightMenu(int TotalEnemytank) {
		_RightMenu = new RightMenu(GameManager.MAP_SIZE + 20, 20, _map, TotalEnemytank);

		_map.attachChild(_RightMenu);
	}
	
	private void createAllBots() {
		_bots = new ArrayList<Bot>(maxAvaiableEnemyTank);

		for (int i = 0; i < maxAvaiableEnemyTank; i++) {
			// _bots.add(new Bot());
		}
	}

	private void loadMapData(int iCurrentStage) {
		StageDTO stage = XMLLoader.getStage(iCurrentStage);
		
		_map = new Map(iCurrentStage, stage);

		GameManager.CurrentMap = _map;
		_gameScene.attachChild(_map);

		loadMapTanks(stage);
	}

	private void loadMapTanks(StageDTO stage) {

		loadPlayerTanks(GameManager.getCurrentTankLifes());
		loadEnemyTanks(stage.getTanksNameQueue());
	}

	private void loadPlayerTanks(int lives) {
		_totalPlayerTanks = new LinkedList<Tank>();
		for (int i = 0; i < lives; i++) {
			Tank tank = TankManager.CreatePlayerTank(1);
			
			_totalPlayerTanks.add(tank);
		}
		Tank t = _totalPlayerTanks.poll();
		t.setLevel(GameManager.getCurrentTankLevel());
		t.work();
		_gameScene.setController(t);
		_map.addPlayerTank(t);

	}

	private void loadEnemyTanks(List<String> tanksNameQueue) {

		Tank tank = null;
		Random rd = new Random();

		_totalEnemyTanks = new LinkedList<Tank>();
		
		for (int i = 0; i < tanksNameQueue.size(); i++) {
			int Position = i%3 + 1;
			if (tanksNameQueue.get(i).equals("Normal")) {
				tank = TankManager.createEnemyTank(TankType.NORMAL, Position, rd.nextInt(5)==0); /* rd.nextInt(5)==0*/
			} 
			else if (tanksNameQueue.get(i).equals("Racer")) {
				tank = TankManager.createEnemyTank(TankType.RACER, Position, rd.nextInt(5)==0);
			} else if (tanksNameQueue.get(i).equals("GlassCannon")) {
				tank = TankManager.createEnemyTank(TankType.GLASS_CANNON, Position, rd.nextInt(5)==0);
			} else if (tanksNameQueue.get(i).equals("BigMom")) {
				tank = TankManager.createEnemyTank(TankType.BIG_MOM, Position, rd.nextInt(5)==0);
			}
			
			_totalEnemyTanks.add(tank);
			
		}
		
		/* dumy */
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


	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		_map.Update(pSecondsElapsed);
		
		 updateTanksAndBots(pSecondsElapsed);
		 updateWinLose();
	}

	private void updateTanksAndBots(float pSecondsElapsed) {
		List<Tank> tanks = _map.getEnemyTanks();

		if (tanks.size() < maxAvaiableEnemyTank && _totalEnemyTanks.size() > 0) {
			addEnemyTankToMap();
		}



		if (_totalPlayerTanks.size() > 0
				&& _map.getPlayerTanks().size() < maxAvaiablePlayerTank) {
			addPlayerTankToMap();
		}
		/* map da kiem tra roi
		for (int i = 0; i < tanks.size(); i++) {
			if (!tanks.get(i).isAlive()) {
				tanks.remove(i);
			}
		}
		}*/

		for (int i = 0; i < _bots.size(); i++) {
			_bots.get(i).Update(pSecondsElapsed);
			if (_bots.get(i).isFree()) {
				_bots.remove(i);
			}
		}
		
		if (_totalPlayerTanks.size() > 0 && _map.getPlayerTanks().size()==0 )
		{
			addPlayerTankToMap();
		}
	}

	private void updateWinLose() {
		if (_map.getEnemyTanks().size() == 0 && _totalEnemyTanks.size() == 0){
			/*
			 * Là thắng
			 */

			_gameScene.onWin();
		}

		if (_map.getTotalPlayerTanks() <= 0 || !_map.isEagleAlive()) {			
			/**
		
			 * Là thua
			 */

			_gameScene.onLose();
		}
	}

	private void addEnemyTankToMap() {
		Tank tank = _totalEnemyTanks.poll();
		Bot bot = new Bot(tank);
		_bots.add(bot);
		tank.work();
		_map.addEnemyTank(tank);

	}

	private void addPlayerTankToMap() {
		Tank t = _totalPlayerTanks.poll();
		t.work();
		_gameScene.setController(t);
		_map.addPlayerTank(t);
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public void AddBot(Bot bot) {
		_bots.add(bot);
	}

	public Tank getPlayerTank() {
		return _map.getPlayerTanks().get(0);
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
		_totalPlayerTanks.add(TankManager.CreatePlayerTank(1));
		GameManager.addNewLifeForTank();
	}

	public int GetPlayer1Life() {
		return _totalPlayerTanks.size();
	}
	
	public void clear() {
		GameManager.Context.runOnUpdateThread(new Runnable() {			
			@Override
			public void run() {
				_map.detachSelf();
				_frame.detachSelf();
				_totalEnemyTanks.clear();
				_totalPlayerTanks.clear();
				GameManager.PhysicsWorld.clearForces();
				GameManager.PhysicsWorld.clearPhysicsConnectors();
			}
		});
		
	}

	public void clearMapData() {
		GameManager.CurrentMap = null;
		
		GameManager.Context.runOnUpdateThread(new Runnable() {	
			@Override
			public void run() {
				_gameScene.detachChild(_map);
				Debug.d(GameManager.TANK_TAG, "detach map");
			}
		});
		
	}

	public int  getTotalEnemyTanks() {
		return _totalEnemyTanks.size();
	}

	
}
