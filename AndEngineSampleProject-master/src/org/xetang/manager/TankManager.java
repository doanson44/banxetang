package org.xetang.manager;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.MapObjectFactory.TankType;
import org.xetang.tank.BigMom;
import org.xetang.tank.GlassCannon;
import org.xetang.tank.Normal;
import org.xetang.tank.Racer;
import org.xetang.tank.Tank;

public class TankManager implements IUpdateHandler{

	private static TankManager mInstance;
	
	public static Tank Player;

	public static float Player1_PX = 9 * GameManager.LARGE_CELL_WIDTH / 2 + 2;
	public static float Player1_PY = 24 * GameManager.LARGE_CELL_HEIGHT / 2 + 3;

	public static float Player2_PX = 15 * GameManager.LARGE_CELL_WIDTH / 2;
	public static float Player2_PY = 24 * GameManager.LARGE_CELL_HEIGHT / 2 + 3;

	public static float Enermy_PX_1 = 0;
	public static float Enermy_PY_1 = 0;

	public static float Enermy_PX_2 = 6 * GameManager.LARGE_CELL_WIDTH ;;
	public static float Enermy_PY_2 = 0;

	public static float Enermy_PX_3 = 12 * GameManager.LARGE_CELL_WIDTH ;;
	public static float Enermy_PY_3 = 0;

	/**
	 * Tạo 1 xe tăng cho người chơi ở vị trí định sẵn
	 * @param Player : = 1 hoặc 2, để xác định là tạo xe tăng cho người chơi 1 hay người chơi 2
	 * @return xe tăng
	 */
	public static Tank CreatePlayerTank(int Player) {
		Tank tank = null;

		if (Player == 1) {
			tank = new Normal(Player1_PX, Player1_PY);
		} else
			tank = new Normal(Player2_PX, Player2_PY);
		
		tank.SetType(ObjectType.PlayerTank);
		return tank;
	}

	/**
	 * Tạo 1 xe tăng địch ở vị trí định sẵn
	 * @param type : loại xe tăng địch muốn tạo etc: BigMom, Racer...
	 * @param Position : bằng 1, 2 hay 3, là vị trí xe tăng địch xuất hiện ở góc trên cùng của màn hình
	 * @return xe tăng
	 */
	public static Tank CreateEnermytank(TankType type, int Position) {
		Tank tank = null;
		float px = 0, py=0;
		
		switch (Position) {
		case 1:
			px = Enermy_PX_1;
			py = Enermy_PY_1;
			break;
		case 2:
			px = Enermy_PX_2;
			py = Enermy_PY_2;
			break;
		case 3:
			px = Enermy_PX_3;
			py = Enermy_PY_3;
			break;
		default:
			break;
		}
		
		switch (type) {
		case BigMom:
			tank = new BigMom(px, py);
			break;
		case GlassCannon:
			tank = new GlassCannon(px, py);
			break;
		case Racer:
			tank = new Racer(px, py);
			break;
		case Normal:
			tank = new Normal(px, py);
			break;
		default:
			break;
		}
		
		tank.SetType(ObjectType.EnermyTank);
		return tank;
	}

	public static TankManager getInstance() {
		if(mInstance==null)
			mInstance = new TankManager();
		return mInstance;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public static void register(Tank tank) {
		// TODO Auto-generated method stub
		
	}

	public static void loadOldData() {
		// TODO Auto-generated method stub
		
	}

	public static void createPlayerTank() {
		// TODO Auto-generated method stub
		
	}

	public static void generateOpponentTank(int i) {
		// TODO Auto-generated method stub
		
	}

}
