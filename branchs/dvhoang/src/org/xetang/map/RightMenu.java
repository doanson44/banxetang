package org.xetang.map;

import java.util.ArrayList;

import org.andengine.entity.text.Text;
import org.xetang.manager.GameManager;
import org.xetang.map.item.EnermyIcon;
import org.xetang.root.GameEntity;


public class RightMenu extends GameEntity {

	protected Map _mMap;
	protected ArrayList<EnermyIcon> _mEnermys = new ArrayList<EnermyIcon>();
	protected int _TotalEnermy;
	public static Text Player1 = null;
	
	public RightMenu (float px, float py, Map map, int totalEnermy){
		
		_mMap = map;
		_TotalEnermy = totalEnermy;
		InitEnermyTank(px,py);
		UpdateText();
		AddStateText();
	}
	
	public static void UpdateText() {
		// TODO Auto-generated method stub
		if(Player1 != null)
			Player1.detachSelf();
		Player1 = new Text(GameManager.MAP_SIZE + 20, GameManager.MAP_SIZE -200, 
				GameManager.getFont("font2"), "P1: " + GameManager.CurrentMapManager.GetPlayer1Life()
				,GameManager.VertexBufferObject);
		GameManager.CurrentMap.attachChild(Player1);
	}
	
	public void AddStateText(){
		Text State = new Text(-70,0, 
				GameManager.getFont("font2"), "STATE " + GameManager.getCurrentStage()
				,GameManager.VertexBufferObject);
		this.attachChild(State);
	}

	protected void InitEnermyTank (float px, float py){
		int pyStart = 0;
		for(int i=0 ; i < _TotalEnermy ; i++){
			EnermyIcon icon = new EnermyIcon(px + (i%2 * (EnermyIcon.GetSize() + 1)), pyStart + py);
			_mEnermys.add(icon);
			
			this.attachChild(icon);
			
			if(i%2 == 1)
				pyStart += icon.GetSprite().getWidth() + 1;
		}
	}
	
	public void RemoveLastItem (){
		this.detachChild(_mEnermys.get(_mEnermys.size() -1));
	}
	
}
