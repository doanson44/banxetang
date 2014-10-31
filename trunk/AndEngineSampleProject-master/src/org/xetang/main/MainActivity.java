package org.xetang.main;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.root.MainMenuScene;

import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */

// RealMayo: THIS PROJECT IS SIMPLY THE CODE TAKEN FROM PhysicsExample.java FROM
// THE AndEngineExamples

public class MainActivity extends SimpleBaseGameActivity implements
		IAccelerationListener, IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// default
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private static final FixtureDef FIXTURE_DEF = PhysicsFactory
			.createFixtureDef(1, 0.5f, 0.5f);

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	private TiledTextureRegion mTriangleFaceTextureRegion;
	private TiledTextureRegion mHexagonFaceTextureRegion;

	private Scene mScene;
	private Camera mCamera;

	private PhysicsWorld mPhysicsWorld;
	private int mFaceCount = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Touch the screen to add objects.",
				Toast.LENGTH_LONG).show();

		this.mCamera = new Camera(-190, -30, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), this.mCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

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

		GameManager.Camera = this.mCamera;
		GameManager.TextureManager = this.getTextureManager();
		GameManager.AssetManager = this.getAssets();
		GameManager.Context = this;
		GameManager.VertexBufferObject = this.getVertexBufferObjectManager();

		GameManager.loadResource();

	}

	TiledTextureRegion mTankTextureRegion;
//
//	private void loadResource() {
//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("tank/");
//		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
//				this.getTextureManager(), 32, 32);
//
//		this.mTankTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createTiledFromAsset(this.mBitmapTextureAtlas,
//						this.getAssets(), "tank_north_32.png", 0, 0, 1, 1);
//		this.mBitmapTextureAtlas.load();
//	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new MainMenuScene();
		
		/*
		this.mScene.setBackground(new Background(0, 0, 0));
		this.mScene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);*/

		// Thiết lập biến toàn cục
		GameManager.Scene = this.mScene;
		GameManager.PhysicsWorld = this.mPhysicsWorld;

		return this.mScene;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		if (this.mPhysicsWorld != null) {
			if (pSceneTouchEvent.isActionDown()) {
				this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

				return true;
			}

		}
		return false;
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

	
	public void setScene(Scene scene) {
		this.mScene = scene;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
