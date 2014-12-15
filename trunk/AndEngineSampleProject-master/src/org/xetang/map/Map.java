﻿package org.xetang.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.manager.TankManager;
import org.xetang.map.helper.DestroyHelper;
import org.xetang.map.model.MapObjectBlockDTO;
import org.xetang.map.model.StageDTO;
import org.xetang.map.model.StageObjectDTO;
import org.xetang.map.model.XMLLoader;
import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObjectFactory;
import org.xetang.map.object.MapObjectFactory.ObjectLayer;
import org.xetang.map.object.MapObjectFactory.ObjectType;
import org.xetang.root.GameEntity;
import org.xetang.tank.Tank;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Map extends GameEntity implements IUpdateHandler {

	List<Tank> mEnemyTanks = new ArrayList<Tank>();
	List<Tank> mPlayerTanks = new ArrayList<Tank>();
	int mICurrentStage; // Chỉ số của màn chơi
	boolean mIsEagleAlive = true;

	java.util.Map<ObjectLayer, IEntity> _layerMap = new HashMap<ObjectLayer, IEntity>();

	public Map(int iCurrentStage, StageDTO stage) {
		mICurrentStage = iCurrentStage;

		initLayers();
		loadMapData(stage);
		createBorders();
		createListeners();
	}

	private void initLayers() {

		createConstructionLayer();

		Entity layer = new Entity();
		layer.setZIndex(MapObjectFactory.Z_INDEX_MOVING);
		attachChild(layer);
		_layerMap.put(ObjectLayer.MOVING, layer);

		layer = new Entity();
		layer.setZIndex(MapObjectFactory.Z_INDEX_WRAPPER);
		layer.setChildrenIgnoreUpdate(true);
		attachChild(layer);
		_layerMap.put(ObjectLayer.WRAPPER, layer);

		layer = new Entity();
		layer.setZIndex(MapObjectFactory.Z_INDEX_BLOW_UP);
		attachChild(layer);
		_layerMap.put(ObjectLayer.BLOW_UP, layer);

		sortChildren(true);
	}

	private void createConstructionLayer() {

		SpriteGroup spriteGroupLayer = new SpriteGroup(
				MapObjectFactory.getBitmapTextureAtlas(),
				MapObjectFactory.MAX_OBJECT_COUNT,
				GameManager.Activity.getVertexBufferObjectManager()) {

			@Override
			protected boolean onUpdateSpriteBatch() {
				return false;
			}

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (super.onUpdateSpriteBatch()) {
					submit();
				}
			};
		};

		spriteGroupLayer.setZIndex(MapObjectFactory.Z_INDEX_CONSTRUCTION);
		attachChild(spriteGroupLayer);
		_layerMap.put(ObjectLayer.CONSTRUCTION, spriteGroupLayer);
	}

	public void loadMapData(StageDTO stage) {

		// addObject(GameItemManager.getInstance().CreateItem(ObjectType.STAR),ObjectLayer.WRAPPER);
		// addObject(GameItemManager.getInstance().CreateItem(ObjectType.STAR),ObjectLayer.WRAPPER);
		// addObject(GameItemManager.getInstance().CreateItem(ObjectType.STAR),ObjectLayer.WRAPPER);

		List<StageObjectDTO> objects = stage.getObjects();

		StageObjectDTO stageObject;
		MapObjectBlockDTO objectsBlock;
		for (int i = 0; i < objects.size(); i++) {
			stageObject = objects.get(i);

			for (int j = 0; j < stageObject.getAreas().size(); j++) {

				objectsBlock = MapObjectFactory.createObjectBlock(
						XMLLoader.getObjectFromID(stageObject.getId()),
						stageObject.getAreas().get(j));

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

		if (object.getType() != ObjectType.BUSH) {
			addObject(object, ObjectLayer.CONSTRUCTION);
		} else {
			addObject(object, ObjectLayer.WRAPPER);
		}
	}

	private void createBorders() {
		Rectangle ground = new Rectangle(-GameManager.BORDER_THICK,
				GameManager.MAP_SIZE, GameManager.MAP_SIZE
						+ GameManager.BORDER_THICK * 2,
				GameManager.BORDER_THICK,
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle left = new Rectangle(-GameManager.BORDER_THICK, 0,
				GameManager.BORDER_THICK, GameManager.MAP_SIZE,
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle roof = new Rectangle(-GameManager.BORDER_THICK,
				-GameManager.BORDER_THICK, GameManager.MAP_SIZE
						+ GameManager.BORDER_THICK * 2,
				GameManager.BORDER_THICK,
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle right = new Rectangle(GameManager.MAP_SIZE, 0,
				GameManager.BORDER_THICK, GameManager.MAP_SIZE,
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

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

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
				removePlayerTank(i);
			} else
				mPlayerTanks.get(i).Update(pSecondsElapsed);
		}
		for (int i = 0; i < mEnemyTanks.size(); i++) {
			if (!mEnemyTanks.get(i).isAlive())
				removeEnemyTank(i);
			else
				mEnemyTanks.get(i).Update(pSecondsElapsed);
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

	public List<Tank> getEnemyTanks() {
		return mEnemyTanks;
	}

	public List<Tank> getPlayerTanks() {
		return mPlayerTanks;
	}

	private void removeEnemyTank(int i) {
		TankManager.addDefeatedTank(mEnemyTanks.get(i));
		mEnemyTanks.remove(i);
	}

	private void removePlayerTank(int i) {
		mPlayerTanks.remove(i);
	}

	public void addPlayerTank(Tank playerTank) {
		mPlayerTanks.add(playerTank);

		addObject(playerTank.getAppearingSprite(), ObjectLayer.MOVING);
		addObject(playerTank, ObjectLayer.MOVING);

	}

	public void addEnemyTank(Tank enemyTank) {
		mEnemyTanks.add(enemyTank);

		addObject(enemyTank.getAppearingSprite(), ObjectLayer.MOVING);
		addObject(enemyTank, ObjectLayer.MOVING);
	}

	public int getTotalPlayerTanks() {
		return mPlayerTanks.size();
	}

	public boolean isEagleAlive() {
		/*
		 * Kiểm tra Đại bàng còn sống không ?
		 */

		return mIsEagleAlive;
	}

	public void addObject(IMapObject object, ObjectLayer layer) {
		addObject(object.getSprite(), layer);
	}

	public void addObject(Sprite sprite, ObjectLayer layer) {
		_layerMap.get(layer).attachChild(sprite);
	}

	public void notifyEagleDie() {
		mIsEagleAlive = false;
	}

	public void destroyAllEnemyTanks() {
		for (int i = 0; i < mEnemyTanks.size(); i++) {
			mEnemyTanks.get(i).KillSelf();
			TankManager.addDefeatedTank(mEnemyTanks.get(i));
		}

		mEnemyTanks.clear();
	}
}