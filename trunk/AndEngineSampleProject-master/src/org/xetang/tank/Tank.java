package org.xetang.tank;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
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


/**
 * 
 */
public class Tank extends GameEntity implements IGameController, IMapObject {

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
	int speed;				// tốc độ của xe tăng
	ObjectType _type;
	FixtureDef _ObjectFixtureDef;
	float _distinctMove = 10;
	public boolean isAlive() {
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
    	_ObjectFixtureDef =  PhysicsFactory
				.createFixtureDef(1, 0, 0);
    	CreateBody();
	}

	protected void CreateBody (){
		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, tankSprite,
				BodyType.StaticBody,_ObjectFixtureDef);
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
		  	float distinct = tankSprite.getX() - _distinctMove;
		  	for (float i = tankSprite.getX(); i >= tankSprite.getX() - speed ; i--) {
				if(_map.isPointValid(i, tankSprite.getY())){
					distinct = i+1;
					break;
				}
					
			}
	    	MoveXModifier modifier = new MoveXModifier(speed, tankSprite.getX(), distinct);
	    	tankSprite.registerEntityModifier(modifier);
	    }

	// di chuyển qua phải
	    @Override
	    public void onRight() {
	    	// TODO Auto-generated method stub
		  	Log.i("right", "right");
	    	MoveXModifier modifier = new MoveXModifier(speed, tankSprite.getX(), tankSprite.getX() + _distinctMove);
	    	tankSprite.registerEntityModifier(modifier);
	    }
	    
	    
	    // // di chuyển qua lên
	    @Override
	    public void onForward() {
	    	// TODO Auto-generated method stub
	    	float distinct = tankSprite.getY() - _distinctMove;
		  	for (float i = tankSprite.getY(); i >= tankSprite.getY() - speed ; i--) {
				if(_map.isPointValid(tankSprite.getX(), i)){
					distinct = i+1;
					break;
				}
			}
	    	MoveYModifier modifier = new MoveYModifier(speed, tankSprite.getY(), distinct);
	    	tankSprite.registerEntityModifier(modifier);
	    }
	   
	    /// di chuyển qua xuống
	    @Override
	    public void onBackward() {
	    	// TODO Auto-generated method stub
	    	float distinct = tankSprite.getY() + _distinctMove;
		  	for (float i = tankSprite.getY(); i <= tankSprite.getY() + speed ; i++) {
				if(_map.isPointValid(tankSprite.getX(), i)){
					distinct = i-1;
					break;
				}
			}
	    	MoveYModifier modifier = new MoveYModifier(speed, tankSprite.getY(),distinct);
	    	tankSprite.registerEntityModifier(modifier);
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
	public void setAlive(boolean alive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TiledSprite getSprite() {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}
	
	public void SetType(ObjectType type) {
		_type = type;
	}

	

}