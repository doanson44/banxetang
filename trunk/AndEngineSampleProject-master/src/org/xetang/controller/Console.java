package org.xetang.controller;

import java.util.HashMap;
import java.util.Map;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameControllerManager;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;

import android.graphics.Point;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 */
public class Console extends Controller {
	static final int SIZE_CELL = GameControllerManager.SIZE_CELL;

	Map<String, TiledSprite> _buttonBoard = new HashMap<String, TiledSprite>();
	TiledSprite forwardSprite;
	TiledSprite backwardSprite;
	TiledSprite leftSprite;
	TiledSprite rightSprite;
	boolean bMiddleButtonPressed;
	TiledSprite middleSprite;
	Point Pivot = new Point(10, GameManager.CAMERA_HEIGHT - SIZE_CELL * 13);
	Direction mDirection = Direction.NONE;
	boolean mIsActive = true;
	int mMovePointerId = -1;

	public Console(IGameController playerTank) {
		this.mController = playerTank;

		createAllButton();
	}

	/*
	 * ĂN ĐI KU Tạo tất cả các nút trên bàn điều khiển VD: Map(Up, UpArrow)...
	 * Gán các sự kiện nút nhấn tương ứng với việc điều khiển xe tăng
	 */
	private void createAllButton() {
		// Các công việc cần thực hiện trong phương thức này gồm:
		// 1/ Đăng ký các nút bấm với đối tượng Scene.
		// 2/ Bắt sự kiện khi người dùng chạm với các nút.
		// 3/ Gọi các phương thức tương ứng trong mController
		// .....

		HUD hud = new HUD() {

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (mController != null && mController.isReady()) {

					forwardSprite.setCurrentTileIndex(0);
					backwardSprite.setCurrentTileIndex(0);
					leftSprite.setCurrentTileIndex(0);
					rightSprite.setCurrentTileIndex(0);

					switch (mDirection) {
					case UP:
						forwardSprite.setCurrentTileIndex(1);
						mController.onForward();
						break;

					case DOWN:
						backwardSprite.setCurrentTileIndex(1);
						mController.onBackward();
						break;

					case LEFT:
						leftSprite.setCurrentTileIndex(1);
						mController.onLeft();
						break;

					case RIGHT:
						rightSprite.setCurrentTileIndex(1);
						mController.onRight();
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
				if (pSceneTouchEvent.getPointerID() == mMovePointerId) {
					if (bMiddleButtonPressed && !isOutOfBound(realX, realY)) {
						middleSprite.setX(realX - middleSprite.getWidth() / 2);
						middleSprite.setY(realY - middleSprite.getHeight() / 2);
					}

					final Vector2 vector = new Vector2();
					vector.x = realX
							- (Pivot.x + SIZE_CELL * 3 + middleSprite
									.getWidth() / 2);
					vector.y = realY
							- (Pivot.y + SIZE_CELL * 3 + middleSprite
									.getHeight() / 2);

					float angle = vector.angle();
					if (angle >= 45 && angle <= 135) {
						mDirection = Direction.DOWN;
					} else if (angle >= 135 && angle <= 225) {
						mDirection = Direction.LEFT;
					} else if (angle >= 225 && angle <= 315) {
						mDirection = Direction.UP;
					} else {
						mDirection = Direction.RIGHT;
					}

					if (pSceneTouchEvent.isActionUp()
							|| isOutOfBound(realX, realY)) {
						bMiddleButtonPressed = false;
						mDirection = Direction.NONE;
						mMovePointerId = -1;

						middleSprite.setX(Pivot.x + SIZE_CELL * 3);
						middleSprite.setY(Pivot.y + SIZE_CELL * 3);
					}

					return true;
				}

				return super.onSceneTouchEvent(pSceneTouchEvent);
			}

			final float pad = 100;

			private boolean isOutOfBound(float x, float y) {
				return x < Pivot.x - pad || x > Pivot.x + pad + SIZE_CELL * 8
						|| y < Pivot.y - pad
						|| y > Pivot.y + pad + SIZE_CELL * 8;
			}
		}; // Hộp chứa các nút

		// Nút bắn đạn
		TiledSprite fireSprite = new TiledSprite(0, 0,
				(TiledTextureRegion) GameControllerManager
						.getResource("fire button"),
				GameManager.VertexBufferObject) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					// Xử lý khi nút Fire được nhấn
					// ....
					Debug.d(GameManager.TANK_TAG, "Fire được nhấn"); // debug
					if (mController != null && mController.isReady())
						mController.onFire();

				}

				return true;
			};
		};
		fireSprite.setPosition(
				(GameManager.CAMERA_WIDTH - Math.abs(GameManager.CAMERA_X) - GameManager.MAP_SIZE) / 2 - fireSprite.getWidth() / 2 + Math.abs(GameManager.CAMERA_X) + GameManager.MAP_SIZE,
				GameManager.CAMERA_HEIGHT - SIZE_CELL * 9
						- fireSprite.getHeight() / 2);

		hud.registerTouchArea(fireSprite);
		hud.attachChild(fireSprite);

		// Nút lên
		forwardSprite = new TiledSprite(Pivot.x + SIZE_CELL * 3, Pivot.y,
				GameControllerManager.getResource("button up"),
				GameManager.VertexBufferObject) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()|| pSceneTouchEvent.isActionMove()){
					mDirection = Direction.UP;
					bMiddleButtonPressed = false;
				}
				else {
					mDirection = Direction.NONE;

				}
				return true;
			}
		};

		hud.registerTouchArea(forwardSprite);
		hud.attachChild(forwardSprite);

		// Nút xuống
		backwardSprite = new TiledSprite(Pivot.x + SIZE_CELL * 3, Pivot.y
				+ SIZE_CELL * 6,
				GameControllerManager.getResource("button down"),
				GameManager.VertexBufferObject) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()|| pSceneTouchEvent.isActionMove()){
					mDirection = Direction.DOWN;
					bMiddleButtonPressed = false;
				}
				else {
					mDirection = Direction.NONE;

				}
				return true;
			}
		};

		hud.registerTouchArea(backwardSprite);
		hud.attachChild(backwardSprite);

		// Nút trái
		leftSprite = new TiledSprite(Pivot.x, Pivot.y + SIZE_CELL * 3,
				GameControllerManager.getResource("button left"),
				GameManager.VertexBufferObject) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()||pSceneTouchEvent.isActionMove()){
					mDirection = Direction.LEFT;
					bMiddleButtonPressed = false;
				}
				else {
					mDirection = Direction.NONE;

				}
				return true;
			}
		};

		hud.registerTouchArea(leftSprite);
		hud.attachChild(leftSprite);

		// Nút phải
		rightSprite = new TiledSprite(Pivot.x + SIZE_CELL * 6, Pivot.y
				+ SIZE_CELL * 3,
				GameControllerManager.getResource("button right"),
				GameManager.VertexBufferObject) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()){
					mDirection = Direction.RIGHT;
					bMiddleButtonPressed = false;
				}
				else {
					mDirection = Direction.NONE;

				}
				return true;
			}
		};

		hud.registerTouchArea(rightSprite);
		hud.attachChild(rightSprite);

		// nut giữa
		middleSprite = new TiledSprite(Pivot.x + SIZE_CELL * 3, Pivot.y
				+ SIZE_CELL * 3,
				GameControllerManager.getResource("middle button"),
				GameManager.VertexBufferObject) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {
					mMovePointerId = pSceneTouchEvent.getPointerID();
					bMiddleButtonPressed = true;
				} else if (pSceneTouchEvent.isActionUp()) {
					bMiddleButtonPressed = false;
					mDirection = Direction.NONE;
					mMovePointerId = -1;

					this.setX(Pivot.x + SIZE_CELL * 3);
					this.setY(Pivot.y + SIZE_CELL * 3);

				} else if (pSceneTouchEvent.isActionMove()) {
					;
				}// moving

				return true;
			}
		};
		middleSprite.setSize(SIZE_CELL * 2, SIZE_CELL * 2);
		hud.registerTouchArea(middleSprite);
		hud.attachChild(middleSprite);
		
		//nut turn on/off sound
		TiledSprite soundSprite = new TiledSprite(
				0, 
				0, 
				(ITiledTextureRegion) GameManager.getTexture("sound"), 
				GameManager.VertexBufferObject){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()){
					int i = this.getCurrentTileIndex() + 1;
					i %= 2;
					this.setCurrentTileIndex(i);
					
					if (i == 0 )
						turnOnMusic();
					else 
						turnOffMusic();
				}
				return true;
			}
		};
		soundSprite.setPosition(Math.abs(GameManager.CAMERA_X) / 2 - soundSprite.getWidth() / 2,
				GameManager.CAMERA_HEIGHT / 100 * 99 - soundSprite.getHeight());
		hud.attachChild(soundSprite);
		hud.registerTouchArea(soundSprite);
		
		// Các nút được gắn vào 1 HUD duy nhất
		// HUD này sẽ được gắn vào Camera
		GameManager.Camera.setHUD(hud);
	}

	protected void turnOffMusic() {
		GameManager.turnOnOffMusic(0);
	}

	protected void turnOnMusic() {
		GameManager.turnOnOffMusic(1f);		
	}

	public void turnOffHandle() {
		if (mIsActive) {
			GameManager.Camera.getHUD().clearTouchAreas();
			mDirection = Direction.NONE;
		}

		mIsActive = false;
	}

}