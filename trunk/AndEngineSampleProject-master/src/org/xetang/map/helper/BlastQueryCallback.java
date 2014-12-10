package org.xetang.map.helper;

import java.util.LinkedList;
import java.util.Queue;

import org.xetang.map.object.IBullet;
import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObjectFactory.ObjectType;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

public class BlastQueryCallback implements QueryCallback {

	IBullet _ownBullet;
	Queue<IMapObject> _inRangeBodies = new LinkedList<IMapObject>();

	@Override
	public boolean reportFixture(Fixture fixture) {
		IMapObject object = (IMapObject) fixture.getBody().getUserData();

		if (object != null && !_inRangeBodies.contains(object)
				&& DecideHelpder.canDestroy(object, _ownBullet)
				&& object.getType() != ObjectType.PlayerTank
				&& object.getType() != ObjectType.EnermyTank) {
			// object.getSprite().setVisible(false);
			_inRangeBodies.add(object);
		}

		return true;
	}

	public void goOff() {
		while (!_inRangeBodies.isEmpty()) {
			DestroyHelper.add(_inRangeBodies.poll());
		}
	}

	public void setBullet(IBullet bullet) {
		_ownBullet = bullet;
	}
}
