package org.xetang.map;

import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.tank.Tank;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.util.Log;
import android.widget.Toast;



/**
 * 
 */
public class Item {
	Tank mOwner = null; //Xe tăng nhặt đc vật phẩm này
	int type;
	int TimeSurvive = 0;
	int TimeAffect = 0;
	float SecPerFrame = 0;
	float _alpha = 1;
	Map _map;
	public TiledSprite mSprite = null;
	Body body;
	
	public Item (TiledTextureRegion region, Map map)
	{
		_map = map;
		mSprite = new TiledSprite(GetRandomPx(), GetRandomPy(),region, GameManager.VertexBufferObject);
		mSprite.setSize(64, 64);
		CreateBody();
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
		return mSprite;
	}
	public void affect(){
		//Xử lý tác dụng của vật phẩm
		//....
	}
	
	protected void CreateBody (){
		body = PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, mSprite,
				BodyType.StaticBody, PhysicsFactory
				.createFixtureDef(0, 0, 0));
	}
	
	public void setOwner(Tank tank){
		mOwner = tank;
	}


	public void update(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		if(mSprite != null){
			Animate();
			SecPerFrame += pSecondsElapsed;
			if(SecPerFrame > 1){
				SecPerFrame = 0;
				TimeSurvive ++;
				
				Log.i("Time Surive", String.valueOf(TimeSurvive));
			}
			if(mOwner != null){
				TimeAffect ++;
				affect();
			}
		}
	}
	
	Boolean flag = false;
	public void Animate (){
		if(TimeSurvive > 4 && !flag || _alpha >= 1){
			_alpha -= 0.006f;
			flag = false;
		}
		if(_alpha < 0.4 || flag){
			flag = true;
			_alpha += 0.006f;
		}
		mSprite.setAlpha(_alpha);
	}
	// hàm xóa sprite khi quá 10s mà không có xe tăng nào lấy
	public Boolean DestroySprite (){
		if((TimeSurvive > 10 && mOwner== null) || TimeAffect > 5)
			return true;
		return false;
	}
}