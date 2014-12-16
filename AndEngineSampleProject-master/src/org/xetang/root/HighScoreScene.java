package org.xetang.root;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.xetang.manager.GameItemManager;
import org.xetang.manager.GameManager;
import org.xetang.manager.TankManager;
import org.xetang.map.item.Item;



import org.xetang.tank.BigMom;
import org.xetang.tank.GlassCannon;
import org.xetang.tank.Normal;
import org.xetang.tank.Racer;
import org.xetang.tank.Tank;

import com.badlogic.gdx.math.Vector2;

/**
 * Màn hình hiển thị điểm của người chơi đạt được
 * @author nhanbach
 * @since 02/12/2014 
 *
 */
public class HighScoreScene extends Scene {
	

	Hashtable<String, Object> mValues = new Hashtable<String, Object>();
	Hashtable<String, Entity> mEntities = new Hashtable<String, Entity>();
	
	Font 		mFont 			= GameManager.getFont("font2", 24f, Color.WHITE_ARGB_PACKED_INT);
	long 		mPrevTime 		= 0;
	final int 	mDurationMilis 	= 100;
	boolean 	mIsWin			= false;
	boolean		mIsShowed 		= false;
	boolean 	mIsUpdateScoreDone = false;
	boolean 	mFlag = true;
	
	public HighScoreScene(){
		drawTestSquare();	
	}

	private void drawTestSquare() {
		Rectangle left = new Rectangle(0, 0, 2, GameManager.CAMERA_HEIGHT, GameManager.VertexBufferObject);
		Rectangle top = new Rectangle(0, 0, GameManager.CAMERA_WIDTH, 2, GameManager.VertexBufferObject);
		Rectangle right = new Rectangle(GameManager.CAMERA_WIDTH-2, 0, 2, GameManager.CAMERA_HEIGHT, GameManager.VertexBufferObject);
		Rectangle bottom = new Rectangle(0, GameManager.CAMERA_HEIGHT-2, GameManager.CAMERA_WIDTH, 2, GameManager.VertexBufferObject);

		this.attachChild(left);
		this.attachChild(top);
		this.attachChild(right);
		this.attachChild(bottom);

	}

	private void restoreCamera() {
		GameManager.Camera.set(GameManager.CAMERA_X, GameManager.CAMERA_Y, GameManager.CAMERA_X + GameManager.CAMERA_WIDTH,  GameManager.CAMERA_Y + GameManager.CAMERA_HEIGHT);
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if (!mIsShowed) return;
		
		if (System.currentTimeMillis() - mPrevTime > mDurationMilis)
		{
			updateScoreText("player1");
			
			if (mIsUpdateScoreDone && mFlag){
				checkAmazingScore("player1");
				mFlag = false;
			}
		
			mPrevTime = System.currentTimeMillis();
		}
		
			
	}

	private void updateScoreText(String player) {

		boolean bNext = false;
		
		//normal tank text
		bNext = updateTextForTank(player, "normal") || bNext;
		
		//racer 
		bNext = updateTextForTank(player, "racer") || bNext;
		
		//cannon 
		bNext = updateTextForTank(player, "cannon") || bNext;

		
		//bigmom 
		bNext = updateTextForTank(player, "bigmom") || bNext;
		
		if (!bNext){
			mIsUpdateScoreDone = true;
			return;
		}

		GameManager.getSound("score").play();

	}

	private void checkAmazingScore(String player) {
		int curHighScore = mValues.get("total score " + player)!=null ? (Integer)mValues.get("total score " + player) : 0 ;
		
		if ( curHighScore > GameManager.getHighScore())
		{
			GameManager.getMusic("amazing_score").play();
			GameManager.newHighScore(curHighScore);
		}
	}

	private boolean updateTextForTank(String player, String tank ) {
		Text t;
		int iTotal;
		int iCur;
		boolean bNext = false;
		TiledSprite pivot = (TiledSprite)mEntities.get(tank + " tank");

		t = (Text)mEntities.get(String.format("%s text %s", tank, player));
		iTotal = mValues.get(String.format("%s tank %s", tank, player))==null? 
									0 : (Integer)mValues.get(String.format("%s tank %s", tank, player));
		iCur = mValues.get(String.format("cur %s tank %s", tank, player))==null? 
								0 : (Integer)mValues.get(String.format("cur %s tank %s", tank, player));
		//he so
		int ICOR = tank == "normal" ? 100 : tank == "racer" ? 200 : tank == "cannon" ? 300 : 400;
		
		if (iTotal > iCur)
		{
			iCur++;
			mValues.put(String.format("cur %s tank %s", tank, player), iCur);
			t.setText(String.format("%d PTS     % 2d", ICOR * iCur, iCur));
			t.setPosition(pivot.getX() - t.getWidth() - 10, pivot.getY() + pivot.getHeight()/2 - t.getHeight()/2);
			bNext = true;
		}
		return bNext;
	}

