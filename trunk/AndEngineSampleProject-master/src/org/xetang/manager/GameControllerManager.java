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

	static {
		mResources = new Hashtable<String, TiledTextureRegion>();
	}

	public static void loadResource() {
		// Tải tài nguyên của Controller ở đây, vd: ảnh rẽ trái,...
		// ...

		// Ví dụ mẫu, load nút Fire
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("controller/");
		BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 64, 64);
		TiledTextureRegion texture = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"fire_button_64.png", 0, 0, 1, 1);
		textureAtlas.load();
		mResources.put("fire button", texture);

		
		
		// 4 nút di chuyển
		BitmapTextureAtlas textureAtlasMove = new BitmapTextureAtlas(
				GameManager.TextureManager, 32*3, 32*3);
		TiledTextureRegion textureUp = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"up_button_32.png", 112, (int) (GameManager.Camera.getHeight() - 176), 2, 1);
		TiledTextureRegion textureDown = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"down_button_32.png", 112, (int) (GameManager.Camera.getHeight() - 176), 2, 3);
		TiledTextureRegion textureLeft = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"left_button_32.png", 112, (int) (GameManager.Camera.getHeight() - 176), 1, 2);
		TiledTextureRegion textureRight = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"right_button_32.png", 112, (int) (GameManager.Camera.getHeight() - 176), 3, 2);
		
		
		
		textureAtlasMove.load();
		mResources.put("forward button", textureUp);
		mResources.put("backward button", textureDown);
		mResources.put("left button", textureLeft);
		mResources.put("right button", textureRight);
		
		
	}

	public static void setupControls() {
		// Các công việc cần thực hiện trong phương thức này gồm:
		// 1/ Đăng ký các nút bấm với đối tượng Scene.
		// 2/ Bắt sự kiện khi người dùng chạm với các nút.
		// 3/ Gọi các phương thức tương ứng trong mController
		// .....

		HUD hud = new HUD(); // Hộp chứa các nút

		// Nút bắn đạn
		TiledSprite fireSprite = new TiledSprite(673,
				GameManager.Camera.getHeight() - 128,
				(TiledTextureRegion) mResources.get("fire button"),
				GameManager.VertexBufferObject) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					// Xử lý khi nút Fire được nhấn
					// ....
					Debug.d(GameManager.TANK_TAG, "Fire được nhấn"); // debug
					if (mController != null)
						mController.onFire();

				}

				return true;
			};
		};

		hud.registerTouchArea(fireSprite);
		hud.attachChild(fireSprite);

		// Nút lên
		TiledSprite forwardSprite = new TiledSprite(144,
				GameManager.Camera.getHeight() - 176,
				(TiledTextureRegion) mResources.get("forward button"),
				GameManager.VertexBufferObject) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					Debug.d(GameManager.TANK_TAG, "up được nhấn"); // debug
					if (mController != null)
						mController.onForward();

				}

				return true;
			};
		};

		hud.registerTouchArea(forwardSprite);
		hud.attachChild(forwardSprite);

		// Nút xuống
		TiledSprite backwardSprite = new TiledSprite(144,
				GameManager.Camera.getHeight() - 112,
				(TiledTextureRegion) mResources.get("backward button"),
				GameManager.VertexBufferObject) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					Debug.d(GameManager.TANK_TAG, "down được nhấn"); // debug
					if (mController != null)
						mController.onBackward();

				}

				return true;
			};
		};

		hud.registerTouchArea(backwardSprite);
		hud.attachChild(backwardSprite);

		// Nút trái
		TiledSprite leftSprite = new TiledSprite(112,
				GameManager.Camera.getHeight() - 144,
				(TiledTextureRegion) mResources.get("left button"),
				GameManager.VertexBufferObject) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					Debug.d(GameManager.TANK_TAG, "left được nhấn"); // debug
					if (mController != null)
						mController.onLeft();

				}

				return true;
			};
		};

		hud.registerTouchArea(leftSprite);
		hud.attachChild(leftSprite);

		// Nút phải
		TiledSprite rightSprite = new TiledSprite(176,
				GameManager.Camera.getHeight() - 144,
				(TiledTextureRegion) mResources.get("right button"),
				GameManager.VertexBufferObject) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					Debug.d(GameManager.TANK_TAG, "right được nhấn"); // debug
					if (mController != null)
						mController.onRight();

				}

				return true;
			};
		};

		hud.registerTouchArea(rightSprite);
		hud.attachChild(rightSprite);

		// Các nút được gắn vào 1 HUD duy nhất
		// HUD này sẽ được gắn vào Camera
		GameManager.Camera.setHUD(hud);
	}

	public static void setOnController(IGameController controller) {
		GameControllerManager.mController = controller;
	}

}
