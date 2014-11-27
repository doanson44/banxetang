package org.xetang.tank;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.manager.TankManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.map.IMapObject;
import org.xetang.map.Map;
import org.xetang.map.MapObject;
import org.xetang.map.MapObjectFactory2;
import org.xetang.map.MapObjectFactory.ObjectType;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Shield {

	protected AnimatedSprite	_shield;
	protected Body				_shieldBody;
	protected Map				_map;
	protected boolean			_isAlive = false;
	public Shield(float px, float py, Map map){
		_shield = new AnimatedSprite(px, py
				, (ITiledTextureRegion) MapObjectFactory2.getTexture("Protection"),GameManager.VertexBufferObject);
		_shield.animate(200);
		_map = map;
		_map.attachChild(_shield);
	//	CreateBody();
		_isAlive = true;
	}
	
	protected void CreateBody(){
		FixtureDef _shieldFixtureDef = PhysicsFactory
				.createFixtureDef(1, 0, 0,true);
		_shieldBody = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, _shield,
				BodyType.DynamicBody,_shieldFixtureDef);
		_shieldBody.setGravityScale(0);
		_shieldBody.setFixedRotation(true);
		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				_shield, _shieldBody, true, true));
	}
	
	public void SetShieldVelocity(float x, float y){
			_shieldBody.setLinearVelocity(x, y);
	}

	public boolean IsAlive() {
		// TODO Auto-generated method stub
		return _isAlive;
	}
	
	public AnimatedSprite GetSprite (){
		return _shield;
	}

}
