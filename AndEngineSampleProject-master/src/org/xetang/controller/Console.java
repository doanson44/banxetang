package org.xetang.controller;

import java.util.HashMap;
import java.util.Map;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.root.GameEntity;

/**
 * 
 */
public class Console extends GameEntity {

	IGameController _playerTank;
	Map<String, TiledSprite> _buttonBoard = new HashMap<String, TiledSprite>();
	
	public Console(IGameController playerTank) {
		_playerTank = playerTank;
		
		createAllButton();
	}

	/*
	 * ĂN ĐI KU
	 * Tạo tất cả các nút trên bàn điều khiển
	 * VD: Map(Up, UpArrow)...
	 * Gán các sự kiện nút nhấn tương ứng với việc điều khiển xe tăng
	 */
	private void createAllButton() {
		// TODO Auto-generated method stub
		//VD: _playerTank.onLeft();
	}

	
}