package org.xetang.manager;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.xetang.map.Bomb;
import org.xetang.map.Clock;
import org.xetang.map.Helmet;
import org.xetang.map.IMapObject;
import org.xetang.map.Item;
import org.xetang.map.Map;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.helper.ChangeWallCallBack;
import org.xetang.map.Shovel;
import org.xetang.map.Star;
import org.xetang.map.TankItem;
import org.xetang.tank.Tank;

import android.telephony.CellLocation;

public class GameItemManager implements IUpdateHandler {

	private static GameItemManager mInstance;
	public ArrayList<Item> mItems = new ArrayList<Item>();
	public ArrayList<Item> mItemRemove = new ArrayList<Item>();
	
	private GameItemManager(){
		
	}
	
	public static GameItemManager getInstance(){
		if(mInstance==null)
			mInstance = new GameItemManager();
		
		return mInstance;
	}
	
	
	public Item CreateItem( ObjectType type){
		Item item = null;
		
		switch (type) {
		case Bomb:
			item = new Bomb(GameManager.CurrentMap);
			break;
		case Clock:
			item = new Clock(GameManager.CurrentMap);
			break;
		case Helmet:
			item = new Helmet(GameManager.CurrentMap);
			break;
		case Shovel:
			item = new Shovel(GameManager.CurrentMap);
			break;
		case Star:
			item = new Star(GameManager.CurrentMap);
			break;
		case TankItem:
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
	
	public void RemoveWall(){
		ChangeWallCallBack callback = new ChangeWallCallBack();
		float HeightCell = GameManager.LARGE_CELL_HEIGHT;
		float WidthCell = GameManager.LARGE_CELL_WIDTH;
		
		float pLowerX = CalcHelper.pixels2Meters(5 * WidthCell + WidthCell/2) ;
		float pLowerY = CalcHelper.pixels2Meters(11 * HeightCell + HeightCell/2) ;
		float pUpperX = CalcHelper.pixels2Meters(7 * WidthCell + WidthCell/2) ;
		float pUpperY = CalcHelper.pixels2Meters(13 * HeightCell) ;
		
		GameManager.PhysicsWorld.QueryAABB(callback, pLowerX, pLowerY, pUpperX, pUpperY);
	}
	public void MakeSteelWallFortress() {
		// TODO Auto-generated method stub
		RemoveWall();
		CreateWall(ObjectType.SteelWall);
		
	}

	public void RetrieveOldWallFortress(){
		RemoveWall();
		CreateWall(ObjectType.BrickWall);
	}
	public void CreateWall(ObjectType type){
		float HeightCell = GameManager.LARGE_CELL_HEIGHT;
	//	CreateWall(type, 11 *HeightCell/2 , 23 * HeightCell/2);
	//	CreateWall(type, 11 *HeightCell/2 , 24 * HeightCell/2);
	//	CreateWall(type, 11 *HeightCell/2 , 25 * HeightCell/2);
	//	CreateWall(type, 14 *HeightCell/2 , 23 * HeightCell/2);
	//	CreateWall(type, 14 *HeightCell/2 , 24 * HeightCell/2);
	//	CreateWall(type, 14 *HeightCell/2 , 25 * HeightCell/2);
	//	CreateWall(type, 12 *HeightCell/2 , 23 * HeightCell/2);
	//	CreateWall(type, 13 *HeightCell/2 , 23 * HeightCell/2);
	}
	
	public void CreateWall(ObjectType type, float px, float py){
		IMapObject wall = MapObjectFactory.createObject(type, 0, 0);
		wall.putToWorld();
		GameManager.CurrentMap.attachChild((IEntity) wall);
	}
	public void DestroyAllEnermy() {
		// TODO Auto-generated method stub
		for (Tank tank : GameManager.CurrentMap.getPlayerTanks()) {
			tank.killSelf();
		}
		GameManager.CurrentMap.getPlayerTanks().clear();
	}

	public void FreezeTime() {
		// TODO Auto-generated method stub
		for (Tank tank : GameManager.CurrentMap.getEnermyTanks()) {
			tank.FreezeSelf();
		}
	}


}
