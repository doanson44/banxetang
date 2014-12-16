package org.xetang.root;


import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.util.color.Color;
import org.xetang.controller.Console;
import org.xetang.manager.GameItemManager;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameMapManager;
import org.xetang.tank.Tank;

import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Looper;

import com.badlogic.gdx.math.Vector2;

public class GameScene extends Scene implements IOnSceneTouchListener {

	GameMapManager _mapManager;
	Console _console;
	Handler handler = new Handler(Looper.getMainLooper());;
	Text gameOrver;
	boolean mIsWin; //Thắng hay thua 
	
	boolean mIsBackgroundPlaying = true;
	Music mStartGameMusic;
	Sound mBackgroundSound;
	
	public GameScene() {
		
		
		/* choi nhac */
		mBackgroundSound = GameManager.getSound("background");
		mBackgroundSound.setLooping(true);
		mStartGameMusic = GameManager.getMusic("gamestart");
		mStartGameMusic.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (!mBackgroundSound.isReleased() && mBackgroundSound.isLoaded())
					mBackgroundSound.play();
			}
		});

	}
	
	public void loadScene()
	{
		initScene();
		bFinish =  false;
		
		mStartGameMusic.play();
		
		int iCurrentStage = GameManager.getCurrentStage();
		_console = new Console(null);
		_mapManager = new GameMapManager(this, iCurrentStage);
		
		registerUpdateHandler(_mapManager);
		registerUpdateHandler(GameItemManager.getInstance());
	}

	private void initScene() {
		createBackground();
		createPhysics();
	}

	private void createBackground() {
		setBackground(new Background(Color.BLACK));
	}

	private void createPhysics() {
		setOnSceneTouchListener(this);

		GameManager.PhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);

		registerUpdateHandler(GameManager.PhysicsWorld);

	}

	boolean bFinish = false;
	/*
	 * ĂN ĐI KU Xử lý thông báo thắng cho người chơi
	 */
	public void onWin() {
		if (bFinish) return;
		bFinish = true;
		mIsWin = true;
		handler.postDelayed(new SwitchingHighScoreScene(), 2000);
		//sound
		mBackgroundSound.stop();
		
		GameManager.saveCurrentTankLevel( GameManager.CurrentMap.getPlayerTanks().get(0).getLevel() );
	}

	/*
	 * ĂN ĐI KU Xử lý thông báo thua cho người chơi
	 */
	public void onLose() {
		if (bFinish) return;
		bFinish = true;
		mIsWin = false;
		//sound
		GameManager.getMusic("gameover").play();

		_console.turnOffHandle();
		
		showNotification();
		handler.postDelayed(new SwitchingHighScoreScene(), 5000);

		GameManager.resetTankLevel();
		GameManager.resetCurrentTankLife();
	}

	private void showNotification() {
		Font f = GameManager.getFont("font2", 48f, Color.RED_ARGB_PACKED_INT);
		gameOrver = new Text(0, 0, f, "Game Over", GameManager.VertexBufferObject) ;
		gameOrver.setPosition(GameManager.CAMERA_HEIGHT/2 - gameOrver.getWidth()/2 - GameManager.CAMERA_X, GameManager.CAMERA_HEIGHT - gameOrver.getHeight());
		//Body body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, t, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		//GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(t, body, true, false));
		//body.setLinearVelocity(new Vector2(0,-2));
		GameManager.Camera.getHUD().attachChild(gameOrver);
		gameOrver.setZIndex(50);
	}

	int count = 0;

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (!pSceneTouchEvent.isActionDown()) {
			return false;
		}

		return true;
	}

	public void onSwitched(String action) {
		if (action == GameManager.ACTION_SCENE_OPEN)
		{
			loadScene();
		}
		else
		{
			cleanScene();
		}

	}
	
	private void cleanScene() {
		if (_mapManager != null)
			_mapManager.clearMapData();
		clearUpdateHandlers();
		
		if (mStartGameMusic.isPlaying()){
			mStartGameMusic.pause();
			mStartGameMusic.seekTo(0);
		}
		if (!mBackgroundSound.isReleased())
			mBackgroundSound.stop();
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

				
		if (gameOrver != null)
			gameOrver.setY(gameOrver.getY() - 2);
		
	}
	
	class SwitchingHighScoreScene implements Runnable{
		@Override
		public void run() {
			handler.sendEmptyMessage(1);
			GameManager.switchToScene("highscore", mIsWin);
			mBackgroundSound.stop();
			mStartGameMusic.pause();
			mStartGameMusic.seekTo(0);
		}
		
	}

	public void setController(Tank t) {
		_console.setOnController(t);
		
	}


	
	
	
}