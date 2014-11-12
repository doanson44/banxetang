package org.xetang.map;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.root.GameEntity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class MapObject extends GameEntity {

	public enum ObjectType {
		Eagle, BrickWall, SteelWall, Bush, Water, Bullet
	};

	public int CellWidth;
	public int CellHeight;

	protected Sprite _sprite = null;
	protected Body _body = null;
	protected FixtureDef _objectFixtureDef = null;

	public MapObject(MapObject object) {
		CellHeight = object.CellHeight;
		CellWidth = object.CellWidth;

		_sprite = new Sprite(object.getX(), object.getY(),
				object._sprite.getTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(object._sprite.getWidth(), object._sprite.getHeight());
		attachChild(_sprite);

		_objectFixtureDef = object._objectFixtureDef;
		_body = null;
	}

	public abstract MapObject clone();

	public MapObject(FixtureDef objectFixtureDef,
			TiledTextureRegion objectTextureRegion, int piecePerMap,
			float posX, float posY) {

		// setX(posX);
		// setY(posY);
		CellWidth = GameManager.MAP_WIDTH / piecePerMap;
		CellHeight = CellWidth / 4 * 3;
		_objectFixtureDef = objectFixtureDef;

		createSprite(objectTextureRegion, posX, posY);
		attachChild(_sprite);
	}

	protected void createSprite(TiledTextureRegion objectTextureRegion,
			float posX, float posY) {

		_sprite = new TiledSprite(posX, posY, objectTextureRegion,
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(CellWidth, CellHeight);
	}

	protected void createBody() {

		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, _sprite,
				BodyType.StaticBody, _objectFixtureDef);
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

		if (_body != null) {
			_body.setTransform(posX, posY, _body.getAngle());
		}
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

	public Sprite getSprite() {
		return _sprite;
	}

	public void setSprite(Sprite sprite) {
		this._sprite = sprite;
	}

	public Body getBody() {
		return _body;
	}

	public void setBody(Body body) {
		this._body = body;
	}

	public abstract ObjectType getType();
}