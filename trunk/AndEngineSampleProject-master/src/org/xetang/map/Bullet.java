package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.tank.Tank;

import android.graphics.Point;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bullet extends MapObject {

	Tank _tank;
	int _speed; // pixel/s
	Point _position;
	Direction _direction;
	int _damage;
	
	public Bullet(Bullet bullet) {
		super(bullet);
	}

	public Bullet(float posX, float posY) {
		super(MapObjectFactory.getBulletFixtureDef(), MapObjectFactory
				.getBulletTextureRegion(),
				MapObjectFactory.BULLET_CELL_PER_MAP, posX, posY);

		CellWidth = GameManager.MAP_WIDTH
				/ MapObjectFactory.BULLET_CELL_PER_MAP;
		CellHeight = (int) (_sprite.getHeight() * (CellWidth / _sprite
				.getWidth()));
	}

	public Bullet(Tank tank, float posX, float posY) {

		this(posX, posY); setTank(tank);
	}




	@Override
	protected void createSprite(TiledTextureRegion objectTextureRegion,
			float posX, float posY) {

		_sprite = new TiledSprite(posX, posY, objectTextureRegion,
				GameManager.Context.getVertexBufferObjectManager());
		_sprite.setScale(CellWidth / _sprite.getWidth());
	}

	@Override
	protected void createBody() {

		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, _sprite,
				BodyType.DynamicBody, _objectFixtureDef);

		_body.setBullet(true);

		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				_sprite, _body, true, true));
	}

	public void setTank(Tank tank) {
		_tank = tank;
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Bullet;
	}

	
	@Override
	public MapObject clone() {
		return new Bullet(this);
	}
}