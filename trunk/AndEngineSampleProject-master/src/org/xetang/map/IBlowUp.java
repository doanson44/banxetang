package org.xetang.map;

import com.badlogic.gdx.math.Vector2;

public interface IBlowUp extends IMapObject {

	public void setOwnObject(IMapObject ownObject);

	public void setTargetObject(IMapObject targetObject);
	
	public void blowUpAtHere();
	
	public void blowUpAt(Vector2 pos);

	public void blowUpAt(float posX, float posY);
}
