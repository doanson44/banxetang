package org.xetang.tank;



import org.xetang.map.IMapObject;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

public class MoveCallBack implements QueryCallback {

	boolean IsExist = false;
	@Override
	public boolean reportFixture(Fixture fixture) {
		// TODO Auto-generated method stub
		IMapObject object = (IMapObject) fixture.getBody().getUserData();

		if(object != null){
			IsExist = true;
			Log.i("Callback", object.getType().name());
		}
		return true;
	}
	
	public boolean CheckExist(){
		return IsExist;
	}

}
