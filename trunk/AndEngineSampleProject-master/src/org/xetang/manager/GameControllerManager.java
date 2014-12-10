package org.xetang.manager;

import java.util.Hashtable;
import java.util.Map;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.controller.IGameController;
import org.xetang.manager.GameManager.Direction;

import android.graphics.Point;

import com.badlogic.gdx.math.Vector2;

public class GameControllerManager {
	static IGameController mController;
	static Map<String, TiledTextureRegion> mResources;
	static boolean bMiddleButtonPressed;
	static TiledSprite middleSprite;
	static Point Pivot = new Point(30, GameManager.CAMERA_HEIGHT - 200);
	static {
		mResources = new Hashtable<String, TiledTextureRegion>();
	}
	static Direction mDirection = Direction.NONE;

	public static void loadResource() {
		// Tải tài nguyên của Controller ở đây, vd: ảnh rẽ trái,...
		// ...

		// Ví dụ mẫu, load nút Fire
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/controller/");
		BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 64, 64);
		TiledTextureRegion texture = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"fire_button_64.png", 0, 0, 1, 1);
		textureAtlas.load();
		mResources.put("fire button", texture);

		
		
		// 4 nút di chuyển
		BitmapTextureAtlas textureAtlasMove = new BitmapTextureAtlas(
				GameManager.TextureManager, 32, 32*4);
		TiledTextureRegion textureUp = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlasMove, GameManager.AssetManager,
						"up_button_32.png", 0,0, 1, 1);
		TiledTextureRegion textureDown = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlasMove, GameManager.AssetManager,
						"down_button_32.png", 0,32,1, 1);
		TiledTextureRegion textureLeft = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlasMove, GameManager.AssetManager,
						"left_button_32.png", 0,64, 1, 1);
		TiledTextureRegion textureRight = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlasMove, GameManager.AssetManager,
						"right_button_32.png", 0,96, 1, 1);
	
		textureAtlasMove.load();
		mResources.put("forward button", textureUp);
		mResources.put("backward button", textureDown);
		mResources.put("left button", textureLeft);
		mResources.put("right button", textureRight);
		
		
		//middle circle
		textureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 128, 128, TextureOptions.BILINEAR);
		TiledTextureRegion textureMiddle = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"circle1_128.png", 0, 0, 1, 1);
		textureAtlas.load();
		mResources.put("middle button", textureMiddle);
		
	}
	
	public static void setupControls() {
		// Các công việc cần thực hiện trong phương thức này gồm:
		// 1/ Đăng ký các nút bấm với đối tượng Scene.
		// 2/ Bắt sự kiện khi người dùng chạm với các nút.
		// 3/ Gọi các phương thức tương ứng trong mController
		// .....

		HUD hud = new HUD(){
			long prevTime = 0;
			long WaitTimeMilis = 300; //300 mili seconds
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if( System.currentTimeMillis() - prevTime >= WaitTimeMilis && mController != null){
					prevTime = System.currentTimeMillis();
					switch (mDirection) {
						case UP:
							mController.onForward();
							Debug.d(GameManager.TANK_TAG, "onForward");
						break;
						
						case DOWN:
							mController.onBackward();
							Debug.d(GameManager.TANK_TAG, "onBackward");
						break;
						
						case LEFT:
							mController.onLeft();
							Debug.d(GameManager.TANK_TAG, "onLeft");
						break;
						
						case RIGHT:
							mController.onRight();
							Debug.d(GameManager.TANK_TAG, "onRight");
						break;
	
						default:
							mController.onIdle();
							break;
					}
				}			
			}
			
			@Override
			public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
				float realX = pSceneTouchEvent.getX() - GameManager.CAMERA_X;
				float realY = pSceneTouchEvent.getY() - GameManager.CAMERA_Y;
				if (pSceneTouchEvent.isActionMove() && bMiddleButtonPressed && !isOutOfBound(realX, realY)){
					GameControllerManager.middleSprite.setX(realX - GameControllerManager.middleSprite.getWidth()/2);
					GameControllerManager.middleSprite.setY(realY - GameControllerManager.middleSprite.getHeight()/2);
				}
				
				if (isOutOfBound(realX, realY)){
					GameControllerManager.middleSprite.setX(Pivot.x + 48);
					GameControllerManager.middleSprite.setY(Pivot.y + 48);
					mDirection = Direction.NONE;
				}
				return super.onSceneTouchEvent(pSceneTouchEvent);
			}
			
			
			private boolean isOutOfBound(float x, float y) {
				float pad = 10;
				return x < Pivot.x - pad || x > Pivot.x + pad + 128 || y < Pivot.y - pad || y > Pivot.y + pad + 128;
			}
		}; // Hộp chứa các nút
		
		

		// Nút bắn đạn
		TiledSprite fireSprite = new TiledSprite(GameManager.CAMERA_WIDTH - 136,
				GameManager.CAMERA_HEIGHT - 136,
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
		fireSprite.setPosition(GameManager.CAMERA_WIDTH - 136, GameManager.CAMERA_HEIGHT - 136 - fireSprite.getHeight()/2);

		hud.registerTouchArea(fireSprite);
		hud.attachChild(fireSprite);

		// Nút lên
		TiledSprite forwardSprite = new TiledSprite(Pivot.x + 48,
				Pivot.y,
				 mResources.get("forward button"),
				GameManager.VertexBufferObject);

		hud.registerTouchArea(forwardSprite);
		hud.attachChild(forwardSprite);

		// Nút xuống
		TiledSprite backwardSprite = new TiledSprite(Pivot.x + 48, 
				Pivot.y + 96,
				 mResources.get("backward button"),
				GameManager.VertexBufferObject);

		hud.registerTouchArea(backwardSprite);
		hud.attachChild(backwardSprite);

		// Nút trái
		TiledSprite leftSprite = new TiledSprite(Pivot.x,
				Pivot.y + 48,
				 mResources.get("left button"),
				GameManager.VertexBufferObject) ;

		hud.registerTouchArea(leftSprite);
		hud.attachChild(leftSprite);

		// Nút phải
		TiledSprite rightSprite = new TiledSprite(Pivot.x + 96,
				Pivot.y + 48,
				 mResources.get("right button"),
				GameManager.VertexBufferObject);

		hud.registerTouchArea(rightSprite);
		hud.attachChild(rightSprite);
		
		//nut giữa
		middleSprite = new TiledSprite(Pivot.x + 48, 
									Pivot.y + 48, 
									mResources.get("middle button"), 
									GameManager.VertexBufferObject){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown())
					bMiddleButtonPressed = true;
				else if(pSceneTouchEvent.isActionUp()){
					bMiddleButtonPressed = false;
					mDirection = Direction.NONE;
					
					this.setX( Pivot.x + 48);
					this.setY( Pivot.y + 48);
				}
				else if (pSceneTouchEvent.isActionMove()){
					Vector2 vector = new Vector2();
					vector.x = pSceneTouchEvent.getX() - (Pivot.x + 48 + this.getWidth()/2);
					vector.y = pSceneTouchEvent.getY() - (Pivot.y + 48 + this.getHeight()/2);
					
					float angle = vector.angle();
					if (angle >= 45 && angle <= 135){
						mDirection = Direction.DOWN;
					}
					else if (angle >= 135 && angle <= 225){
						mDirection = Direction.LEFT;
					}
					else if (angle >= 225 && angle <= 315){				
						mDirection = Direction.UP;
					}
					else {
						mDirection = Direction.RIGHT;
					}
				}// moving

				
				return true;
			}
		};
		middleSprite.setSize(32, 32);
		hud.registerTouchArea(middleSprite);
		hud.attachChild(middleSprite);
		
		

		// Các nút được gắn vào 1 HUD duy nhất
		// HUD này sẽ được gắn vào Camera
		GameManager.Camera.setHUD(hud);
	}

	public static void setOnController(IGameController controller) {
		GameControllerManager.mController = controller;
	}

}
