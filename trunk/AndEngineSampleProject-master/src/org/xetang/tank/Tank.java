package org.xetang.tank;



import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.controller.Controller;
import org.xetang.controller.IGameController;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.manager.TankManager;
import org.xetang.map.Bullet;
import org.xetang.map.IMapObject;
import org.xetang.map.Map;
import org.xetang.map.MapObject;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.root.GameEntity;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Transform;


/**
 * 
 */

public class Tank extends GameEntity implements IGameController, IMapObject {

	float DEGTORAD  = 0.0174532925199432957f;
	Controller mController;
	
	Bullet 	mBullet;
	boolean mIsFiring; 		//Xe tăng có đang bắn đạn hay không?
	Direction 	mDirection; 	//Hướng của xe tăng
	int 	mLevel;			//Cấp độ của xe tăng
	boolean mIsAlive;
	boolean mIsFreeze = false;
	Body _body;
	Map _map;
	TiledSprite tankSprite;
	int pX, pY;				
	float speed;				// tốc độ của xe tăng
	ObjectType _type;
	FixtureDef _ObjectFixtureDef;
	float _distinctMove = 10;
	public synchronized boolean isAlive() {
		return mIsAlive;
	}
	
	public void killSelf(){
		mIsAlive = false;
		mController.onTankDie();
	}

	public TiledSprite GetCurrentSprite (){
		return tankSprite;
	}

	public Tank(int px, int py, Map map, TiledTextureRegion region){
		//mBullet = new Bullet(this);
		_map = map;
		TankManager.register(this);
    	tankSprite = new TiledSprite(px, py, region, GameManager.VertexBufferObject);
    	mDirection = Direction.Down;
    	_ObjectFixtureDef =  PhysicsFactory
				.createFixtureDef(1, 0, 0);
    	CreateBody();
	}

	protected void CreateBody (){
		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, tankSprite,
				BodyType.DynamicBody,_ObjectFixtureDef);
		_body.setGravityScale(0);
		_body.setFixedRotation(true);
		_body.setUserData(this);
		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				tankSprite, _body, true, true));
		
	}
	
	public void setController(Controller controller){
		this.mController = controller;
	}
	
	public void loadOldData() {
		// TODO Auto-generated method stub
		
	}


	// di chuyển qua trái
	  @Override
	    public void onLeft() {
	    	// TODO Auto-generated method stub
		  	mDirection = Direction.Left;
		  	
		  	SetTranform(90);
		  	_body.setLinearVelocity(-speed, 0);
	    }

	// di chuyển qua phải
	    @Override
	    public void onRight() {
	    	// TODO Auto-generated method stub
			mDirection = Direction.Right;
			
			SetTranform(270);
			
			//tankSprite.setCurrentTileIndex(2);
		  	_body.setLinearVelocity(speed, 0);
	    }
	    
	    
	    // // di chuyển qua lên
	    @Override
	    public void onForward() {
	    	// TODO Auto-generated method stub
			mDirection = Direction.Up;
			SetTranform(180);
	    	_body.setLinearVelocity(0, -speed);
	    }
	   
	    /// di chuyển qua xuống
	    @Override
	    public void onBackward() {
	    	// TODO Auto-generated method stub
	    	mDirection = Direction.Down;
	    	//tankSprite.setCurrentTileIndex(0);
	    	_body.setTransform(_body.getTransform().getPosition(), 0 * DEGTORAD);
	    	_body.setLinearVelocity(0, speed);
	    }
		@Override
		public void onCancelMove() {
			// TODO Auto-generated method stub
			_body.setLinearVelocity(0, 0);
		}
		

	public void SetTranform(float degree){
		_body.setTransform(_body.getTransform().getPosition(), 0);
		_body.setTransform(_body.getTransform().getPosition(), degree * DEGTORAD);
	}
	
	@Override
	public void onFire() {
		// TODO Auto-generated method stub
		
	}

	public void CreateShield() {
		// TODO Auto-generated method stub
		
	}

	public void PowerUp() {
		// TODO Auto-generated method stub
		
	}


	public void FreezeSelf() {
		// TODO Auto-generated method stub
		mIsFreeze = true;
	}

	public MapObject clone(){
		return null;
	}
	
	@Override
	public void putToWorld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putToWorld(float posX, float posY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transform(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getCellWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCellHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public synchronized void setAlive(boolean alive) {
		mIsAlive = alive;
	}

	@Override
	public TiledSprite getSprite() {
		// TODO Auto-generated method stub
		return tankSprite;
	}

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return _body;
	}

	@Override
	public FixtureDef getObjectFixtureDef() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doContact(IMapObject object) {
		// TODO Auto-generated method stub
		try {
			Log.i("Va cham", object.getType().name());
		} catch (Exception e) {
			Debug.d("Collsion", "Nothing to contact!");
		}
	}

	@Override
	public ObjectType getType() {
		// TODO Auto-generated method stub
		return ObjectType.PlayerTank;
	}
	
	public void SetType(ObjectType type) {
		_type = type;
	}

	public float GetX(){
		return _body.getTransform().getPosition().x;
	}
	
	public float GetY(){
		return _body.getTransform().getPosition().y;
	}



}