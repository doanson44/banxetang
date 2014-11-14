package org.xetang.map;

import org.andengine.entity.sprite.Sprite;
import org.xetang.map.MapObject.ObjectType;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface IMapObject {

	public abstract IMapObject clone();

	public void putToWorld();

	public void putToWorld(float posX, float posY);

	public void setPosition(float posX, float posY);

	public float getX();

	public float getY();

	public void transform(float x, float y);

	public float getCellWidth();

	public float getCellHeight();

	public boolean isAlive();

	public void setAlive(boolean alive);

	public Sprite getSprite();

	public Body getBody();
	
	public FixtureDef getObjectFixtureDef();

	public abstract void doContact(IMapObject object);

	public abstract ObjectType getType();
}
