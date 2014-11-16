package org.xetang.tank;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.controller.Controller;
import org.xetang.controller.IGameController;
import org.xetang.manager.GameManager;
import org.xetang.manager.TankManager;
import org.xetang.map.Bullet;
import org.xetang.map.Map;
import org.xetang.root.GameEntity;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


/**
 * 
 */
public class Tank extends GameEntity implements IUpdateHandler, IGameController {

	Controller mController;
	
	Bullet 	mBullet;
	boolean mIsFiring; 		//Xe tăng có đang bắn đạn hay không?
	int 	mDirection; 	//Hướng của xe tăng
	int 	mLevel;			//Cấp độ của xe tăng
	boolean mIsAlive;
	boolean mIsFreeze = false;
	Body body;
	Map _map;
	TiledSprite tankSprite;
	int pX, pY;				
	int speed;				// tốc độ của xe tăng
		
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
    	CreateBody();
	}

	protected void CreateBody (){
		body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, tankSprite,
				BodyType.StaticBody, PhysicsFactory
				.createFixtureDef(0, 0, 0));
		
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
		  	float distinct = tankSprite.getX() - speed;
		  	for (float i = tankSprite.getX(); i >= tankSprite.getX() - speed ; i--) {
				if(_map.isPointValid(i, tankSprite.getY())){
					distinct = i+1;
					break;
				}
					
			}
	    	MoveXModifier modifier = new MoveXModifier(speed, tankSprite.getX(), distinct - 100);
	    	tankSprite.registerEntityModifier(modifier);
	    }

	// di chuyển qua phải
	    @Override
	    public void onRight() {
	    	// TODO Auto-generated method stub
	    	float distinct = tankSprite.getX() + speed;
		  	for (float i = tankSprite.getX(); i <= tankSprite.getX() + speed ; i++) {
				if(_map.isPointValid(i, tankSprite.getY())){
					distinct = i-1;
					break;
				}
					
			}
	    	MoveXModifier modifier = new MoveXModifier(speed, tankSprite.getX(), distinct);
	    	tankSprite.registerEntityModifier(modifier);
	    }
	    
	    
	    // // di chuyển qua lên
	    @Override
	    public void onForward() {
	    	// TODO Auto-generated method stub
	    	float distinct = tankSprite.getY() - speed;
		  	for (float i = tankSprite.getY(); i >= tankSprite.getY() - speed ; i--) {
				if(_map.isPointValid(tankSprite.getX(), i)){
					distinct = i+1;
					break;
				}
			}
	    	MoveYModifier modifier = new MoveYModifier(speed, tankSprite.getY(), distinct -100);
	    	tankSprite.registerEntityModifier(modifier);
	    }
	   
	    /// di chuyển qua xuống
	    @Override
	    public void onBackward() {
	    	// TODO Auto-generated method stub
	    	float distinct = tankSprite.getY() + speed;
		  	for (float i = tankSprite.getY(); i <= tankSprite.getY() + speed ; i++) {
				if(_map.isPointValid(tankSprite.getX(), i)){
					distinct = i-1;
					break;
				}
			}
	    	MoveYModifier modifier = new MoveYModifier(speed, tankSprite.getY(),distinct + 100);
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
	

	

}