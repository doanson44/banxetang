package org.xetang.map.helper;

import java.util.LinkedList;
import java.util.Queue;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.xetang.manager.GameManager;
import org.xetang.map.IMapObject;

public class DestroyHelper implements IUpdateHandler {

	private static DestroyHelper _instance = null;

	public static DestroyHelper getInstance() {
		if (_instance == null) {
			_instance = new DestroyHelper();
		}

		return _instance;
	}

	private static boolean _isRemoving = false;

	private static Runnable _removalCore;

	private static Queue<IMapObject> _destroyQueue = new LinkedList<IMapObject>();

	private DestroyHelper() {

		_removalCore = new Runnable() {

			@Override
			public void run() {

				doRemove(true);

				PhysicsConnector connector;
				while (!_destroyQueue.isEmpty()) {

					final IMapObject object;
					final Sprite sprite;

					object = _destroyQueue.peek();

					object.setAlive(false);

					sprite = object.getSprite();

					connector = GameManager.PhysicsWorld
							.getPhysicsConnectorManager()
							.findPhysicsConnectorByShape(sprite);

					if (connector != null) {
						GameManager.PhysicsWorld
								.unregisterPhysicsConnector(connector);
					}

					if (object.getBody() != null) {
						object.getBody().setActive(false);
						GameManager.PhysicsWorld.destroyBody(object.getBody());
					}

					GameManager.Activity.runOnUpdateThread(new Runnable() {

						@Override
						public void run() {
							((Entity) object).detachSelf();
							((Entity) object).dispose();
							sprite.detachSelf();
							sprite.dispose();
						}
					});

					_destroyQueue.poll();
				}

				doRemove(false);
			}
		};
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {

		if (_destroyQueue.size() <= 0 || isRemoving()) {
			return;
		}

		GameManager.Activity.runOnUpdateThread(_removalCore);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public static synchronized boolean isRemoving() {
		return _isRemoving;
	}

	public static synchronized void doRemove(boolean remove) {
		DestroyHelper._isRemoving = remove;
	}

	public static synchronized void add(IMapObject object) {
		if (!_destroyQueue.contains(object) && object.isAlive()) {
			_destroyQueue.add(object);
		}
	}
}
