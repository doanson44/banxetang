package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.tank.Tank;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bullet extends MapObject implements IBullet {

	Tank _tank;
	int _damage;
	Direction _direction;
	Vector2 _speed;
	Vector2 _blowRadius;
	Vector2 _topPointUnit = new Vector2();

	public Bullet(Bullet bullet) {
		super(bullet);

		_tank = bullet._tank;
		_damage = bullet._damage;
		_direction = bullet._direction;
		_speed = bullet._speed;
		_blowRadius = bullet._blowRadius;
		_topPointUnit = bullet._topPointUnit;

		_sprite = new TiledSprite(bullet.getX(), bullet.getY(), bullet
				.getSprite().getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setScale(bullet.getSprite().getScaleX(), bullet.getSprite()
				.getScaleY());
		// _sprite.setSize(bullet.getSprite().getWidth(), bullet.getSprite()
		// .getHeight());

		attachChild(_sprite);
	}

	public Bullet(float posX, float posY) {
		super(MapObjectFactory.getBulletFixtureDef(), MapObjectFactory
				.getBulletTextureRegion(),
				MapObjectFactory.BULLET_CELL_PER_MAP, posX, posY,
				MapObjectFactory.Z_INDEX_BULLET);

		initSpecification(MapObjectFactory.NORMAL_BULLET_DAMAGE,
				MapObjectFactory.NORMAL_BULLET_SPEED,
				MapObjectFactory.NORMAL_BULLET_BLOW_RADIUS);

		_cellWidth = GameManager.MAP_WIDTH
				/ MapObjectFactory.BULLET_CELL_PER_MAP;
		_cellHeight = (int) (_sprite.getHeight() * (_cellWidth / _sprite
				.getWidth()));
	}

	public Bullet(Tank tank, float posX, float posY) {

		this(posX, posY);
		setTank(tank);
	}

	@Override
	public MapObject clone() {
		return new Bullet(this);
	}

	@Override
	protected void createSprite(TiledTextureRegion objectTextureRegion,
			float posX, float posY) {

		_sprite = new TiledSprite(posX, posY, objectTextureRegion,
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setScale(_cellWidth / _sprite.getWidth());
	}

	@Override
	protected void createBody() {

		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, _sprite,
				BodyType.DynamicBody, _objectFixtureDef);
		_body.setGravityScale(0);
		// _body.setLinearVelocity(0f, 10f);

		// _body.setBullet(true);
		_body.setUserData(this);

		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				_sprite, _body, true, true));
	}

	@Override
	public void doContact(IMapObject object) {

		Vector2 topPoint = getTopPoint();

		IBlowUp blast = (IBlowUp) MapObjectFactory.createObject(
				ObjectType.Blast, topPoint.x, topPoint.y);
		blast.setOwnObject(this);
		blast.setTargetObject(object);
		blast.blowUpAtHere();

	//	GameManager.CurrentMapManager.addBlast(blast);

		_sprite.setVisible(false);
		_body.setLinearVelocity(0f, 0f);
		DestroyHelper.add(this);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Bullet;
	}

	@Override
	public void initSpecification(int damage, Vector2 speed, Vector2 blowRadius) {
		_damage = damage;
		_speed = speed;
		_blowRadius = blowRadius;
	}

	public void setTank(Tank tank) {
		_tank = tank;
	}

	public int getDamage() {
		return _damage;
	}

	public Direction getDirection() {
		return _direction;
	}

	@Override
	public Vector2 getBlowRadius() {
		return _blowRadius;
	}

	@Override
	public Vector2 getTopPointUnit() {
		return _topPointUnit;
	}

	@Override
	public void readyToFire(Direction direction) {

		_direction = direction;
		_sprite.setRotation(CalcHelper.direction2Degrees(direction));
	}

	@Override
	public void beFired() {

		// if (loại xe tăng gì)

		Vector2 speedVector2 = null;

		switch (_direction) {
		case Up:
			speedVector2 = new Vector2(0f, -_speed.y);
			_topPointUnit.set(0f, -_cellHeight / 2);
			break;

		case Right:
			speedVector2 = new Vector2(_speed.x, 0f);
			_topPointUnit.set(_cellWidth / 2, 0f);
			break;

		case Down:
			speedVector2 = new Vector2(0f, _speed.y);
			_topPointUnit.set(0f, _cellHeight / 2);
			break;

		case Left:
			speedVector2 = new Vector2(-_speed.x, 0f);
			_topPointUnit.set(-_cellWidth / 2, 0f);
			break;

		default:
			break;
		}

		beFired(speedVector2);
	}

	public void beFired(Vector2 speedVector) {

		// Xét vị trí xe tăng hiện tại
		putToWorld(/* Vị trí X, Vị trí Y */);
		_body.setLinearVelocity(speedVector);
	}

	@Override
	public Vector2 getTopPoint() {
		float[] coord = _sprite.getSceneCenterCoordinates();

		return new Vector2(coord[0] + _topPointUnit.x, coord[1]
				+ _topPointUnit.y);
	}

}