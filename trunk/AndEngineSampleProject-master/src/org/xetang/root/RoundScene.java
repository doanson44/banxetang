package org.xetang.root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;

import com.badlogic.gdx.math.Vector2;

import android.content.Context;
import android.graphics.Color;

public class RoundScene extends Scene {
	/************************************************************/
	/*							Constant						*/
	/************************************************************/
	final String DEFAUILT_IMAGE = "default.png";
	final int IMAGE_SIZE = 400;
	final int NUM_COLUMN = 1;
	final int NUM_ROW = 1;

	/************************************************************/
	/*							Members							*/
	/************************************************************/
	private Map<String, BaseTextureRegion> mResouces;
	private List<Round> mRounds;
	private Context mContext;
	private List<IEntity> mRoundSprites;

	int titleHeight;
	int paddingWidth;
	int paddingHeight;
	int frameWidth;
	int frameHeight;
	int frameStartX;
	int frameStartY;
	
	/************************************************************/
	/*							Constructor						*/
	/************************************************************/
	public RoundScene(Context context) {
		mContext = context;
		
		initData();
		loadResource();
		calcFrameSize();

		drawTitle();

	}



	/************************************************************/
	/************************************************************/
	private void initData() {
		mResouces = new HashMap<String, BaseTextureRegion>();
		mRounds = new ArrayList<RoundScene.Round>();
		mRoundSprites = new ArrayList<IEntity>();
	}

	
	private void loadResource() {
		try {
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/round/");
			TiledTextureRegion texture;
			BitmapTextureAtlas 	atlas = new BitmapTextureAtlas(GameManager.TextureManager, IMAGE_SIZE, IMAGE_SIZE);
			String[] names = mContext.getAssets().list("gfx/round");

			for (int i = 0; i < names.length; i++) {
				atlas = new BitmapTextureAtlas(GameManager.TextureManager, IMAGE_SIZE, IMAGE_SIZE);
				texture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
						atlas, 
						GameManager.AssetManager,
						names[i], 
						0,
						0, 
						1, 
						1); 
				mResouces.put(getNamePart(names[i]), texture);
				atlas.load();
			}
			atlas = new BitmapTextureAtlas(GameManager.TextureManager, IMAGE_SIZE, IMAGE_SIZE);
			texture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
					atlas, 
					GameManager.AssetManager,
					DEFAUILT_IMAGE, 
					0,
					0, 
					1, 
					1); 
			mResouces.put("default", texture);
			
