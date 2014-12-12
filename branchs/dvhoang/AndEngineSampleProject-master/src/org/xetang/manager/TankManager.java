package org.xetang.manager;

import java.util.Queue;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.map.object.MapObjectFactory.TankType;
import org.xetang.tank.BigMom;
import org.xetang.tank.GlassCannon;
import org.xetang.tank.Normal;
import org.xetang.tank.Player1;
import org.xetang.tank.Player2;
import org.xetang.tank.Racer;
import org.xetang.tank.Tank;

public class TankManager implements IUpdateHandler {

	private static TankManager mInstance;

	public static Tank Player;

	public static final float Player1_PX = 9 * GameManager.LARGE_CELL_SIZE / 2 + 2;
	public static final float Player1_PY = 24 * GameManager.LARGE_CELL_SIZE / 2 + 3;

	public static final float Player2_PX = 15 * GameManager.LARGE_CELL_SIZE / 2;
	public static final float Player2_PY = 24 * GameManager.LARGE_CELL_SIZE / 2 + 3;

	public static final float Enermy_PX_1 = 0;
	public static final float Enermy_PY_1 = 0;

	public static final float Enermy_PX_2 = 6 * GameManager.LARGE_CELL_SIZE;;
	public static final float Enermy_PY_2 = 0;

	public static final float Enermy_PX_3 = 12 * GameManager.LARGE_CELL_SIZE;;
	public static final float Enermy_PY_3 = 0;

	public static final float NORMAL_TANK_TIME = 5f;
	public static final float NORMAL_TANK_SPEED = CalcHelper
			.pixels2Meters(GameManager.MAP_SIZE / NORMAL_TANK_TIME);
	public static final float SLOW_TANK_SPEED = NORMAL_TANK_SPEED * 2f / 3f;
	public static final float FAST_TANK_SPEED = NORMAL_TANK_SPEED * 3f / 2f;

	/**
	 * Tạo 1 xe tăng cho người chơi ở vị trí định sẵn
	 * 
	 * @param Player
	 *            : = 1 hoặc 2, để xác định là tạo xe tăng cho người chơi 1 hay
	 *            người chơi 2
	 * @return xe tăng
	 */
	public static void CreatePlayerTank(final Queue<Tank> mPlayerTanks,
			final int Player) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Tank tank = null;

				if (Player == 1) {
					tank = new Player1(Player1_PX, Player1_PY);
				} else
					tank = new Player2(Player2_PX, Player2_PY);

				tank.SetType(ObjectType.PLAYER_TANK);
				mPlayerTanks.add(tank);
			}
		};

		GameManager.Activity.runOnUpdateThread(runnable);

	}

	/**
	 * Tạo 1 xe tăng địch ở vị trí định sẵn
	 * 
	 * @param type
	 *            : loại xe tăng địch muốn tạo etc: BigMom, Racer...
	 * @param Position
	 *            : bằng 1, 2 hay 3, là vị trí xe tăng địch xuất hiện ở góc trên
	 *            cùng của màn hình
	 * @param isTankBonus
	 *            : true or false, để set xe tăng nhấp nháy, khi giết được sẽ
	 *            rớt item
	 * @return xe tăng
	 */
	public static void createEnemyTank(final Queue<Tank> mEnermyTanks,
			final TankType type, final int Position, final boolean isTankBonus) {

		Runnable run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Tank tank = null;
				float px = 0, py = 0;

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
				case BIG_MOM:
					tank = new BigMom(px, py);
					break;
				case GLASS_CANNON:
					tank = new GlassCannon(px, py);
					break;
				case RACER:
					tank = new Racer(px, py);
					break;
				case NORMAL:
					tank = new Normal(px, py);
					break;
				default:
					break;
				}
				tank.SetTankBonus(isTankBonus);
				tank.SetType(ObjectType.ENEMY_TANK);
				mEnermyTanks.add(tank);
			}
		};
		GameManager.Activity.runOnUpdateThread(run);
	}

	public static TankManager getInstance() {
		if (mInstance == null)
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

	public static void generateOpponentTank(int i) {
		// TODO Auto-generated method stub

	}

}
