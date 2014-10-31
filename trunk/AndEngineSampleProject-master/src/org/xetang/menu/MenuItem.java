package org.xetang.menu;

import org.xetang.root.MainMenuScene;
import org.xetang.root.MenuEntity;

/**
 * 
 */
public class MenuItem extends MenuEntity {

	//Thông báo về cha khi item này được chọn
	MainMenuScene _parent;
	
	/*ĂN ĐI KU
	 * 
	 */
	public MenuItem(MainMenuScene parent, String string) {
		// TODO Auto-generated constructor stub
		_parent = parent;
		
		/*
		 * Thêm tại đây
		 */
		
		_parent.onClickItem(this);
	}
	
	
}