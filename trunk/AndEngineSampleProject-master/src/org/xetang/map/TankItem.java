package org.xetang.map;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.manager.TankManager;




/**
 * 
 */
public class TankItem extends Item {

    /**
     * 
     */
    public TankItem(Map map) {
    	super((TiledTextureRegion) MapObjectFactory2.getTexture("Tank"), map); 
    }

    @Override
    public void affect() {
    	// TODO Auto-generated method stub
    	GameManager.CurrentMapManager.AddNewLifeForTank();
    	RightMenu.UpdateText();
    }
}