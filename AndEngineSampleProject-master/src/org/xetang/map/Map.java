package org.xetang.map;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.map.model.MapObjectBlockDTO;
import org.xetang.map.model.StageDTO;
import org.xetang.map.model.StageObjectDTO;
import org.xetang.root.GameEntity;
import org.xetang.tank.Tank;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Map extends GameEntity {

	List<Item> mItems = new ArrayList<Item>();
	List<Item> mItemRemove = new ArrayList<Item>();
	List<Tank> mEnermyTanks = new ArrayList<Tank>();
	List<Tank> mPlayerTanks = new ArrayList<Tank>();
	int mICurrentStage; // Chỉ số của màn chơi

	Entity _layerBase;
	Entity _layerBullet;
	Entity _layerBush;
	Entity _layerBlast;

	public Map(int iCurrentStage, StageDTO stage) {
		mICurrentStage = iCurrentStage;

		initLayers();
		loadMapData(stage);
		createBorders();
		createListeners();
	}

	private void initLayers() {
		_layerBase = new Entity();
		_layerBase.setZIndex(MapObjectFactory.Z_INDEX_DEFAULT);
		attachChild(_layerBase);

		_layerBullet = new Entity();
		_layerBullet.setZIndex(MapObjectFactory.Z_INDEX_BULLET);
		attachChild(_layerBullet);

		_layerBush = new Entity();
		_layerBush.setZIndex(MapObjectFactory.Z_INDEX_BUSH);
		attachChild(_layerBush);

		_layerBlast = new Entity();
		_layerBlast.setZIndex(MapObjectFactory.Z_INDEX_BLAST);
		attachChild(_layerBlast);

		sortChildren(true);
	}

	public void loadMapData(StageDTO stage) {

		Item bomb = new Bomb(this);
		Item clock = new Clock(this);
		Item Helmet = new Helmet(this);
		Item shovel = new Shovel(this);
		Item clock2 = new Clock(this);
		
		mItems.add(bomb);
		mItems.add(clock);
		mItems.add(Helmet);
		mItems.add(shovel);
		mItems.add(clock2);
		
		for (Item item : mItems) {
			this.attachChild(item.getSprite());
		}
		
		List<StageObjectDTO> objects = stage.getObjects();

		StageObjectDTO stageObject;
		MapObjectBlockDTO objectsBlock;
		for (int i = 0; i < objects.size(); i++) {
			stageObject = objects.get(i);

			for (int j = 0; j < stageObject.getAreas().size(); j++) {

				objectsBlock = MapObjectFactory.createObjectBlock(ObjectType
						.values()[stageObject.getId()], stageObject.getAreas()
						.get(j));

				attachBlock(objectsBlock);
			}
		}
	}

	private void attachBlock(MapObjectBlockDTO objectsBlock) {

		List<IMapObject> objects = objectsBlock.getObjectsBlock();
		IMapObject object;
		for (int i = 0; i < objects.size(); i++) {
			object = objects.get(i);

			object.transform(getX(), getY());
			object.putToWorld();

			attachToLayer(object);
		}
	}

	private void attachToLayer(IMapObject object) {
		if (object.getType() != ObjectType.Bush) {
			_layerBase.attachChild((IEntity) object);
		} else {
			_layerBush.attachChild((IEntity) object);
		}
	}

	private void createBorders() {
		Rectangle ground = new Rectangle(-GameManager.BORDER_WIDTH,
				GameManager.MAP_HEIGHT, GameManager.MAP_WIDTH
						+ GameManager.BORDER_WIDTH * 2,
				GameManager.BORDER_WIDTH,
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle left = new Rectangle(-GameManager.BORDER_WIDTH, 0,
				GameManager.BORDER_WIDTH, GameManager.MAP_HEIGHT,
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle roof = new Rectangle(-GameManager.BORDER_WIDTH,
				-GameManager.BORDER_WIDTH, GameManager.MAP_WIDTH
						+ GameManager.BORDER_WIDTH * 2,
				GameManager.BORDER_WIDTH,
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle right = new Rectangle(GameManager.MAP_WIDTH, 0,
				GameManager.BORDER_WIDTH, GameManager.MAP_HEIGHT,
				GameManager.Activity.getVertexBufferObjectManager());

		FixtureDef borderFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f,
				0f);

		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, ground,
				BodyType.StaticBody, borderFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, roof,
				BodyType.StaticBody, borderFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, left,
				BodyType.StaticBody, borderFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, right,
				BodyType.StaticBody, borderFixtureDef);

		attachChild(ground);
		attachChild(left);
		attachChild(roof);
		attachChild(right);
	}

	private void createListeners() {

		GameManager.Engine.registerUpdateHandler(DestroyHelper.getInstance());

		ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {

				IMapObject objectA = null;
				IMapObject objectB = null;

				try {
					objectA = (MapObject) contact.getFixtureA().getBody()
							.getUserData();
				} catch (Exception e) {
					Debug.d("Collison", "Collide with something else!");
				}

				try {
					objectB = (MapObject) contact.getFixtureB().getBody()
							.getUserData();
				} catch (Exception e) {
					Debug.d("Collison", "Collide with something else!");
				}

				if (objectA != null) {
					objectA.doContact(objectB);
				}

				if (objectB != null) {
					objectB.doContact(objectA);
				}
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub

			}

			/*
			 * This is called after a contact is updated. This allows you to
			 * inspect a contact before it goes to the solver. If you are
			 * careful, you can modify the contact manifold (e.g. disable
			 * contact). A copy of the old manifold is provided so that you can
			 * detect changes. Note: this is called only for awake bodies. Note:
			 * this is called even when the number of contact points is zero.
			 * Note: this is not called for sensors. Note: if you set the number
			 * of contact points to zero, you will not get an EndContact
			 * callback. However, you may get a BeginContact callback the next
			 * step.
			 */

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			/*
			 * This lets you inspect a contact after the solver is finished.
			 * This is useful for inspecting impulses. Note: the contact
			 * manifold does not include time of impact impulses, which can be
			 * arbitrarily large if the sub-step is small. Hence the impulse is
			 * provided explicitly in a separate data structure. Note: this is
			 * only called for contacts that are touching, solid, and awake.
			 */

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}
		};

		GameManager.PhysicsWorld.setContactListener(contactListener);
	}

	public void Update(float pSecondsElapsed) {

		UpdateItem(pSecondsElapsed);
	}

	private void UpdateItem(float pSecondsElapsed) {
		for (Item item : mItems) {
			if (item.isAlive())
				item.update(pSecondsElapsed);
			else {
				mItemRemove.add(item);
				this.detachChild(item.GetSprite());
			}
		}
		for (Item item : mItemRemove) {

			mItems.remove(item);
		}
		mItemRemove.clear();
	}

	public boolean isPointValid(float x, float y) {
		// Kiểm tra xem điểm(x,y) hiện tại thuộc ô nào của bản đồ,
		// và ô đó xe tăng có thể đi vào?
		// ...
		return false;
	}

	public int[] pointToCoordinate(float x, float y) {
		// Xác định xem điểm(x,y) thuộc ô nào trên bản đồ?
		// ...

		return new int[] { 0, 0 };
	}

	public List<Tank> getTotalEnermyTanks() {
		return mEnermyTanks;
	}

	public void addEnermyTank(Tank enermyTank) {
		mEnermyTanks.add(enermyTank);
	}

	public int getTotalPlayerTanks() {
		return mPlayerTanks.size();
	}

	public boolean isEagleAlive() {
		/*
		 * Kiểm tra Đại bàng còn sống không ?
		 */

		return true; // dummy
	}

	// Dành cho test
	public void addBullet(IBullet bullet) {
		_layerBullet.attachChild((IEntity) bullet);
	}

	// Dành cho test
	public void addBlast(IBlowUp blast) {
		_layerBlast.attachChild((IEntity) blast);
	}

	public void MakeStoneWallFortress() {
		// TODO Auto-generated method stub

	}


	public void DestroyAllEnermy() {
		// TODO Auto-generated method stub

	}

	public void FreezeTime() {
		// TODO Auto-generated method stub

	}

	public void addBlowUp(IBlowUp blast) {
		// TODO Auto-generated method stub
		_layerBlast.attachChild((IEntity)blast);
	}

}