package org.xetang.tank;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.item.MapObjectFactory2;
import org.xetang.map.object.MapObjectFactory.ObjectLayer;
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
		_shield.setSize(GameManager.LARGE_CELL_SIZE,
				GameManager.LARGE_CELL_SIZE);
		_shield.animate(50);

		GameManager.CurrentMap.addObject(_shield, ObjectLayer.MOVING);
		_isAlive = true;
	}

	public void KillSelf() {
		_isAlive = false;
		GameManager.Context.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				_shield.setVisible(false);
				_shield.detachSelf();
				_shield.dispose();
			}
		});
		
	}

	public boolean IsAlive() {
		return _isAlive;
	}

	public AnimatedSprite GetSprite() {
		return _shield;
	}

}
