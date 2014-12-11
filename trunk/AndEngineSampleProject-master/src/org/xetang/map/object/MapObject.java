package org.xetang.map.object;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.root.GameEntity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class MapObject extends GameEntity implements IMapObject {

	protected float _cellWidth;
	protected float _cellHeight;

	protected boolean _alive = true;

	protected TiledSprite _sprite = null;
	protected Body _body = null;
	protected FixtureDef _objectFixtureDef = null;

	public MapObject(IMapObject object) {
		_cellWidth = object.getCellWidth();
		_cellHeight = object.getCellHeight();
		_objectFixtureDef = object.getObjectFixtureDef();
		_body = null;

		this.setZIndex(((Entity) object).getZIndex());
	}

	public MapObject(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, float posX, float posY,
			float width) {
		this(objectFixtureDef, objectTextureRegion, posX, posY, width, width);
	}

	public MapObject(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, float posX, float posY,
			float width, float height) {
		this(objectFixtureDef, objectTextureRegion, posX, posY, width, height,
				MapObjectFactory.Z_INDEX_CONSTRUCTION);
	}

	public MapObject(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, float posX, float posY,
			float width, int zIndex) {
		this(objectFixtureDef, objectTextureRegion, posX, posY, width, width,
				zIndex);
	}

	public MapObject(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, float posX, float posY,
			float width, float height, int zIndex) {

		if (objectFixtureDef != null) {
			_objectFixtureDef = objectFixtureDef;
		}

		// setX(posX);
		// setY(posY);
		_cellWidth = width;
		_cellHeight = height;

		createSprite(objectTextureRegion, posX, posY);
		this.setZIndex(zIndex);
		// attachChild(_sprite);
	}

	public abstract IMapObject clone();

	protected void createSprite(TiledTextureRegion objectTextureRegion,
			float posX, float posY) {

		_sprite = new TiledSprite(posX, posY, objectTextureRegion,
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(_cellWidth, _cellHeight);

		_sprite.setUserData(this);
	}

	protected void createBody() {

		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, _sprite,
				BodyType.StaticBody, _objectFixtureDef);

		_body.setUserData(this);
	}

	public void putToWorld() {

		if (_body == null) {
			createBody();
		}
	}

	public void putToWorld(float posX, float posY) {

		setPosition(posX, posY);
		putToWorld();
	}

	public void setPosition(float posX, float posY) {

		// setX(posX);
		// setY(posY);
		_sprite.setPosition(posX, posY);
	}

	public float getX() {
		return _sprite.getX();
	}

	public float getY() {
		return _sprite.getY();
	}

	public void transform(float x, float y) {
		setPosition(_sprite.getX() + x, _sprite.getY() + y);
	}

	public float getCellWidth() {
		return _cellWidth;
	}

	public float getCellHeight() {
		return _cellHeight;
	}

	public synchronized boolean isAlive() {
		return _alive;
	}

	public synchronized void setAlive(boolean alive) {
		this._alive = alive;
	}

	public TiledSprite getSprite() {
		return _sprite;
	}

	public Body getBody() {
		return _body;
	}

	public FixtureDef getObjectFixtureDef() {
		return _objectFixtureDef;
	}
}