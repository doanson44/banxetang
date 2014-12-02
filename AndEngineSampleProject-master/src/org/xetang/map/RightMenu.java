package org.xetang.map;

import java.util.ArrayList;

import org.andengine.entity.text.Text;
import org.xetang.manager.GameManager;
import org.xetang.root.GameEntity;

import android.renderscript.Font;

public class RightMenu extends GameEntity {

	protected Map _mMap;
	protected ArrayList<EnermyIcon> _mEnermys = new ArrayList<EnermyIcon>();
	protected int _TotalEnermy;
	
	public RightMenu (float px, float py, Map map, int totalEnermy){
		
		_mMap = map;
		_TotalEnermy = totalEnermy;
		InitEnermyTank(px,py);
		UpdateText();
		AddStateText();
	}
	
	public void UpdateText() {
		// TODO Auto-generated method stub
		
		Text Player1 = new Text(GameManager.MAP_WIDTH + 20, GameManager.MAP_HEIGHT -200, 
				GameManager.getFont("font2"), "P1: " + GameManager.CurrentMapManager.getTotalPlayerTank()
				,GameManager.VertexBufferObject);
		this.attachChild(Player1);
	}
	
	public void AddStateText(){
		Text State = new Text(-150,0, 
				GameManager.getFont("font2"), "STATE " + GameManager.getCurrentStage()
				,GameManager.VertexBufferObject);
		this.attachChild(State);
	}

	protected void InitEnermyTank (float px, float py){
		int pyStart = 0;
		for(int i=0 ; i < _TotalEnermy ; i++){
			EnermyIcon icon = new EnermyIcon(px + (i%2 * (EnermyIcon.GetSize() + 5)), pyStart + py);
			_mEnermys.add(icon);
			
			this.attachChild(icon);
			
			if(i%2 == 1)
				pyStart += icon.GetSprite().getWidth() + 5;
		}
	}
	
	public void RemoveLastItem (){
		this.detachChild(_mEnermys.get(_mEnermys.size() -1));
	}
	
}
