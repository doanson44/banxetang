package org.xetang.map.item;

import org.xetang.manager.GameManager;
import org.xetang.map.RightMenu;

public class TankItem extends Item {

    public TankItem() {
    	super();
    	_sprite.setCurrentTileIndex(5);
    }

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	GameManager.CurrentMapManager.AddNewLifeForTank();
    	RightMenu.UpdateText();
    }
}