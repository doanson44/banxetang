package org.xetang.manager;

import java.util.Dictionary;
import java.util.Hashtable;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.map.Map;

public class GameMapManager   {

	private static GameMapManager mInstance;
	
	public static Map CurrentMap;
	private static Dictionary<String, TiledTextureRegion> mResources;
	
	
	static{
		mResources = new Hashtable<String, TiledTextureRegion>();
	}
	
	private GameMapManager(){
		
	}
	
	public static GameMapManager getInstance(){
		if(mInstance==null)
			mInstance = new GameMapManager();
		return mInstance;
	}

	public static void loadResource() {
		// Tải các tài nguyên liên quan đến bản đồ, vd: gạch, đại bàng, nước, ...
		// Tham khảo cách load tài nguyên ở class GameControllerManager
		
	}


	public static void loadMapData(int mStage) {
		//Tải màn chơi mà người dùng đang chơi dở dang
		GameMapManager.CurrentMap = new Map();
		GameMapManager.CurrentMap.loadMapData(mStage);
	}

	
}
