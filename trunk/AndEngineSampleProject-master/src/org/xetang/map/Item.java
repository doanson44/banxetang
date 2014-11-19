package org.xetang.map;

import java.util.Random;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.root.GameEntity;
import org.xetang.tank.Tank;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import android.util.Log;



/**
 * 
 */
public class Item implements IMapObject {
	Tank _mOwner = null; //Xe tăng nhặt đc vật phẩm này
	int type;
	int _TimeSurvive = 0;
	int _TimeAffect = 0;
	float _SecPerFrame = 0;
	float _alpha = 1;
	public static int _CellWidth = 52;
	public static int _CellHeight = 52;
	Map _map;
	public TiledSprite _sprite = null;
	Body _body;
	boolean _isAlive  = false;
	
	public Item (TiledTextureRegion region, Map map)
	{
		_map = map;
		_sprite = new TiledSprite(GetRandomPx(), GetRandomPy(),region, GameManager.VertexBufferObject);
		_sprite.setSize(_CellWidth, _CellHeight);
		CreateBody();
		setAlive(true);
	}
	
	public int GetRandomPx ()
	{
		return new Random().nextInt(570);
	}
	public int GetRandomPy ()
	{
		return new Random().nextInt(430);
	}
	
	public TiledSprite GetSprite()
	{
		return _sprite;
	}
	public void affect(){
		//....
	}
	
	protected void CreateBody (){
		_body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, _sprite,
				BodyType.StaticBody, PhysicsFactory
				.createFixtureDef(0, 0, 0,true));
		_body.setUserData(this);
		GameManager.PhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				_sprite, _body, true, true));
	
	}
	
	public void setOwner(Tank tank){
		_mOwner = tank;
	}


	public void update(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		if(_sprite != null){
			Animate();
			_SecPerFrame += pSecondsElapsed;
			if(_SecPerFrame > 1){
				_SecPerFrame = 0;
				_TimeSurvive ++;
				DestroySprite();
			//	Log.i("Time Surive", String.valueOf(_TimeSurvive));
			}
			if(_mOwner != null){
				_TimeAffect ++;
				affect();
			}
		}
	}
	
	Boolean flag = false;
	public void Animate (){
		if(_TimeSurvive > 4 && !flag || _alpha >= 1){
			_alpha -= 0.01f;
			flag = false;
		}
		if(_alpha < 0.4 || flag){
			flag = true;
			_alpha += 0.01f;
		}
		_sprite.setAlpha(_alpha);
	}
	
	// hàm xóa sprite khi quá 10s mà không có xe tăng nào lấy
	public void DestroySprite (){
		if((_TimeSurvive > 10 && _mOwner== null) || _TimeAffect > 5)
			_isAlive = false;
	}

	public MapObject clone(){
		return null;
	}
	@Override
	public void putToWorld() {
		// TODO Auto-generated method stub
		if (_body == null) {
			CreateBody();
		}
	}

	@Override
	public void putToWorld(float posX, float posY) {
		// TODO Auto-generated method stub
		
		setPosition(posX, posY);
		putToWorld();

	}

	@Override
	public void setPosition(float posX, float posY) {
		// TODO Auto-generated method stub
		_sprite.setPosition(posX, posY);

		if (_body != null) {
			_body.setTransform(posX, posY, _body.getAngle());
		}
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return _sprite.getX();
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return _sprite.getY();
	}

	@Override
	public void transform(float x, float y) {
		// TODO Auto-generated method stub
		setPosition(_sprite.getX() + x, _sprite.getY() + y);
	}

	@Override
	public float getCellWidth() {
		// TODO Auto-generated method stub
		return _CellWidth;
	}

	@Override
	public float getCellHeight() {
		// TODO Auto-generated method stub
		return _CellHeight;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return _isAlive;
	}

	@Override
	public void setAlive(boolean alive) {
		// TODO Auto-generated method stub
		_isAlive = alive;
	}

	@Override
	public TiledSprite getSprite() {
		// TODO Auto-generated method stub
		return _sprite;
	}

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return _body;
	}

	@Override
	public FixtureDef getObjectFixtureDef() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doContact(IMapObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ObjectType getType() {
		// TODO Auto-generated method stub
		return null;
	}
}