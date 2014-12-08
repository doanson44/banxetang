package org.xetang.tank;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory2;
import org.xetang.map.MapObjectFactory.ObjectLayer;
import org.xetang.root.GameEntity;

public class Shield extends GameEntity {

	protected AnimatedSprite _shield;
	protected boolean _isAlive = false;
	public int TimeSurvive = 0;

	public Shield(float px, float py) {
		_shield = new AnimatedSprite(px, py,
				(ITiledTextureRegion) MapObjectFactory2
						.getTexture("Protection"),
				GameManager.VertexBufferObject);
		_shield.animate(200);
		this.attachChild(_shield);
		GameManager.CurrentMap.addObject(this, ObjectLayer.Moving);
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

	public AnimatedSprite GetSprite() {
		return _shield;
	}

}
