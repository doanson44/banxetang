package org.xetang.map;

import java.util.ArrayList;

import org.andengine.entity.text.Text;
import org.xetang.manager.GameManager;
import org.xetang.map.item.EnermyIcon;
import org.xetang.root.GameEntity;

import android.graphics.Color;


public class RightMenu extends GameEntity {

	protected Map _mMap;
	protected ArrayList<EnermyIcon> _mEnermys = new ArrayList<EnermyIcon>();
	protected int _TotalEnermy;
	public Text Player1 = null;
	
	public RightMenu (float px, float py, Map map, int totalEnermy){
		
		_mMap = map;
		_TotalEnermy = totalEnermy;
		InitEnermyTank(px,py);
		UpdateText();
		
		AddStateText();
	}
	
	public void UpdateText() {
		if (Player1 == null){
			Player1 = new Text(GameManager.MAP_SIZE + 20, GameManager.MAP_SIZE -200, 
					GameManager.getFont("font2", 24f, Color.WHITE), 
					"P1: " + GameManager.CurrentMapManager.GetPlayer1Life(),
					7,
					GameManager.VertexBufferObject);
			this.attachChild(Player1);
			
		}
		else
			Player1.setText("P1: " + GameManager.CurrentMapManager.GetPlayer1Life());
	}
	
	public void AddStateText(){

		Text State = new Text(0,0, 
				GameManager.getFont("font2", 24f, Color.WHITE), "STATE " + GameManager.getCurrentStage()
				,GameManager.VertexBufferObject);
		State.setPosition(GameManager.CAMERA_X * 90 / 100, GameManager.CAMERA_HEIGHT / 100 * 5);
		this.attachChild(State);
	}

	protected void InitEnermyTank (float px, float py){
		int pyStart = 0;
		for(int i=0 ; i < _TotalEnermy ; i++){
			EnermyIcon icon = new EnermyIcon(px + (i%2 * (EnermyIcon.GetSize() + 1)), pyStart + py);
			icon.setX( (GameManager.CAMERA_WIDTH - Math.abs(GameManager.CAMERA_X) - GameManager.MAP_SIZE) / 2 - EnermyIcon.GetSize()*2);
			_mEnermys.add(icon);
			
			this.attachChild(icon);
			
			if(i%2 == 1)
				pyStart += icon.GetSprite().getWidth() + 1;
		}
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		UpdateText();
		
		while (_mEnermys.size() > GameManager.CurrentMapManager.getTotalEnemyTanks())
			RemoveLastItem();
	}
	
	private void RemoveLastItem (){
		this.detachChild(_mEnermys.get(_mEnermys.size() -1));
		_mEnermys.remove(_mEnermys.size() - 1);
	}
	
}
