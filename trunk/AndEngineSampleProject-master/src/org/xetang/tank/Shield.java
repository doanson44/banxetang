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
import org.xetang.root.GameEntity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Shield extends GameEntity {

	protected AnimatedSprite	_shield;
	protected boolean			_isAlive = false;
	public Shield(float px, float py){
		_shield = new AnimatedSprite(px, py
				, (ITiledTextureRegion) MapObjectFactory2.getTexture("Protection"),GameManager.VertexBufferObject);
		_shield.animate(200);
		this.attachChild(_shield);
		GameManager.CurrentMap.attachChild(this);
		_isAlive = true;
	}
	
	
	public void KillSelf() {
		GameManager.CurrentMap.detachChild(this);
		_isAlive = false;
	}


	public boolean IsAlive() {
		// TODO Auto-generated method stub
		return _isAlive;
	}
	
	public AnimatedSprite GetSprite (){
		return _shield;
	}

}
