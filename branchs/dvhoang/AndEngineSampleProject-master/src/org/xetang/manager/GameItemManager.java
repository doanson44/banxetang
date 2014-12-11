package org.xetang.manager;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.helper.ChangeWallCallBack;
import org.xetang.map.item.Bomb;
import org.xetang.map.item.Clock;
import org.xetang.map.item.Helmet;
import org.xetang.map.item.Item;
import org.xetang.map.item.Shovel;
import org.xetang.map.item.Star;
import org.xetang.map.item.TankItem;
import org.xetang.map.model.MapObjectBlockDTO;
import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObjectFactory;
import org.xetang.map.object.MapObjectFactory.ObjectLayer;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.tank.Tank;

import android.graphics.Point;
import android.util.Pair;

public class GameItemManager implements IUpdateHandler {

	private static GameItemManager mInstance;
	public ArrayList<Item> mItems = new ArrayList<Item>();
	public ArrayList<Item> mItemRemove = new ArrayList<Item>();

	private GameItemManager() {

	}

	public static GameItemManager getInstance() {
		if (mInstance == null)
			mInstance = new GameItemManager();

		return mInstance;
	}

	public Item CreateItem(ObjectType type) {
		Item item = null;

		switch (type) {
		case BOMB:
			item = new Bomb(GameManager.CurrentMap);
			break;
		case CLOCK:
			item = new Clock(GameManager.CurrentMap);
			break;
		case HELMET:
			item = new Helmet(GameManager.CurrentMap);
			break;
		case SHOVEL:
			item = new Shovel(GameManager.CurrentMap);
			break;
		case STAR:
			item = new Star(GameManager.CurrentMap);
			break;
		case TANK_ITEM:
			item = new TankItem(GameManager.CurrentMap);
			break;
		default:
			break;
		}
		mItems.add(item);
		return item;
	}

	public static void loadResource() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		UpdateItem(pSecondsElapsed);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	private void UpdateItem(float pSecondsElapsed) {
		for (Item item : mItems) {
			if (item.isAlive())
				item.update(pSecondsElapsed);
			else {
				mItemRemove.add(item);
			}
		}
		for (Item item : mItemRemove) {
			mItems.remove(item);
		}
		mItemRemove.clear();
	}

	public void RemoveWall() {
		ChangeWallCallBack callback = new ChangeWallCallBack();

		float pLowerX = CalcHelper
				.pixels2Meters(5 * GameManager.LARGE_CELL_SIZE
						+ GameManager.LARGE_CELL_SIZE / 2);
		float pLowerY = CalcHelper
				.pixels2Meters(11 * GameManager.LARGE_CELL_SIZE
						+ GameManager.LARGE_CELL_SIZE / 2);
		float pUpperX = CalcHelper
				.pixels2Meters(7 * GameManager.LARGE_CELL_SIZE
						+ GameManager.LARGE_CELL_SIZE / 2);
		float pUpperY = CalcHelper
				.pixels2Meters(13 * GameManager.LARGE_CELL_SIZE);

		GameManager.PhysicsWorld.QueryAABB(callback, pLowerX, pLowerY, pUpperX,
				pUpperY);
	}

	public void MakeSteelWallFortress() {
		// TODO Auto-generated method stub
		RemoveWall();
		CreateWall(ObjectType.STEEL_WALL);

	}

	public void RetrieveOldWallFortress() {
		RemoveWall();
		CreateWall(ObjectType.BRICK_WALL);
	}

	public void CreateWall(ObjectType type) {
		if (type == ObjectType.STEEL_WALL) {
			CreateWall(type, 11 * GameManager.LARGE_CELL_SIZE / 2,
					23 * GameManager.LARGE_CELL_SIZE / 2);
			CreateWall(type, 11 * GameManager.LARGE_CELL_SIZE / 2,
					24 * GameManager.LARGE_CELL_SIZE / 2);
			CreateWall(type, 11 * GameManager.LARGE_CELL_SIZE / 2,
					25 * GameManager.LARGE_CELL_SIZE / 2);
			CreateWall(type, 14 * GameManager.LARGE_CELL_SIZE / 2,
					23 * GameManager.LARGE_CELL_SIZE / 2);
			CreateWall(type, 14 * GameManager.LARGE_CELL_SIZE / 2,
					24 * GameManager.LARGE_CELL_SIZE / 2);
			CreateWall(type, 14 * GameManager.LARGE_CELL_SIZE / 2,
					25 * GameManager.LARGE_CELL_SIZE / 2);
			CreateWall(type, 12 * GameManager.LARGE_CELL_SIZE / 2,
					23 * GameManager.LARGE_CELL_SIZE / 2);
			CreateWall(type, 13 * GameManager.LARGE_CELL_SIZE / 2,
					23 * GameManager.LARGE_CELL_SIZE / 2);
		} else {
			MapObjectBlockDTO block1 = MapObjectFactory.createObjectBlock(type,
					new Pair<Point, Point>(new Point(12, 23), new Point(4, 2)));
			MapObjectBlockDTO block2 = MapObjectFactory.createObjectBlock(type,
					new Pair<Point, Point>(new Point(11, 23), new Point(2, 6)));
			MapObjectBlockDTO block3 = MapObjectFactory.createObjectBlock(type,
					new Pair<Point, Point>(new Point(14, 23), new Point(2, 6)));
			CreateWall(block1);
			CreateWall(block2);
			CreateWall(block3);
		}

	}

	public void CreateWall(final ObjectType type, final float px, final float py) {
		Runnable runable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				IMapObject wall = MapObjectFactory.createObject(type, px, py);
				wall.putToWorld();
				GameManager.CurrentMap
						.addObject(wall, ObjectLayer.CONSTRUCTION);
			}
		};
		GameManager.Activity.runOnUpdateThread(runable);
	}

	public void CreateWall(MapObjectBlockDTO block) {
		for (final IMapObject object : block.getObjectsBlock()) {
			Runnable runable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					object.putToWorld();
					GameManager.CurrentMap.addObject(object,
							ObjectLayer.CONSTRUCTION);
				}
			};
			GameManager.Activity.runOnUpdateThread(runable);
		}
	}

	public void DestroyAllEnermy() {
		// TODO Auto-generated method stub
		for (int i = 0; i < GameManager.CurrentMap.getEnemyTanks().size(); i++) {
			GameManager.CurrentMap.getEnemyTanks().get(i).KillSelf();
		}
		GameManager.CurrentMap.getEnemyTanks().clear();
	}

	public void FreezeTime() {
		// TODO Auto-generated method stub
		for (Tank tank : GameManager.CurrentMap.getEnemyTanks()) {
			tank.FreezeSelf();
		}
	}

	public ObjectType GetRandomType() {
		int random = (new Random()).nextInt() % 6;
		ObjectType type = null;

		switch (Math.abs(random)) {
		case 0:
			type = ObjectType.BOMB;
			break;
		case 1:
			type = ObjectType.CLOCK;
			break;
		case 2:
			type = ObjectType.HELMET;
			break;
		case 3:
			type = ObjectType.SHOVEL;
			break;
		case 4:
			type = ObjectType.TANK_ITEM;
			break;
		case 5:
			type = ObjectType.STAR;
			break;
		default:
			break;
		}
		return type;
	}

	public void CreateRandomItem() {
		// TODO Auto-generated method stub
		Runnable run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GameManager.CurrentMap.addObject(CreateItem(GetRandomType()),
						ObjectLayer.WRAPPER);
				// GameManager.CurrentMap.attachChild(
				// CreateItem(GetRandomType()));
			}
		};
		GameManager.Activity.runOnUpdateThread(run);
	}

}
