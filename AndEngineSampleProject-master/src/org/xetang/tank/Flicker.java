package org.xetang.tank;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.item.MapObjectFactory2;

public class Flicker implements IUpdateHandler, IAnimationListener {

	protected AnimatedSprite mSprite;
	protected float SecPerFrame = 0;
	protected Tank mTank;
	protected int StopAnimate = 0;
	
	public Flicker(float px, float py) {
		mSprite = new AnimatedSprite(px + 3, py + 3,
				(ITiledTextureRegion) MapObjectFactory2.getTexture("Flicker"),
				GameManager.VertexBufferObject);
		mSprite.setSize(GameManager.LARGE_CELL_SIZE - 10,GameManager.LARGE_CELL_SIZE - 10);
		mSprite.setVisible(false);
	}
	
	
	public void Animate(){
		mSprite.animate(new long[]{150,150,150,150}, 0, 3, 3, this);
	
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
		SecPerFrame += pSecondsElapsed;
		if(SecPerFrame > 1){
			SecPerFrame = 0;
			StopAnimate++;
			if(StopAnimate == 2){
				mSprite.stopAnimation();
				mSprite.setVisible(false);
				mTank.setAppearingDone();
			}
		}

	}


	@Override
	public void reset() {
		
	}


	@Override
	public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
			int pInitialLoopCount) {
		
	}


	@Override
	public void onAnimationFrameChanged(final AnimatedSprite pAnimatedSprite,
			int pOldFrameIndex, int pNewFrameIndex) {
		
	}


	@Override
	public void onAnimationLoopFinished(final AnimatedSprite pAnimatedSprite,
			int pRemainingLoopCount, int pInitialLoopCount) {
		
	}


	@Override
	public void onAnimationFinished(final AnimatedSprite pAnimatedSprite) {
		mTank.setAppearingDone();
		
		GameManager.Engine.runOnUpdateThread(new Runnable() {	
			@Override
			public void run() {
				pAnimatedSprite.detachSelf();				
			}
		});
		
	}


	public void setAppearing() {
		mSprite.setVisible(true);
	}
	
	
}
