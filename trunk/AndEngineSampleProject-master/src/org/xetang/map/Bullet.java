package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.math.MathConstants;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameManager.Direction;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.tank.Tank;

import android.graphics.Point;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bullet extends MapObject implements IBullet {

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

		_cellWidth = GameManager.MAP_WIDTH
				/ MapObjectFactory.BULLET_CELL_PER_MAP;
		_cellHeight = (int) (_sprite.getHeight() * (_cellWidth / _sprite
				.getWidth()));
	}

	public Bullet(Tank tank, float posX, float posY) {

		this(posX, posY); setTank(tank);
	}

	@Override
	public MapObject clone() {
		return new Bullet(this);
	}



	@Override
	protected void createSprite(TiledTextureRegion objectTextureRegion,
			float posX, float posY) {

		_sprite = new TiledSprite(posX, posY, objectTextureRegion,
				GameManager.Context.getVertexBufferObjectManager());
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

	public void setTank(Tank tank) {
		_tank = tank;
	}

	@Override
	public void readyToFire(Direction direction) {

		float rotationValue = direction.ordinal() * MathConstants.PI_HALF
				* MathConstants.RAD_TO_DEG;

		_sprite.setRotation(rotationValue);
		// _sprite.setRotationCenter(0.5f, 0.5f);
		// _sprite.setRotationCenterX(rotationValue);
		// _sprite.setRotationCenterY(rotationValue);
		_direction = direction;
	}

	@Override
	public void beFired() {

		// if (loại xe tăng gì)

		Vector2 speedVector2 = null;

		switch (_direction) {
		case Up:
			speedVector2 = new Vector2(0f,
					-GameManager.NORMAL_BULLET_SPPED_HEIGHT);
			break;

		case Right:
			speedVector2 = new Vector2(GameManager.NORMAL_BULLET_SPPED_WIDTH,
					0f);
			break;

		case Down:
			speedVector2 = new Vector2(0f,
					GameManager.NORMAL_BULLET_SPPED_HEIGHT);
			break;

		case Left:
			speedVector2 = new Vector2(-GameManager.NORMAL_BULLET_SPPED_WIDTH,
					0f);
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
	public void doContact(IMapObject object) {
		_sprite.setVisible(false);
		_body.setLinearVelocity(0f, 0f);
		DestroyHelper.add(this);
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Bullet;
	}
}