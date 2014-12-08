package org.xetang.manager;

import java.util.Queue;

import org.andengine.engine.handler.IUpdateHandler;
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

	public static float Player1_PX = 9 * GameManager.LARGE_CELL_WIDTH / 2 + 2;
	public static float Player1_PY = 24 * GameManager.LARGE_CELL_HEIGHT / 2 + 3;

	public static float Player2_PX = 15 * GameManager.LARGE_CELL_WIDTH / 2;
	public static float Player2_PY = 24 * GameManager.LARGE_CELL_HEIGHT / 2 + 3;

	public static float Enermy_PX_1 = 0;
	public static float Enermy_PY_1 = 0;

	public static float Enermy_PX_2 = 6 * GameManager.LARGE_CELL_WIDTH;;
	public static float Enermy_PY_2 = 0;

	public static float Enermy_PX_3 = 12 * GameManager.LARGE_CELL_WIDTH;;
	public static float Enermy_PY_3 = 0;

	/**
	 * Táº¡o 1 xe tÄƒng cho ngÆ°á»�i chÆ¡i á»Ÿ vá»‹ trÃ­ Ä‘á»‹nh sáºµn
	 * 
	 * @param Player
	 *            : = 1 hoáº·c 2, Ä‘á»ƒ xÃ¡c Ä‘á»‹nh lÃ  táº¡o xe tÄƒng cho ngÆ°á»�i chÆ¡i 1 hay
	 *            ngÆ°á»�i chÆ¡i 2
	 * @return xe tÄƒng
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

				tank.SetType(ObjectType.PlayerTank);
				mPlayerTanks.add(tank);
			}
		};

		GameManager.Activity.runOnUpdateThread(runnable);

	}

	/**
	 * Táº¡o 1 xe tÄƒng Ä‘á»‹ch á»Ÿ vá»‹ trÃ­ Ä‘á»‹nh sáºµn
	 * 
	 * @param type
	 *            : loáº¡i xe tÄƒng Ä‘á»‹ch muá»‘n táº¡o etc: BigMom, Racer...
	 * @param Position
	 *            : báº±ng 1, 2 hay 3, lÃ  vá»‹ trÃ­ xe tÄƒng Ä‘á»‹ch xuáº¥t hiá»‡n á»Ÿ gÃ³c trÃªn
	 *            cÃ¹ng cá»§a mÃ n hÃ¬nh
	 * @param isTankBonus
	 *            : true or false, Ä‘á»ƒ set xe tÄƒng nháº¥p nhÃ¡y, khi giáº¿t Ä‘Æ°á»£c sáº½
	 *            rá»›t item
	 * @return xe tÄƒng
	 */
	public static void CreateEnermytank(final Queue<Tank> mEnermyTanks,
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
				tank.SetTankBonus(isTankBonus);
				tank.SetType(ObjectType.EnermyTank);
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
