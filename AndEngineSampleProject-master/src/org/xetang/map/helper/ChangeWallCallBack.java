package org.xetang.map.helper;

import org.xetang.map.object.IMapObject;
import org.xetang.map.object.MapObjectFactory.ObjectType;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

public class ChangeWallCallBack implements QueryCallback{

	@Override
	public boolean reportFixture(Fixture fixture) {
		// TODO Auto-generated method stub
		
		IMapObject object = (IMapObject) fixture.getBody().getUserData();
		if(object!= null && (object.getType() == ObjectType.BRICK_WALL 
				|| object.getType() == ObjectType.STEEL_WALL)){
			DestroyHelper.add(object);
		}
		
		return true;
	}

}
