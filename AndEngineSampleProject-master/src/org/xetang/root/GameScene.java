package org.xetang.root;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.color.Color;
import org.xetang.controller.Console;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.manager.GameMapManager;
import org.xetang.map.Bullet;
import org.xetang.map.IBullet;
import org.xetang.tank.Normal;
import org.xetang.tank.Tank;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameScene extends Scene implements IOnSceneTouchListener {

	GameMapManager _mapManager;
	Console _console;
	public GameScene() {
		GameManager.loadGameData();
		initScene();

		int iCurrentStage = GameManager.getCurrentStage();
		_mapManager = new GameMapManager(this, iCurrentStage);
		this.registerUpdateHandler(_mapManager);
		_console = new Console(_mapManager.getPlayerTank());
	}
	BitmapTextureAtlas mBitmapTextureAtlas;
	TiledTextureRegion mBoxFaceTextureRegion;
	private void initScene() {
		createBackground();
		createPhysics();
	}

	private void createBackground() {
		
		
		setBackground(new Background(Color.BLACK));
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				GameManager.Context.getTextureManager(), 64, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas,
						GameManager.Context, "gfx/face_box_tiled.png", 0, 0,
						2, 1); // 64x32
		GameManager.TextureManager.loadTexture(
				this.mBitmapTextureAtlas);
		

	}

	private void createPhysics() {
		setOnSceneTouchListener(this);
		
		GameManager.PhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);

		registerUpdateHandler(GameManager.PhysicsWorld);

	}

	/*
	 * ĂN ĐI KU Xử lý thông báo thắng cho người chơi
	 */
	public void onWin() {
		// TODO Auto-generated method stub

	}

	/*
	 * ĂN ĐI KU Xử lý thông báo thua cho người chơi
	 */
	public void onLose() {
		// TODO Auto-generated method stub

	}

	int count = 0;
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (!pSceneTouchEvent.isActionDown()) {
			return false;
		}
		
		IBullet bullet = new Bullet(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()) ;
		final AnimatedSprite face;
		final Body body;

		face = new AnimatedSprite(pSceneTouchEvent.getX(),
				pSceneTouchEvent.getY(), this.mBoxFaceTextureRegion,
				GameManager.Context.getVertexBufferObjectManager());
		body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, face,
				BodyType.DynamicBody,
				PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f));

		face.animate(200);

		GameManager.Scene.attachChild(face);
		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				face, body, true, true));


		bullet.readyToFire(Direction.values()[count % 4]);
		bullet.beFired();
		GameManager.Scene.attachChild((IEntity) bullet);
		++count;
		
		return true;
	}

	public void onSwitched() {
		_console = new Console(_mapManager.getPlayerTank());
		
	}
}