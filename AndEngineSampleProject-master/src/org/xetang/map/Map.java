package org.xetang.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.debug.Debug;
import org.xetang.controller.Bot;
import org.xetang.manager.GameItemManager;
import org.xetang.manager.GameManager;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.map.model.MapObjectBlockDTO;
import org.xetang.map.model.StageDTO;
import org.xetang.map.model.StageObjectDTO;
import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObjectFactory;
import org.xetang.map.object.MapObjectFactory.ObjectLayer;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.root.GameEntity;
import org.xetang.tank.Flicker;
import org.xetang.tank.Tank;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Map extends GameEntity implements IUpdateHandler {

	List<Tank> mEnermyTanks = new ArrayList<Tank>();
	List<Tank> mPlayerTanks = new ArrayList<Tank>();
	int mICurrentStage; // Chỉ số của màn chơi

	java.util.Map<ObjectLayer, Entity> _layerMap = new HashMap<ObjectLayer, Entity>(
			10);

	RightMenu _RightMenu;

	public Map(int iCurrentStage, StageDTO stage) {
		mICurrentStage = iCurrentStage;

		initLayers();
		loadMapData(stage);
		createBorders();
		createListeners();
	}

	private void initLayers() {

		Entity layer = new Entity();
		layer.setZIndex(MapObjectFactory.Z_INDEX_CONSTRUCTION);
		attachChild(layer);
		_layerMap.put(ObjectLayer.Construction, layer);

		layer = new Entity();
		layer.setZIndex(MapObjectFactory.Z_INDEX_MOVING);
		attachChild(layer);
		_layerMap.put(ObjectLayer.Moving, layer);

		layer = new Entity();
		layer.setZIndex(MapObjectFactory.Z_INDEX_WRAPPER);
		attachChild(layer);
		_layerMap.put(ObjectLayer.Wrapper, layer);

		layer = new Entity();
		layer.setZIndex(MapObjectFactory.Z_INDEX_BLOW_UP);
		attachChild(layer);
		_layerMap.put(ObjectLayer.BlowUp, layer);

		sortChildren(true);
	}

	public void loadMapData(StageDTO stage) {

		this.attachChild(GameItemManager.getInstance().CreateItem(
				ObjectType.TankItem));
		this.attachChild(GameItemManager.getInstance().CreateItem(
				ObjectType.TankItem));
		this.attachChild(GameItemManager.getInstance().CreateItem(
				ObjectType.TankItem));
		this.attachChild(GameItemManager.getInstance().CreateItem(
				ObjectType.TankItem));

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
			addObject((IEntity) object, ObjectLayer.Construction);
		} else {
			addObject((IEntity) object, ObjectLayer.Wrapper);
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

	public void InitRightMenu(int TotalEnermytank) {
		_RightMenu = new RightMenu(GameManager.MAP_WIDTH + 20, 20, this, 10);

		attachChild(_RightMenu);
		_RightMenu.RemoveLastItem();
	}

	private void createListeners() {

		GameManager.Engine.registerUpdateHandler(DestroyHelper.getInstance());

		ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {

				IMapObject objectA = null;
				IMapObject objectB = null;

				try {
					objectA = (IMapObject) contact.getFixtureA().getBody()
							.getUserData();
				} catch (Exception e) {
					Debug.d("Collison", "Collide with something else!");
				}

				try {
					objectB = (IMapObject) contact.getFixtureB().getBody()
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
		UpdateTank(pSecondsElapsed);
	}

	private void UpdateTank(float pSecondsElapsed) {
		for (int i = 0; i < mPlayerTanks.size(); i++) {
			if (!mPlayerTanks.get(i).isAlive()) {
				mPlayerTanks.remove(i);
			} else
				mPlayerTanks.get(i).Update(pSecondsElapsed);
		}
		for (int i = 0; i < mEnermyTanks.size(); i++) {
			if (!mEnermyTanks.get(i).isAlive())
				mEnermyTanks.remove(i);
			else
				mEnermyTanks.get(i).Update(pSecondsElapsed);
		}
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

	public List<Tank> getEnermyTanks() {
		return mEnermyTanks;
	}

	public List<Tank> getPlayerTanks() {
		return mPlayerTanks;
	}

	public void RemoveEnermyTank(Tank tank) {
		mEnermyTanks.remove(tank);
	}

	public void RemovePlayerTank(Tank tank) {
		// TODO Auto-generated method stub
		mPlayerTanks.remove(tank);
	}

	public void addPlayerTank(Tank playerTank) {
		mPlayerTanks.add(playerTank);

		addObject(playerTank, ObjectLayer.Moving);
		//this.attachChild(playerTank);

	}

	public void addEnermyTank(Tank enermyTank) {
		AddTank(enermyTank);
		// mEnermyTanks.add(enermyTank);
	}

	public void AddTank(Tank tank) {
		CreateFlicker(tank);
	}

	public void CreateFlicker(Tank tank) {
		Flicker f = new Flicker(tank.getX(), tank.getY());
		f.Animate();
		f.SetTank(tank);
		GameManager.Scene.registerUpdateHandler(f);

		addObject(f.GetSprite(), ObjectLayer.Moving);
		//this.attachChild(f.GetSprite());
	}

	public void AddEnermyTankToList(Tank tank) {
		mEnermyTanks.add(tank);

		addObject(tank, ObjectLayer.Moving);
		// this.attachChild(tank);

		GameManager.CurrentMapManager.AddBot(new Bot(tank));
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

	public void addObject(IEntity object, ObjectLayer layer) {
		// TODO Auto-generated method stub
		_layerMap.get(layer).attachChild(object);
	}
}