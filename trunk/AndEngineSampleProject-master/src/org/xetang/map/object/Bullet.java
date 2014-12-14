package org.xetang.map.object;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.map.object.MapObjectFactory.ObjectLayer;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.tank.Tank;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bullet extends MapObject implements IBullet {

	IMapObject _tank;
	int _damage;
	Direction _direction;
	float _speed;
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
		_sprite.setSize(bullet.getSprite().getWidth(), bullet.getSprite()
				.getHeight());
		_sprite.setRotationCenter(bullet.getSprite().getRotationCenterX(),
				bullet.getSprite().getRotationCenterY());
		_sprite.setUserData(this);

		// attachChild(_sprite);
	}

	public Bullet() {
		this(0f, 0f);
	}

	public Bullet(float posX, float posY) {
		super(MapObjectFactory.getFixtureDef(ObjectType.BULLET),
				MapObjectFactory.getTextureRegion(ObjectType.BULLET), posX,
				posY, MapObjectFactory.getBulletSize().x, MapObjectFactory
						.getBulletSize().y, MapObjectFactory.Z_INDEX_MOVING);

		_sprite.setRotationCenter(MapObjectFactory.getBulletSize().x / 2f,
				MapObjectFactory.getBulletSize().y / 2f);

		initSpecification(MapObjectFactory.NORMAL_BULLET_DAMAGE,
				MapObjectFactory.NORMAL_BULLET_SPEED,
				MapObjectFactory.NORMAL_BULLET_BLOW_RADIUS);
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

		if (object == _tank) {
			return;
		}

		if (object != null) {
			// tank địch bắn trúng tank của ngư�?i chơi
			if (object.getType() == ObjectType.PLAYER_TANK
					&& _tank.getType() == ObjectType.ENEMY_TANK) {
				Tank tank = (Tank) object;
				if (tank.getShield() == null) {
					tank.KillSelf();
				}
			}
			// tank của ngư�?i chơi bắn trúng tank địch
			else if (object.getType() == ObjectType.ENEMY_TANK
					&& _tank.getType() == ObjectType.PLAYER_TANK) {

				Tank tank = (Tank) object;
				if (tank.BeFire()) {
					GameManager.CurrentMapManager.AddTankKill(tank);
					tank.KillSelf();
				}
			}
		}

		doBlast(object);
		_sprite.setVisible(false);
		_body.setLinearVelocity(0f, 0f);
		DestroyHelper.add(this);
	}

	private void doBlast(IMapObject object) {
		Vector2 topPoint = getTopPoint();

		IBlowUp blast = (IBlowUp) MapObjectFactory.createObject(
				ObjectType.BLAST, topPoint.x, topPoint.y);
		blast.setOwnObject(this);
		blast.setTargetObject(object);
		blast.blowUpAtHere();

		GameManager.CurrentMap.addObject(blast, ObjectLayer.BLOW_UP);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.BULLET;
	}

	@Override
	public void initSpecification(int damage, float speed, Vector2 blowRadius) {
		_damage = damage;
		_speed = speed;
		_blowRadius = blowRadius;
	}

	public void setTank(Tank tank) {
		_tank = tank;
	}

	public IMapObject GetTank() {
		return _tank;
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
	public void readyToFire(Direction direction) {

		_direction = direction;
		_sprite.setRotation(CalcHelper.direction2Degrees(direction));
	}

	@Override
	public void beFired() {

		// if (loại xe tăng gì)

		Vector2 speedVector2;
		if (_direction.ordinal() % 2 == 0) { // Up or Down
			speedVector2 = new Vector2(0f, -_speed);
			_topPointUnit.set(0f, -_cellHeight / 2);

		} else { // Left or Right
			speedVector2 = new Vector2(0f, -_speed);
			_topPointUnit.set(0f, -_cellWidth / 2);
		}

		speedVector2.rotate(CalcHelper.direction2Degrees(_direction));
		_topPointUnit.rotate(CalcHelper.direction2Degrees(_direction));

		// switch (_direction) {
		// case Up:
		// speedVector2 = new Vector2(0f, -_speed.y);
		// _topPointUnit.set(0f, -_cellHeight / 2);
		// break;
		//
		// case Right:
		// speedVector2 = new Vector2(_speed.x, 0f);
		// _topPointUnit.set(_cellWidth / 2, 0f);
		// break;
		//
		// case Down:
		// speedVector2 = new Vector2(0f, _speed.y);
		// _topPointUnit.set(0f, _cellHeight / 2);
		// break;
		//
		// case Left:
		// speedVector2 = new Vector2(-_speed.x, 0f);
		// _topPointUnit.set(-_cellWidth / 2, 0f);
		// break;
		//
		// default:
		// break;
		// }

		beFired(speedVector2);
	}

	public void beFired(Vector2 speedVector) {

		// Xét vị trí xe tăng hiện tại
		putToWorld(/* Vị trí X, Vị trí Y */);
		_body.setLinearVelocity(speedVector);
		// GameManager.getMusic("fire").play();
	}

	@Override
	public Vector2 getTopPoint() {
		float[] coord = _sprite.getSceneCenterCoordinates();

		return new Vector2(coord[0] + _topPointUnit.x, coord[1]
				+ _topPointUnit.y);
	}

}