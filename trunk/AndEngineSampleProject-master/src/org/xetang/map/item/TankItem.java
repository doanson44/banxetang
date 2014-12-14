package org.xetang.map.item;

import org.xetang.manager.GameManager;

public class TankItem extends Item {

    public TankItem() {
    	super();
    	_sprite.setCurrentTileIndex(5);
    }

    @Override
    public void affect() {
    	GameManager.CurrentMapManager.AddNewLifeForTank();
  
    }
}