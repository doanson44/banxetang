package org.xetang.menu;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameManager;
import org.xetang.root.MainMenuScene;
import org.xetang.root.MenuEntity;

/**
 * 
 */
public class MenuItem extends MenuEntity implements ITouchArea {

	//Thông báo về cha khi item này được chọn
	MainMenuScene _parent;
	Font _font;
	int _id;
	
	Text _text;
	
	/*ĂN ĐI KU
	 * 
	 */
	public MenuItem(MainMenuScene parent, String string, int menuId) {
		// TODO Auto-generated constructor stub
		_parent = parent;
		_id = menuId;
		/*
		 * Thêm tại đây
		 */
		
		loadFont(Color.WHITE_ABGR_PACKED_INT);
		_text = new Text(0,0, this._font, string, GameManager.VertexBufferObject);
		this.attachChild(_text);
		
		
		
		
		
	}

	private void loadFont(int color) {
		FontFactory.setAssetBasePath("font/");
		
		this._font = FontFactory.createFromAsset(GameManager.FontManager, 
				GameManager.TextureManager, 256, 256, 
				TextureOptions.BILINEAR,
				GameManager.AssetManager, 
				"font2.ttf", 32f, true, color);
		this._font.load();
		
	}

	public int getWidthMenuItem() {
		return (int) _text.getWidth();
	}

	@Override
	public boolean contains(float pX, float pY) {
		return this._text.contains(pX, pY);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		if(pSceneTouchEvent.isActionUp()){
			loadFont(Color.WHITE_ABGR_PACKED_INT);
			_text.setColor(1,1,1);
			_parent.onClickItem(this);
		}
			
		else if(pSceneTouchEvent.isActionDown()){
			loadFont(Color.WHITE_ABGR_PACKED_INT);
			_text.setColor(1,0,0);
		}		
		return true;
	
	}

	public int getId() {
		return _id;
	}
	
	
}