			atlas.load();
		} catch (Exception e) {
		}
		
	}
	
	private void loadRoundSprites() {
		TiledSprite sprite;
		Text text;
		Font font = GameManager.getFont("font2", 36f, Color.WHITE);
		
		for (int i = 0; i < mRounds.size(); i++) {
			sprite = new TiledSprite(
					0, 
					0, 
					(TiledTextureRegion)mResouces.get(mRounds.get(i).ImageAssert), 
					GameManager.VertexBufferObject){
				boolean bSelected = false;
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (pSceneTouchEvent.isActionDown()){
						this.setColor(0.3f, 0.2f, 0.4f, 1f);
						bSelected = true;
					}
					else if (pSceneTouchEvent.isActionUp()){
						this.setColor(1, 1, 1, 1f);
						if (bSelected)
							onSelectedStage((Round)this.getUserData());
					}
					else if (pSceneTouchEvent.isActionMove()){
						if (prePos.dst(originPos) > 20){
							this.setColor(1, 1, 1, 1f);
							bSelected = false;
						}
					}

					
					return true;
				}
				
			};
			sprite.setSize(frameWidth, frameHeight);
			sprite.setPosition(frameStartX + i*frameWidth + (i+1)*paddingWidth, frameStartY + paddingHeight + titleHeight);
			sprite.setUserData(mRounds.get(i));
			text = new Text(
					0, 
					0, 
					font, 
					String.format("Stage %d", i+1), 
					GameManager.VertexBufferObject);
			text.setX(sprite.getX() + sprite.getWidth()/2 - text.getWidth()/2);
			text.setY(sprite.getY() + sprite.getHeight()/2 - text.getHeight()/2);
			
			
			this.attachChild(sprite);
			this.attachChild(text);			
			mRoundSprites.add(sprite);
			mRoundSprites.add(text);
			
			this.registerTouchArea(sprite);

		}
		
	}


	protected void onSelectedStage(Round selectedRound) {
		if (selectedRound.IsUnlock){
			restoreCamera();
			GameManager.seekStage(selectedRound.Index);
			GameManager.switchToScene("game", null);
		}
	}


	private void drawTitle() {
		Font font = GameManager.getFont("font1", 64f, Color.WHITE);
		Text text = new Text(
				0, 
				0, 
				font, 
				"select stage", 
				GameManager.VertexBufferObject);
		text.setPosition(GameManager.CAMERA_WIDTH / 2 - text.getWidth()/2, titleHeight / 2 - text.getHeight()/2);
		this.attachChild(text);
	}
	
	private void calcFrameSize() {
		this.titleHeight = GameManager.CAMERA_HEIGHT / 100 * 20;
		this.paddingWidth = GameManager.CAMERA_WIDTH / 100 * 30;
		this.paddingHeight = GameManager.CAMERA_HEIGHT / 100 * 10;
		this.frameWidth = (GameManager.CAMERA_WIDTH - (NUM_COLUMN + 1)
				* this.paddingWidth)
				/ NUM_COLUMN;
		this.frameHeight = (GameManager.CAMERA_HEIGHT - titleHeight - (NUM_ROW + 1)
				* this.paddingHeight)
				/ NUM_ROW;
		this.frameStartX = 0;
		this.frameStartY = 0;

	}
	private void loadRounds() {
		try {
			String[] names = mContext.getAssets().list("gfx/round");
			mRounds.clear();
			for (String name : names) {
				if (!name.contains("default")) {
					int idx = Integer.parseInt(getNamePart(name));
					mRounds.add(new Round(
							GameManager.getReachedStage() >= idx ? getNamePart(name) : getNamePart(DEFAUILT_IMAGE),
							idx, GameManager.getReachedStage() >= idx));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getNamePart(String name) {
		int start = name.lastIndexOf(File.separator);
		start = start == -1 ? 0 : start;
		int end = name.lastIndexOf(".");
		end = end == -1 ? name.length() : end;
		return name.substring(start, end);
	}
	

	HUD backupHUD;
	private void backupCamera() {
		backupHUD = GameManager.Camera.getHUD();
	}
	


	private void restoreCamera() {
		GameManager.Camera.set(GameManager.CAMERA_X, GameManager.CAMERA_Y, GameManager.CAMERA_WIDTH + GameManager.CAMERA_X, GameManager.CAMERA_HEIGHT + GameManager.CAMERA_Y);
		GameManager.Camera.setHUD(backupHUD);
	}





	private void setNewCamera() {
		GameManager.Camera.set(0, 0, GameManager.CAMERA_WIDTH, GameManager.CAMERA_HEIGHT);	
		GameManager.Camera.setHUD(null);
	}

	private void transformSprite(float deltaX, float deltaY) {
		Vector2 curPos = new Vector2();
		for (int i = 0; i < mRoundSprites.size(); i++) {
			curPos.x = mRoundSprites.get(i).getX();
			curPos.y = mRoundSprites.get(i).getY();
			curPos.add(deltaX, deltaY);
			mRoundSprites.get(i).setPosition(curPos.x, curPos.y);
		}
	}

	Vector2 transformVector = Vector2.Zero;
	private void transformInertial(Vector2 vector) {
		transformVector = vector;
	}
	
	public void onSwitched(String actionSceneClose) {
		if (actionSceneClose == GameManager.ACTION_SCENE_OPEN){
			backupCamera();
			setNewCamera();
			loadRounds();
			loadRoundSprites();
		}
		else
		{
			restoreCamera();
			clearRoundSprites();
		}
	}



	private void clearRoundSprites() {
		for (int i = 0; i < mRoundSprites.size(); i++) {
			this.detachChild(mRoundSprites.get(i));
		}
		this.clearTouchAreas();
	}


	/************************************************************/
	/*						Override Method						*/
	/************************************************************/
	Vector2 prePos = new Vector2();
	Vector2 originPos = new Vector2();
	long preTime;
	long delayTime = 350;
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		//Debug.d(GameManager.TANK_TAG, "Scene touch");
		if (pSceneTouchEvent.isActionMove()){
			transformSprite(pSceneTouchEvent.getX() - prePos.x, 0);
			prePos.x = pSceneTouchEvent.getX();
			prePos.y = pSceneTouchEvent.getY();
		}
		else if (pSceneTouchEvent.isActionDown())
		{
			originPos.x = prePos.x = pSceneTouchEvent.getX();
			originPos.y = prePos.y = pSceneTouchEvent.getY();
			preTime = System.currentTimeMillis();

		}
		else if (pSceneTouchEvent.isActionUp())
		{
			if (System.currentTimeMillis() - preTime < delayTime )
				transformInertial(new Vector2(pSceneTouchEvent.getX() - originPos.x, 0));
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}
	

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if (transformVector.dst(0, 0) > 0.001 && mRoundSprites.get(0).getX() < frameStartX + paddingWidth
				&& mRoundSprites.get(mRoundSprites.size()-2).getX() > frameStartX + paddingWidth ){
			transformSprite(transformVector.x/3 , 0);
			transformVector.x /= 1.15f;
			//Debug.d(GameManager.TANK_TAG, String.valueOf(transformVector.x));
		}
		else if (mRoundSprites.get(0).getX() >= frameStartX + paddingWidth){
			transformSprite(frameStartX + paddingWidth - mRoundSprites.get(0).getX(), 0);
			transformVector = Vector2.Zero;
		}
		else if (mRoundSprites.get(mRoundSprites.size()-2).getX() <= frameStartX + paddingWidth ){
			transformSprite(frameStartX + paddingWidth - mRoundSprites.get(mRoundSprites.size()-2).getX(), 0);
			transformVector = Vector2.Zero;
		}
	}

	/************************************************************/
	/*						Internal Class						*/
	/************************************************************/
	class Round {
		public String ImageAssert;
		public int Index;
		public boolean IsUnlock;

		public Round(String image, int idx, boolean isUnlock) {
			ImageAssert = image;
			Index = idx;
			IsUnlock = isUnlock;
		}
	}


}
