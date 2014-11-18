package org.xetang.tank;


import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
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
    	this.speed = 2;
    }
    
    @Override
    public void onLeft() {
    	// TODO Auto-generated method stub
    	tankSprite.setRotation(90);
    	super.onLeft();
    }

    @Override
    public void onRight() {
    	// TODO Auto-generated method stub
    	tankSprite.setRotation(270);
    	super.onRight();
    }
    
    @Override
    public void onForward() {
    	// TODO Auto-generated method stub
    	tankSprite.setRotation(180);
    	super.onForward();
    }
   
    @Override
    public void onBackward() {
    	// TODO Auto-generated method stub
    	tankSprite.setRotation(0);
    	super.onBackward();
    }
}