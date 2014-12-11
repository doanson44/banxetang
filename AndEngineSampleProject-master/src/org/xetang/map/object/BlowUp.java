package org.xetang.map.object;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.xetang.manager.GameManager;
import org.xetang.map.helper.BlastQueryCallback;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class BlowUp extends MapObject implements IBlowUp {

	protected IMapObject _ownObject;
	protected IMapObject _targetObject;
	protected BlastQueryCallback _inRangeBodies;

	public BlowUp(BlowUp blowUp) {
		super(blowUp);

		_sprite = new AnimatedSprite(blowUp.getX(), blowUp.getY(), blowUp
				.getSprite().getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(blowUp.getSprite().getWidth(), blowUp.getSprite()
				.getHeight());
		_sprite.setUserData(this);
		
		// attachChild(_sprite);
	}

	public BlowUp(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, float posX, float posY,
			float width) {
		this(objectFixtureDef, objectTextureRegion, posX, posY, width, width);
	}

	public BlowUp(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, float posX, float posY,
			float width, float height) {
		super(objectFixtureDef, objectTextureRegion, posX, posY, width, height,
				MapObjectFactory.Z_INDEX_BLOW_UP);
	}

	@Override
	protected void createSprite(TiledTextureRegion objectTextureRegion,
			float posX, float posY) {

		_sprite = new AnimatedSprite(posX, posY, objectTextureRegion,
				GameManager.Activity.getVertexBufferObjectManager(),
				DrawType.STATIC);
		_sprite.setSize(_cellWidth, _cellHeight);
	}

	@Override
	protected void createBody() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doContact(IMapObject object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOwnObject(IMapObject ownObject) {
		_ownObject = ownObject;
	}

	@Override
	public void setTargetObject(IMapObject targetObject) {
		_targetObject = targetObject;
	}

	@Override
	public void blowUpAtHere() {
		blowUpAt(getX(), getY());
	}

	@Override
	public void blowUpAt(Vector2 pos) {
		blowUpAt(pos.x, pos.y);
	}

	@Override
	public void blowUpAt(float posX, float posY) {
		this.setPosition(posX - getCellWidth() / 2, posY - getCellHeight() / 2);
	}
}
