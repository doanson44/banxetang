package org.xetang.map.helper;

import java.util.LinkedList;
import java.util.Queue;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObject;

public class DestroyHelper implements IUpdateHandler {

	private static DestroyHelper _instance = null;

	protected DestroyHelper() {

		_removalCore = new Runnable() {

			@Override
			public void run() {

				doRemoving(true);

				PhysicsConnector connector;
				while (!_destroyQueue.isEmpty()) {

					final MapObject object;
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

					object.getBody().setActive(false);
					GameManager.PhysicsWorld.destroyBody(object.getBody());

					GameManager.Activity.runOnUpdateThread(new Runnable() {

						@Override
						public void run() {
							object.detachSelf();
							object.dispose();
							sprite.detachSelf();
							sprite.dispose();
						}
					});

					_destroyQueue.poll();
				}

				doRemoving(false);
			}
		};
	}

	public static DestroyHelper getInstance() {
		if (_instance == null) {
			_instance = new DestroyHelper();
		}

		return _instance;
	}

	private static boolean _isRemoving = false;

	private static Runnable _removalCore;

	private static Queue<MapObject> _destroyQueue = new LinkedList<MapObject>();

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

	public static synchronized void doRemoving(boolean remove) {
		DestroyHelper._isRemoving = remove;
	}

	public static void add(MapObject object) {

		if (!_destroyQueue.contains(object) && object.isAlive()) {
			_destroyQueue.add(object);
		}
	}
}
