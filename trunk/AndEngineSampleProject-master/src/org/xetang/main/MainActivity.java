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
import org.xetang.map.MapObjectFactory;

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

		GameManager.Camera = new Camera(GameManager.CAMERA_X,
				GameManager.CAMERA_Y, GameManager.CAMERA_WIDTH,
				GameManager.CAMERA_HEIGHT);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				GameManager.Camera);

		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);

		return engineOptions;
	}

	@Override
	public void onCreateResources() {

		GameManager.Activity = this;
		GameManager.Engine = this.mEngine;
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

		super.onDestroyResources();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		GameManager.SwitchToScene("game");

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
}
