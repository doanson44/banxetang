package org.xetang.main;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.xetang.manager.GameManager;
<<<<<<< .mine
import org.xetang.map.MapObjectFactory;
import org.xetang.root.GameScene;
=======
import org.xetang.root.GameScene;
import org.xetang.root.MainMenuScene;
>>>>>>> .r33

import android.widget.Toast;

public class MainActivity extends SimpleBaseGameActivity implements
		IAccelerationListener {

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {

		Engine engine = new LimitedFPSEngine(pEngineOptions, 60);

		try {
			if (MultiTouch.isSupported(this)) {
				engine.setTouchController(new MultiTouchController());
				if (MultiTouch.isSupportedDistinct(this)) {
					Toast.makeText(
							this,
							"MultiTouch detected --> Both controls will work properly!",
							Toast.LENGTH_SHORT).show();
				} else {
					GameManager.PlaceOnScreenControlsAtDifferentVerticalLocations = true;
					Toast.makeText(
							this,
							"MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						this,
						"Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(
					this,
					"Sorry your Android Version does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.",
					Toast.LENGTH_LONG).show();
		}

		return engine;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {

<<<<<<< .mine
		GameManager.Camera = new Camera(GameManager.CAMERA_X,
				GameManager.CAMERA_Y, GameManager.CAMERA_WIDTH,
				GameManager.CAMERA_HEIGHT);
=======
		this.mCamera = new Camera(GameManager.CAMERA_X, GameManager.CAMERA_Y, GameManager.CAMERA_WIDTH, GameManager.CAMERA_HEIGHT);
>>>>>>> .r33

<<<<<<< .mine
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				GameManager.Camera);

		return engineOptions;
=======
		EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), this.mCamera);
>>>>>>> .r33
		options.getAudioOptions().setNeedsMusic(true);
		options.getAudioOptions().setNeedsSound(true);
		return options;
	}

	@Override
	public void onCreateResources() {

<<<<<<< .mine
		GameManager.Activity = this;
		GameManager.Engine = this.mEngine;
=======
//		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
//				this.getTextureManager(), 64, 128, TextureOptions.BILINEAR);
//		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
//						"face_box_tiled.png", 0, 0, 2, 1); // 64x32
//		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
//						"face_circle_tiled.png", 0, 32, 2, 1); // 64x32
//		this.mTriangleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
//						"face_triangle_tiled.png", 0, 64, 2, 1); // 64x32
//		this.mHexagonFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
//						"face_hexagon_tiled.png", 0, 96, 2, 1); // 64x32
//		this.mBitmapTextureAtlas.load();
		
		GameManager.Engine = this.mEngine;
		GameManager.Camera = this.mCamera;
>>>>>>> .r33
		GameManager.TextureManager = this.getTextureManager();
		GameManager.AssetManager = this.getAssets();
		GameManager.Context = this;
		GameManager.VertexBufferObject = this.getVertexBufferObjectManager();
		GameManager.VertexBufferObject = this.getVertexBufferObjectManager();
		GameManager.FontManager = this.getFontManager();
		GameManager.MusicManager = this.getMusicManager();

		GameManager.loadResource();
	}

	@Override
	public void onDestroyResources() throws Exception {
		MapObjectFactory.unloadAll();

<<<<<<< .mine
		super.onDestroyResources();
=======
		GameManager.SwitchToScene(0);
		
		/*
		this.mScene.setBackground(new Background(0, 0, 0));
		this.mScene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);*/

		// Thiết lập biến toàn cục
		//GameManager.Scene = this.mScene;
		//GameManager.PhysicsWorld = this.mPhysicsWorld;

		return GameManager.Scene;
>>>>>>> .r33
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		GameManager.Scene = new GameScene();

		return GameManager.Scene;
	}

	@Override
	public void onAccelerationAccuracyChanged(
			final AccelerationData pAccelerationData) {

	}

	@Override
	public void onAccelerationChanged(final AccelerationData pAccelerationData) {
		// final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(),
		// pAccelerationData.getY());
		// this.mPhysicsWorld.setGravity(gravity);
		// Vector2Pool.recycle(gravity);
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerationSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerationSensor();
	}
<<<<<<< .mine
=======

	// ===========================================================
	// Methods
	// ===========================================================

	private void addFace(final float pX, final float pY) {
		this.mFaceCount = 2;
		Debug.d("Faces: " + this.mFaceCount);

		final AnimatedSprite face;
		final Body body;

		if (this.mFaceCount % 4 == 0) {
			face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion,
					this.getVertexBufferObjectManager());
			body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face,
					BodyType.DynamicBody, FIXTURE_DEF);
		} else if (this.mFaceCount % 4 == 1) {
			face = new AnimatedSprite(pX, pY, this.mCircleFaceTextureRegion,
					this.getVertexBufferObjectManager());
			body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, face,
					BodyType.DynamicBody, FIXTURE_DEF);
		} else if (this.mFaceCount % 4 == 2) {
			face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion,
					this.getVertexBufferObjectManager());
			body = MainActivity.createTriangleBody(this.mPhysicsWorld, face,
					BodyType.DynamicBody, FIXTURE_DEF);
		} else {
			face = new AnimatedSprite(pX, pY, this.mHexagonFaceTextureRegion,
					this.getVertexBufferObjectManager());
			body = MainActivity.createHexagonBody(this.mPhysicsWorld, face,
					BodyType.DynamicBody, FIXTURE_DEF);
		}

		face.animate(200);
		this.mScene.attachChild(face);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face,
				body, true, true));

	}

	/**
	 * Creates a {@link Body} based on a {@link PolygonShape} in the form of a
	 * triangle:
	 * 
	 * <pre>
	 *  /\
	 * /__\
	 * </pre>
	 */
	private static Body createTriangleBody(final PhysicsWorld pPhysicsWorld,
			final IAreaShape pAreaShape, final BodyType pBodyType,
			final FixtureDef pFixtureDef) {
		/*
		 * Remember that the vertices are relative to the center-coordinates of
		 * the Shape.
		 */
		final float halfWidth = pAreaShape.getWidthScaled() * 0.5f
				/ PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = pAreaShape.getHeightScaled() * 0.5f
				/ PIXEL_TO_METER_RATIO_DEFAULT;

		final float top = -halfHeight;
		final float bottom = halfHeight;
		final float left = -halfHeight;
		final float centerX = 0;
		final float centerY = 0;
		final float right = halfWidth;

		final Vector2[] vertices = {

		new Vector2(left, top), new Vector2(centerX, top),
				new Vector2(right, top), new Vector2(right, centerY),
				new Vector2(right, bottom), new Vector2(centerX, bottom),
				new Vector2(left, bottom), new Vector2(left, centerY)

		};

		/*
		 * final Vector2[] vertices = { new Vector2(left, top), new
		 * Vector2(right, top), new Vector2(right, bottom), new Vector2(left,
		 * bottom) };
		 */

		return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape,
				vertices, pBodyType, pFixtureDef);
	}

	/**
	 * Creates a {@link Body} based on a {@link PolygonShape} in the form of a
	 * hexagon:
	 * 
	 * <pre>
	 *  /\
	 * /  \
	 * |  |
	 * |  |
	 * \  /
	 *  \/
	 * </pre>
	 */
	private static Body createHexagonBody(final PhysicsWorld pPhysicsWorld,
			final IAreaShape pAreaShape, final BodyType pBodyType,
			final FixtureDef pFixtureDef) {
		/*
		 * Remember that the vertices are relative to the center-coordinates of
		 * the Shape.
		 */
		final float halfWidth = pAreaShape.getWidthScaled() * 0.5f
				/ PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = pAreaShape.getHeightScaled() * 0.5f
				/ PIXEL_TO_METER_RATIO_DEFAULT;

		/*
		 * The top and bottom vertex of the hexagon are on the bottom and top of
		 * hexagon-sprite.
		 */
		final float top = -halfHeight;
		final float bottom = halfHeight;

		final float centerX = 0;

		/*
		 * The left and right vertices of the heaxgon are not on the edge of the
		 * hexagon-sprite, so we need to inset them a little.
		 */
		final float left = -halfWidth + 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float right = halfWidth - 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float higher = top + 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float lower = bottom - 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;

		final Vector2[] vertices = { new Vector2(centerX, top),
				new Vector2(right, higher), new Vector2(right, lower),
				new Vector2(centerX, bottom), new Vector2(left, lower),
				new Vector2(left, higher) };

		return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape,
				vertices, pBodyType, pFixtureDef);
	}

	
	
	@Override
	public void onBackPressed() {
		if(GameManager.Scene != GameManager.ListScene.get(0)){
			GameManager.SwitchToScene(0);
			return;
		}
		super.onBackPressed();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
>>>>>>> .r33
}
