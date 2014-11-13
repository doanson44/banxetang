package org.xetang.root;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import junit.framework.Assert;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.debug.Debug.DebugLevel;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameMapManager;
import org.xetang.menu.MenuItem;

import com.badlogic.gdx.utils.Array;

import android.R.menu;
import android.graphics.Typeface;
import android.media.CamcorderProfile;
import android.text.style.TypefaceSpan;

/**
 * 
 */
public class MainMenuScene extends Scene  {

	List<MenuItem> _items;
	HUD _hudMain = new HUD();
	HUD _hudOptions = new HUD();
	HUD _hudAbout = new HUD();
	Music background;
	
	/**
     * 
     */
	public MainMenuScene() {
		createMainMenu();
		initHudOptions();
		initHudAbout();
		
	}

	private void initHudAbout() {	
		
		
		try {
			InputStream input = GameManager.AssetManager.open("about.txt");
			String txt = readStreamAsString(input);
			String[] lines = txt.split("\n");
			GameManager.getFont("font2").load();
			for (int i = 0; i < lines.length; i++) {
				Text t = new Text(0, 0, GameManager.getFont("font2"), lines[i], GameManager.VertexBufferObject);
				t.setPosition(GameManager.Camera.getWidth()/2 - t.getWidth()/2, GameManager.Camera.getHeight() + i * t.getHeight()*1.3f);
				_hudAbout.attachChild(t);
				if(lines[i].contains("["))
					t.setColor(Color.RED);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if(GameManager.Camera.getHUD() == _hudAbout){
			onUpdateHubAbout(pSecondsElapsed);
		}
	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionUp() && GameManager.Camera.getHUD() == _hudAbout){
			GameManager.Camera.setHUD(_hudMain);
			return true;
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}

	private void onUpdateHubAbout(float pSecondsElapsed) {
		for (int i = 0; i < _hudAbout.getChildCount(); i++) {
			IEntity item = _hudAbout.getChildByIndex(i);
			item.setPosition(item.getX(), item.getY() - 1);
		}
		if(_hudAbout.getChildByIndex(_hudAbout.getChildCount()-1).getY()<0){
			GameManager.Camera.setHUD(_hudMain);
			resetHudAbout();
		}
		
	}

	private void resetHudAbout() {
		int total = (int) (((int) -_hudAbout.getChildByIndex(0).getY()) + GameManager.Camera.getHeight());
		for (int i = 0; i < _hudAbout.getChildCount(); i++) {
			IEntity item =  _hudAbout.getChildByIndex(i);
			item.setY(item.getY() + total);
		}
		
	}

	private String readStreamAsString(InputStream input) {
		try {
			Reader reader = new InputStreamReader(input);
			char[] buffer = new char[1024];
			String result = "";
			int nBytesReaded = 0;
			while( (nBytesReaded = reader.read(buffer)) > -1){
				char[] t = new char[nBytesReaded];
				System.arraycopy(buffer, 0, t, 0, nBytesReaded); 
				result += String.valueOf(t);
			}
			reader.close();
			return result;
		} catch (Exception e) {
			return "";
		}
		
	}

	private void initHudOptions() {
		createTitleOption();
		createButtonOption();
	}

	private void createButtonOption() {
		Font f = GameManager.getFont("font2");
		f.load();
		
		//Background sound
		Text t = new Text(0, 0, f, "Background", GameManager.VertexBufferObject);
		t.setPosition(GameManager.Camera.getWidth()/2 - t.getWidth()/2 - 25, 200);
		_hudOptions.attachChild(t);
		TiledSprite sprite = new TiledSprite(t.getX() + t.getWidth() + 20, t.getY() - (50 - t.getHeight())/2, (ITiledTextureRegion) GameManager.getTexture("sound"), GameManager.VertexBufferObject){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					//change
					this.setCurrentTileIndex((this.getCurrentTileIndex()+1)%2);
					GameManager.IsBackgroundSound = this.getCurrentTileIndex() == 0;
					if(GameManager.IsBackgroundSound)
						playMusic();
					else
						stopMusic();
				}
				return true;
			}
		};
		_hudOptions.registerTouchArea(sprite);
		_hudOptions.attachChild(sprite);
		
		//Effect sound
		t = new Text(0, 0, f, "Effect", GameManager.VertexBufferObject);
		t.setPosition(GameManager.Camera.getWidth()/2 - t.getWidth()/2 - 25, 270);
		_hudOptions.attachChild(t);
		sprite = new TiledSprite(t.getX() + t.getWidth() + 20, t.getY() - (50 - t.getHeight())/2, (ITiledTextureRegion) GameManager.getTexture("sound"), GameManager.VertexBufferObject){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					//change
					this.setCurrentTileIndex((this.getCurrentTileIndex()+1)%2);
					GameManager.IsEffectSound = this.getCurrentTileIndex() == 0;
				}
				return true;	
			}
		};
		_hudOptions.registerTouchArea(sprite);
		_hudOptions.attachChild(sprite);

		//Back
		t = new Text(0, 0, f, "Back", GameManager.VertexBufferObject){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					GameManager.Camera.setHUD(_hudMain);
					GameManager.getFont("font2").load();
					this.setColor(Color.WHITE);
				}
				else if(pSceneTouchEvent.isActionDown()){
					GameManager.getFont("font2").load();
					this.setColor(Color.RED);	
				}
				else{
					GameManager.getFont("font2").load();
					this.setColor(Color.WHITE);	
				}
				return true;
			}
		};
		t.setPosition(GameManager.Camera.getWidth()/2 - t.getWidth()/2, 350);
		_hudOptions.registerTouchArea(t);
		_hudOptions.attachChild(t);

	}

	private void createTitleOption() {
		Font f = GameManager.getFont("font1");
		f.load();
		Text t = new Text(0, 0, f, "option", GameManager.VertexBufferObject);
		t.setPosition(GameManager.Camera.getWidth()/2 - t.getWidth()/2, 50);
		//t.setScaleCenter(1.5f, 1.5f);
		
		_hudOptions.attachChild(t);
		
	}

	private void createMainMenu() {
		createTitle();
		playMusic();
		createMenuItem();
		attachMenuItem();
		GameManager.Camera.setHUD(_hudMain);
	}

	private void playMusic( ) {
		if(!GameManager.IsBackgroundSound) return;
		background = GameManager.getMusic("menu");
		background.setLooping(true);
		background.play();
	
	}

	private void createTitle() {
		FontFactory.setAssetBasePath("font/");
		
		Font f = FontFactory.createFromAsset(GameManager.FontManager, 
				GameManager.TextureManager, 256, 256, 
				TextureOptions.BILINEAR,
				GameManager.AssetManager, 
				"font1.ttf", 64f, true, Color.WHITE_ABGR_PACKED_INT);
		f.load();
		
		Text t = new Text(0, 50, f, "battle city", GameManager.VertexBufferObject);
		t.setPosition(GameManager.Camera.getWidth()/2 - t.getWidth()/2, 50);
		_hudMain.attachChild(t);
		
	}

	private void attachMenuItem() {
		for (int i = 0; i < _items.size(); i++) {
			MenuItem item = _items.get(i);
			item.setPosition(GameManager.Camera.getWidth()/2 - item.getWidthMenuItem()/2, 200 + i * 64);
			this._hudMain.attachChild(item);
			this._hudMain.registerTouchArea(item);
		}
	}

	/* ĂN ĐI KU
	 * 
	 */
	private void createMenuItem() {
		_items = new ArrayList<MenuItem>();

		/*
		 * Tạo các item trong Mainmenu
		 */
		_items.add(new MenuItem(this, "new game", 1));
		_items.add(new MenuItem(this, "options", 2));
		_items.add(new MenuItem(this, "about", 3));
		_items.add(new MenuItem(this, "exit", 4));
		

	}

	/* ĂN ĐI KU
	 * Hàm sẽ được gọi khi có 1 Menuitem được chọn
	 * Tại đây chuyển hướng đến các Scene khác
	 */
	public void onClickItem(MenuItem menuItem) {	
		if(GameManager.IsEffectSound){
			GameManager.getMusic("blop").play();
			GameManager.getMusic("blop").setVolume(2f);
		}
		
		switch (menuItem.getId()) {
		case 1: //new game	
			stopMusic();
			GameManager.Camera.setHUD(null);
			GameManager.SwitchToScene(1);
			
		break;
		
		case 2: //options
			GameManager.Camera.setHUD(_hudOptions);
		break;
		
		case 3: //about
			GameManager.Camera.setHUD(_hudAbout);
		break;
		
		case 4: //exit
			stopMusic();
			GameManager.Context.finish();
		break;

		default:
		break;
		}
		/*
		 * Dùng hàm này để chuyển hướng Scene
		 * VD bên dưới
		 */
		//GameManager.SwitchToScene(this, new GameScene());
	}

	private void stopMusic() {
		GameManager.getMusic("menu").pause();
		
	}

	public void onSwitched() {
		GameManager.Camera.setHUD(_hudMain);
		playMusic();
		
	}

}