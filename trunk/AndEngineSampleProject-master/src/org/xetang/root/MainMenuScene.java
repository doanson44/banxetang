package org.xetang.root;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.Scene;
import org.xetang.manager.GameManager;
import org.xetang.menu.MenuItem;

/**
 * 
 */
public class MainMenuScene extends Scene {

	List<MenuItem> _items;

	/**
     * 
     */
	public MainMenuScene() {
		createMenuItem();
		attachMenuItem();
	}

	private void attachMenuItem() {
		for (int i = 0; i < _items.size(); i++) {
			this.attachChild(_items.get(i));
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
		_items.add(new MenuItem(this, "<dummy>"));
		_items.add(new MenuItem(this, "<dummy>"));
		_items.add(new MenuItem(this, "<dummy>"));

	}

	/* ĂN ĐI KU
	 * Hàm sẽ được gọi khi có 1 Menuitem được chọn
	 * Tại đây chuyển hướng đến các Scene khác
	 */
	public void onClickItem(MenuItem menuItem) {
		// TODO Auto-generated method stub
		
		
		/*
		 * Dùng hàm này để chuyển hướng Scene
		 * VD bên dưới
		 */
		//GameManager.SwitchToScene(this, new GameScene());
	}

}