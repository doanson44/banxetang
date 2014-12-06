package org.xetang.tank;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory2;

public class Flicker implements IUpdateHandler, IAnimationListener {

	protected AnimatedSprite mSprite;
	protected float SecPerFrame = 0;
	protected Tank mTank;
	protected int StopAnimate = 0;
	
	public Flicker(float px, float py) {
		mSprite = new AnimatedSprite(px + 3, py + 3,
				(ITiledTextureRegion) MapObjectFactory2.getTexture("Flicker"),
				GameManager.VertexBufferObject);
		mSprite.setSize(GameManager.LARGE_CELL_WIDTH - 10,GameManager.LARGE_CELL_HEIGHT - 10);
	}
	
	
	public void Animate(){
		mSprite.animate(new long[]{150,150,150,150},0,3,true,this);
	
	}
	
	public void SetTank (Tank tank){
		mTank = tank;
	}
	public AnimatedSprite GetSprite(){
		return mSprite;
	}

	public boolean IsStopAnimation(){
		return !mSprite.isAnimationRunning();
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		SecPerFrame += pSecondsElapsed;
		if(SecPerFrame > 1){
			SecPerFrame = 0;
			StopAnimate++;
			if(StopAnimate == 2){
				mSprite.stopAnimation();
				mSprite.detachSelf();
			}
		}

	}


	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
			int pInitialLoopCount) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
			int pOldFrameIndex, int pNewFrameIndex) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
			int pRemainingLoopCount, int pInitialLoopCount) {
		// TODO Auto-generated method stub
		if(mTank != null && StopAnimate == 1){
			GameManager.CurrentMap.AddEnermyTankToList(mTank);
			mTank = null;
		}
	}


	@Override
	public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
		// TODO Auto-generated method stub

	}
	
}
