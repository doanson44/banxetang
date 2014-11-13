package org.xetang.tank;

import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.manager.GameMapManager;
import org.xetang.map.Map;


/**
 * 
 */
public class Normal extends Tank {

    /**
     * 
     */
    public Normal(int px, int py, Map map) {
    	super(px, py,map,(TiledTextureRegion) GameManager.getTexture("Player1"));
    	this.speed = 10;
    }
    
    @Override
    public void onLeft() {
    	// TODO Auto-generated method stub
    	tankSprite.setCurrentTileIndex(1);
    	super.onLeft();
    }

    @Override
    public void onRight() {
    	// TODO Auto-generated method stub
    	tankSprite.setCurrentTileIndex(2);
    	super.onRight();
    }
    
    @Override
    public void onForward() {
    	// TODO Auto-generated method stub
    	tankSprite.setCurrentTileIndex(3);
    	super.onForward();
    }
   
    @Override
    public void onBackward() {
    	// TODO Auto-generated method stub
    	tankSprite.setCurrentTileIndex(0);
    	super.onBackward();
    }
}