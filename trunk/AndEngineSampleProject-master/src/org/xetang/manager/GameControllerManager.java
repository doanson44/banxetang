package org.xetang.manager;

import java.util.Dictionary;
import java.util.Hashtable;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.controller.IGameController;

public class GameControllerManager {
	static IGameController mController;
	static Dictionary<String, TiledTextureRegion> mResources;
	
	static{
		mResources = new Hashtable<String, TiledTextureRegion>();
	}
	
	public static void loadResource() {
		//Tải tài nguyên của Controller ở đây, vd: ảnh rẽ trái,...
		//...
		
		//Ví dụ mẫu, load nút Fire
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("controller/");
		BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(GameManager.TextureManager, 64, 64);
		TiledTextureRegion texture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
										textureAtlas, 
										GameManager.AssetManager, 
										"fire_button_64.png", 0, 0, 1, 1);
		textureAtlas.load();
		mResources.put("fire button", texture);
		
	}

	public static void setupControls() {
		//Các công việc cần thực hiện trong phương thức này gồm:
		//  1/ Đăng ký các nút bấm với đối tượng Scene.
		//  2/ Bắt sự kiện khi người dùng chạm với các nút.
		//  3/ Gọi các phương thức tương ứng trong mController
		//.....
		
		HUD hud = new HUD(); //Hộp chứa các nút
		
		//Nút bắn đạn
		TiledSprite fireSprite = new TiledSprite(
								673, 
								GameManager.Camera.getHeight() - 128 , 
								(TiledTextureRegion)mResources.get("fire button"), 
								GameManager.VertexBufferObject)
		{
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					//Xử lý khi nút Fire được nhấn
					//....
					Debug.d(GameManager.TANK_TAG, "Fire được nhấn"); //debug
					if(mController!=null)
						mController.onFire();
					
				}
				
				return true;				
			};
		};
		
		hud.registerTouchArea(fireSprite);
		hud.attachChild(fireSprite);
		
		
		//Các nút được gắn vào 1 HUD duy nhất
		//HUD này sẽ được gắn vào Camera
		GameManager.Camera.setHUD(hud);
	}
	
	public static void setOnController(IGameController controller){
		GameControllerManager.mController = controller;
	}

}