	private void setupNewCamera() {
		GameManager.Camera.offsetCenter(0, 0);
		GameManager.Camera.set(0, 0, GameManager.CAMERA_WIDTH, GameManager.CAMERA_HEIGHT);
		GameManager.Camera.setHUD(null);
	}

	private void backupCamera() {
		
	}

	/**
	 * Vẽ những thông tin phụ như:
	 * 	<ul>
	 * 	<li>Tiêu đề</li>
	 *  <li>Màn chơi</li>
	 *  <li>Điểm cao nhất</li>
	 *  </ul>
	 */
	private void drawDecorations() {
		//high score
		Font f = GameManager.getFont("font2", 32f, Color.WHITE_ARGB_PACKED_INT);
		Text highscore = new Text(0, 0, f, 
							String.format("hi-score      %d", GameManager.getHighScore()),
							GameManager.VertexBufferObject);
		highscore.setPosition(GameManager.CAMERA_WIDTH/2 - highscore.getWidth()/2, GameManager.CAMERA_HEIGHT / 100 * 10);
		highscore.setColor(Color.RED);
		mEntities.put("high score", highscore);

		
		
		//stage
		Text stage = new Text(0, 0, f,
							String.format("stage      %d", GameManager.getCurrentStage()),
							GameManager.VertexBufferObject);
		stage.setPosition(GameManager.CAMERA_WIDTH/2 - stage.getWidth()/2, highscore.getY() + highscore.getHeight()*1.7f);
		stage.setColor(Color.WHITE);
		mEntities.put("stage", stage);

		
		//text player1
		f = GameManager.getFont("font2", 32f, Color.RED_ARGB_PACKED_INT);
		Text textPlayer1 = new Text(GameManager.CAMERA_WIDTH / 100 * 10, 
								GameManager.CAMERA_HEIGHT / 100 * 25, 
								f, 
								"i-player", 
								GameManager.VertexBufferObject);
		mEntities.put("text player1", textPlayer1);
		


		
		//sample tank
		Vector2 pivot = new Vector2(0, GameManager.Camera.getHeight()*0.33f);
		
		TiledSprite normalTank = new TiledSprite(0, 0, 
				(ITiledTextureRegion) GameManager.getTexture("sample normal tank"), 
				GameManager.VertexBufferObject);
		normalTank.setPosition(GameManager.CAMERA_WIDTH/2 - normalTank.getWidth()/2, pivot.y);
		mEntities.put("normal tank", normalTank);

		TiledSprite racerTank = new TiledSprite(0, 0, 
				(ITiledTextureRegion) GameManager.getTexture("sample racer tank"), 
				GameManager.VertexBufferObject);
		racerTank.setPosition(GameManager.CAMERA_WIDTH/2 - racerTank.getWidth()/2, pivot.y + normalTank.getHeight()*1.5f);
		mEntities.put("racer tank", racerTank);

		TiledSprite cannonTank = new TiledSprite(0, 0, 
				(ITiledTextureRegion) GameManager.getTexture("sample cannon tank"), 
				GameManager.VertexBufferObject);
		cannonTank.setPosition(GameManager.CAMERA_WIDTH/2 - cannonTank.getWidth()/2, pivot.y + normalTank.getHeight()*3.0f);
		mEntities.put("cannon tank", cannonTank);

		TiledSprite bigmomTank = new TiledSprite(0, 0, 
				(ITiledTextureRegion) GameManager.getTexture("sample bigmom tank"), 
				GameManager.VertexBufferObject);
		bigmomTank.setPosition(GameManager.CAMERA_WIDTH/2 - bigmomTank.getWidth()/2, pivot.y + normalTank.getHeight()*4.5f);
		mEntities.put("bigmom tank", bigmomTank);
		
		//next button
		f = GameManager.getFont("font2", 48f, Color.WHITE_ARGB_PACKED_INT);
		Text nextText = new Text(0, 0, f,
					mIsWin ? "Next >>": "Again ^-^", 
					10,
					GameManager.VertexBufferObject){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown())
					this.setColor(Color.GREEN);
				else if (pSceneTouchEvent.isActionUp())
				{
					this.setColor(Color.WHITE);
					if (mIsWin)
						nextRound();
					else
						replayRound();
				}
				else
					this.setColor(Color.WHITE);

				return true;
			}
		};
		nextText.setPosition(GameManager.CAMERA_WIDTH/2 - nextText.getWidth()/2,
							GameManager.CAMERA_HEIGHT*0.95f - nextText.getHeight());
		this.registerTouchArea(nextText);
		mEntities.put("next button", nextText);

		
		Enumeration<Entity> entities = mEntities.elements();
		Entity e;
		while (entities.hasMoreElements()){
			e = entities.nextElement();
			this.attachChild(e);
		}
		
	}
	
	/**
	 * Chơi lại màn đã thua
	 */
	protected void replayRound() {
		GameManager.switchToScene("game", null);
	}

	/**
	 * Qua man ke tiep
	 */
	protected void nextRound() {
		GameManager.nextStage();
		GameManager.switchToScene("game", null);
	}

	/**
	 * Thể hiện điểm số của người chơi ra màn hình
	 * @param player Tên người chơi
	 */
	private void drawScorePlayer(String player) {	
		//total score
		Text totalScoreText = new Text(GameManager.CAMERA_WIDTH / 100 * 35, 
				GameManager.CAMERA_HEIGHT / 100 * 25, 
				mFont, 
				String.valueOf( (Integer)mValues.get("total score " + player)), 
				GameManager.VertexBufferObject);
		totalScoreText.setColor(0.75f, 0.75f, 0.2f);
		this.attachChild(totalScoreText);
		
		//normal tank score
		drawTankScore(player, "normal");
		
		//racer tank score
		drawTankScore(player, "racer");

		//cannon tank score
		drawTankScore(player, "cannon");


		//bigmom tank score
		drawTankScore(player, "bigmom");


	}

	private void drawTankScore(String player, String tank) {
		TiledSprite pivot = (TiledSprite)mEntities.get(tank + " tank");
		Text score = new Text(GameManager.CAMERA_WIDTH / 100 * 25, 0, 
						mFont,
						String.format("%d PTS     % 2d", 0, 0),
						15,
						GameManager.VertexBufferObject);
		score.setPosition(pivot.getX() - score.getWidth() - 10, pivot.getY() + pivot.getHeight()/2 - score.getHeight()/2);
		mEntities.put(tank + " text "+ player, score);
		this.attachChild(score);
	}

	/**
	 * Tính điểm cho player
	 * @param player Tên player.
	 * @return void
	 */
	private void calcScorePlayer(String player) {
		List<Tank> defenseTanks = TankManager.getDefeatedTank(player);
		mValues.put("defense tank " + player, defenseTanks) ;
		mValues.put("pickup item " + player, GameItemManager.getInstance().getPickUpItem(player));
		mValues.put("normal tank " + player, countNormalTank(defenseTanks));
		mValues.put("racer tank " + player, countRacerTank(defenseTanks));
		mValues.put("cannon tank " + player, countCannonTank(defenseTanks));
		mValues.put("bigmom tank " + player, countBigMomTank(defenseTanks));
		
		@SuppressWarnings("unchecked")
		int total = calcTotalScore((List<Tank>)mValues.get("defense tank " + player), (List<Item>)mValues.get("pickup item " + player));
		mValues.put("total score " + player, total);
	}

	private int countBigMomTank(List<Tank> defenseTanks) {
		int total = 0;
		for (Tank tank : defenseTanks) {
			if (tank instanceof BigMom)
				total++;
		}
		return total;
	}

	private int countCannonTank(List<Tank> defenseTanks) {
		int total = 0;
		for (Tank tank : defenseTanks) {
			if (tank instanceof GlassCannon)
				total++;
		}
		return total;
	}

	private int countRacerTank(List<Tank> defenseTanks) {
		int total = 0;
		for (Tank tank : defenseTanks) {
			if (tank instanceof Racer)
				total++;
		}
		return total;
	}

	private int countNormalTank(List<Tank> defenseTanks) {
		int total = 0;
		for (Tank tank : defenseTanks) {
			if (tank instanceof Normal)
				total++;
		}
		return total;
	}

	private int calcTotalScore(List<Tank> tanks, List<Item> items) {
		int total = 0;
		
		
		for (Tank tank : tanks) {
			total += tank.getBonusPoint();
		}
		
		
		for (Item item : items) {
			total += item.getBonusPoint();
		}
		
		Debug.d(GameManager.TANK_TAG, String.format("tank: %d  item: %d", tanks.size(), items.size()));
		return total;
	}

	public void onSwitched(String action, Object data) {
		if (action == GameManager.ACTION_SCENE_OPEN){
			backupCamera();
			setupNewCamera();
			mIsWin = data != null ? (Boolean)data : false;
			mIsUpdateScoreDone = false;
			drawDecorations();
			calcScorePlayer("player1");
			drawScorePlayer("player1");
			
			mIsShowed = true;
			mFlag = true;

		}
		else if (action == GameManager.ACTION_SCENE_CLOSE)
		{
			restoreCamera();
			
			GameManager.getMusic("amazing_score").pause();
			GameManager.getMusic("amazing_score").seekTo(0);
			TankManager.resetData();
			GameItemManager.getInstance().resetData();
			GameManager.saveData();
			
			int curHighScore = mValues.get("total score player1" )!=null ? (Integer)mValues.get("total score player1") : 0 ;
			GameManager.newHighScore(curHighScore);

			mIsShowed = false;
			cleanAllText();
			cleanAllTouchHandler();
		}
		
	}

	private void cleanAllTouchHandler() {
		this.clearTouchAreas();
		this.clearUpdateHandlers();
	}

	private void cleanAllText() {
		this.mChildren.clear();
		mEntities.clear();
	}
	
}
