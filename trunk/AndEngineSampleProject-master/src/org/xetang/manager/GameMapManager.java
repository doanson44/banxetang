package org.xetang.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.controller.Bot;
import org.xetang.map.Map;
import org.xetang.map.MapObject;
import org.xetang.root.Frame;
import org.xetang.root.GameScene;
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
		_map = new Map(iCurrentStage);
		_frame = new Frame(iCurrentStage);

		createAllBots();

		loadMapData(iCurrentStage);
		initMapData();
	}

	private void createAllBots() {
		_bots = new ArrayList<Bot>(maxAvaiableEnermyTank);

		for (int i = 0; i < maxAvaiableEnermyTank; i++) {
			_bots.add(new Bot());
		}
	}

	/*
	 * Đọc dữ liệu cần thiết cho các biến trong MapManager
	 */
	private void loadMapData(int iCurrentStage) {
		// TODO Auto-generated method stub

	}

	/*
	 * Khởi tạo các biến cục bộ
	 * Danh sách xe tăng:
	 * 		_totalEnermyTanks
	 * 		_totalPlayerTanks
	 * Số lượng xe tăng tối đa trên bản đồ:
	 * 		maxAvaiableEnermyTank
	 * 		maxAvaiablePlayerTank
	 * Và các biến khác
	 */
	private void initMapData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		_frame.update(_map.getTotalEnermyTanks().size());

		updateCollision(pSecondsElapsed);
		updateTanksAndBots();
		updateWinLose();
	}

	/*
	 * ĂN ĐI KU
	 */
	private void updateCollision(float pSecondsElapsed) {
		MapObject[][] matrix = _map.getMapMatrix();
		int height = matrix.length;
		int width = matrix[0].length;
		int allCells = height * width;
		
		for (int i=0; i<allCells-1; i++) {
			MapObject l = matrix[i/width][i%width];
			
			for (int j=i+1; j<allCells; j++) {
				MapObject r = matrix[j/width][j%width];
				if (!l.isStatic() || !r.isStatic()) {
					/*
					 * Xét va chạm giữa 2 vật thể
					 * Xử lý theo kịch bản game
					 */
				}
			}
		}
		
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

}
//
// private static GameMapManager mInstance;
//
// public static Map CurrentMap;
// private static Dictionary<String, TiledTextureRegion> mResources;
//
//
// static{
// mResources = new Hashtable<String, TiledTextureRegion>();
// }
//
// private GameMapManager(){
//
// }
//
// public static GameMapManager getInstance(){
// if(mInstance==null)
// mInstance = new GameMapManager();
// return mInstance;
// }
//
// public static void loadResource() {
// // Tải các tài nguyên liên quan đến bản đồ, vd: gạch, đại bàng, nước, ...
// // Tham khảo cách load tài nguyên ở class GameControllerManager
//
// }
//
//
// public static void loadMapData(int mStage) {
// //Tải màn chơi mà người dùng đang chơi dở dang
// GameMapManager.CurrentMap = new Map();
// GameMapManager.CurrentMap.loadMapData(mStage);
// }
//
//
// }
