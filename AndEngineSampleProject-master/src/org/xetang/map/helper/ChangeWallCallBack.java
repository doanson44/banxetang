package org.xetang.map.helper;

import org.andengine.entity.IEntity;
import org.xetang.manager.GameMapManager;
import org.xetang.map.IMapObject;
import org.xetang.map.MapObjectFactory;
import org.xetang.map.MapObjectFactory.ObjectType;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

public class ChangeWallCallBack implements QueryCallback{

	@Override
	public boolean reportFixture(Fixture fixture) {
		// TODO Auto-generated method stub
		
		IMapObject object = (IMapObject) fixture.getBody().getUserData();
		if(object!= null && (object.getType() == ObjectType.BrickWall 
				|| object.getType() == ObjectType.SteelWall)){
			DestroyHelper.add(object);
		}
		
		return true;
	}

}
