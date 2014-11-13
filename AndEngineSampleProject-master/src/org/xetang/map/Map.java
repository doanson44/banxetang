package org.xetang.map;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObject.ObjectType;
import org.xetang.map.model.MapObjectBlockDTO;
import org.xetang.map.model.StageDTO;
import org.xetang.map.model.StageObjectDTO;
import org.xetang.root.GameEntity;
import org.xetang.tank.Tank;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * 
 */
public class Map extends GameEntity {

	List<Item> mItems = new ArrayList<Item>();
	List<Tank> mEnermyTanks = new ArrayList<Tank>();
	List<Tank> mPlayerTanks = new ArrayList<Tank>();
	List<Item> mItemRemoves = new ArrayList<Item>(); 	// danh sÃƒÆ’Ã‚Â¡ch cÃƒÆ’Ã‚Â¡c item cÃƒÂ¡Ã‚ÂºÃ‚Â§n phÃƒÂ¡Ã‚ÂºÃ‚Â£i remove sau 1 vÃƒÆ’Ã‚Â²ng for

	int mICurrentStage; // ChÃƒÂ¡Ã‚Â»Ã¢â‚¬Â° sÃƒÂ¡Ã‚Â»Ã¢â‚¬Ëœ cÃƒÂ¡Ã‚Â»Ã‚Â§a mÃƒÆ’Ã‚Â n chÃƒâ€ Ã‚Â¡i

	public Map(int iCurrentStage, StageDTO stage) {
		mICurrentStage = iCurrentStage;

		loadMapData(stage);
		createBorders();
	}

	public void loadMapData(StageDTO stage) {
		// TÃƒÂ¡Ã‚ÂºÃ‚Â£i bÃƒÂ¡Ã‚ÂºÃ‚Â£n Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Å“ thÃƒÂ¡Ã‚Â»Ã‚Â© mICurrentStage trong sÃƒÂ¡Ã‚Â»Ã¢â‚¬Ëœ cÃƒÆ’Ã‚Â¡c bÃƒÂ¡Ã‚ÂºÃ‚Â£n Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Å“ cÃƒÆ’Ã‚Â³ sÃƒÂ¡Ã‚ÂºÃ‚Âµn.
		// HÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n 2 cÃƒÆ’Ã‚Â´ng viÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡c:
		// 1/ Load ma trÃƒÂ¡Ã‚ÂºÃ‚Â­n mMapMatrix
		// 2/ PhÃƒÆ’Ã‚Â¡t sinh cÃƒÆ’Ã‚Â¡c Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Ëœi tÃƒâ€ Ã‚Â°ÃƒÂ¡Ã‚Â»Ã‚Â£ng Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Å“ hÃƒÂ¡Ã‚Â»Ã¯Â¿Â½a tÃƒâ€ Ã‚Â°Ãƒâ€ Ã‚Â¡ng ÃƒÂ¡Ã‚Â»Ã‚Â©ng vÃƒÂ¡Ã‚Â»Ã¢â‚¬Âºi ma trÃƒÂ¡Ã‚ÂºÃ‚Â­n bÃƒÂ¡Ã‚ÂºÃ‚Â£n Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Å“
		// ...

		// demo phan item
		mItems.add(new TankItem(this));
		mItems.add(new Bomb(this));
		mItems.add(new Clock(this));
		mItems.add(new Helmet(this));
		mItems.add(new Shovel(this));
		
		for (Item ite : mItems) {			
			this.attachChild(ite.GetSprite());
		}
		
		List<StageObjectDTO> objects = stage.getObjects();

		StageObjectDTO stageObject;
		MapObjectBlockDTO objectsBlock;
		for (int i = 0; i < objects.size(); i++) {
			stageObject = objects.get(i);

			for (int j = 0; j < stageObject.getAreas().size(); j++) {

				objectsBlock = MapObjectFactory.createObjectBlock(ObjectType
						.values()[stageObject.getId()], stageObject.getAreas()
						.get(j));

				attachBlock(objectsBlock);
			}
		}

	}

	private void attachBlock(MapObjectBlockDTO objectsBlock) {

		List<MapObject> objects = objectsBlock.getObjectsBlock();
		MapObject object;
		for (int i = 0; i < objects.size(); i++) {
			object = objects.get(i);

			object.transform(getX(), getY());
			object.putToWorld();
			attachChild(object);
		}
	}

	private void createBorders() {
		Rectangle ground = new Rectangle(-GameManager.BORDER_WIDTH,
				GameManager.MAP_HEIGHT, GameManager.MAP_WIDTH
						+ GameManager.BORDER_WIDTH * 2,
				GameManager.BORDER_WIDTH,
				GameManager.Context.getVertexBufferObjectManager());

		Rectangle left = new Rectangle(-GameManager.BORDER_WIDTH, 0,
				GameManager.BORDER_WIDTH, GameManager.MAP_HEIGHT,
				GameManager.Context.getVertexBufferObjectManager());

		Rectangle roof = new Rectangle(-GameManager.BORDER_WIDTH,
				-GameManager.BORDER_WIDTH, GameManager.MAP_WIDTH
						+ GameManager.BORDER_WIDTH * 2,
				GameManager.BORDER_WIDTH,
				GameManager.Context.getVertexBufferObjectManager());

		Rectangle right = new Rectangle(GameManager.MAP_WIDTH, 0,
				GameManager.BORDER_WIDTH, GameManager.MAP_HEIGHT,
				GameManager.Context.getVertexBufferObjectManager());

		FixtureDef borderFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f,
				0f);

		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, ground,
				BodyType.StaticBody, borderFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, roof,
				BodyType.StaticBody, borderFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, left,
				BodyType.StaticBody, borderFixtureDef);
		PhysicsFactory.createBoxBody(GameManager.PhysicsWorld, right,
				BodyType.StaticBody, borderFixtureDef);

		attachChild(ground);
		attachChild(left);
		attachChild(roof);
		attachChild(right);
	}

	public boolean isPointValid(float x, float y) {
		// KiÃƒÂ¡Ã‚Â»Ã†â€™m tra xem Ãƒâ€žÃ¢â‚¬ËœiÃƒÂ¡Ã‚Â»Ã†â€™m(x,y) hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n tÃƒÂ¡Ã‚ÂºÃ‚Â¡i thuÃƒÂ¡Ã‚Â»Ã¢â€žÂ¢c ÃƒÆ’Ã‚Â´ nÃƒÆ’Ã‚Â o cÃƒÂ¡Ã‚Â»Ã‚Â§a bÃƒÂ¡Ã‚ÂºÃ‚Â£n Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Å“,
		// vÃƒÆ’Ã‚Â  ÃƒÆ’Ã‚Â´ Ãƒâ€žÃ¢â‚¬ËœÃƒÆ’Ã‚Â³ xe tÃƒâ€žÃ†â€™ng cÃƒÆ’Ã‚Â³ thÃƒÂ¡Ã‚Â»Ã†â€™ Ãƒâ€žÃ¢â‚¬Ëœi vÃƒÆ’Ã‚Â o?
		// ...
		return false;
	}

	public int[] pointToCoordinate(float x, float y) {
		// XÃƒÆ’Ã‚Â¡c Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¹nh xem Ãƒâ€žÃ¢â‚¬ËœiÃƒÂ¡Ã‚Â»Ã†â€™m(x,y) thuÃƒÂ¡Ã‚Â»Ã¢â€žÂ¢c ÃƒÆ’Ã‚Â´ nÃƒÆ’Ã‚Â o trÃƒÆ’Ã‚Âªn bÃƒÂ¡Ã‚ÂºÃ‚Â£n Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Å“?
		// ...

		return new int[] { 0, 0 };
	}

	public List<Tank> getTotalEnermyTanks() {
		return mEnermyTanks;
	}

	public void addEnermyTank(Tank enermyTank) {
		mEnermyTanks.add(enermyTank);
	}

	public int getTotalPlayerTanks() {
		return mPlayerTanks.size();
	}

	public boolean isEagleAlive() {
		/*
		 * KiÃƒÂ¡Ã‚Â»Ã†â€™m tra Ãƒâ€žÃ¯Â¿Â½ÃƒÂ¡Ã‚ÂºÃ‚Â¡i bÃƒÆ’Ã‚Â ng cÃƒÆ’Ã‚Â²n sÃƒÂ¡Ã‚Â»Ã¢â‚¬Ëœng khÃƒÆ’Ã‚Â´ng ?
		 */

		return true; // dummy
	}
	
	public void Update (float pSecondsElapsed)
	{
		for (Item x : mItems) {	
			x.update(pSecondsElapsed);
			if(x.DestroySprite())
			{
				this.detachChild(x.mSprite);
				mItemRemoves.add(x);
			}
		}
		
		// xÃƒÂ³a tÃ¡ÂºÂ¥t cÃ¡ÂºÂ£ cÃƒÂ¡c item Ã„â€˜ÃƒÂ£ hÃ¡ÂºÂ¿t hÃ¡ÂºÂ¡n trong List item
		for (Item itemRemove : mItemRemoves) {
			mItems.remove(itemRemove);
		}
		mItemRemoves.clear();
	}
	
	// PhÃƒÂ¡ hÃ¡Â»Â§y tÃ¡ÂºÂ¥t cÃ¡ÂºÂ£ cÃƒÂ¡c kÃ¡ÂºÂ» Ã„â€˜Ã¡Â»â€¹ch 
		public void DestroyAllEnermy() {
			// TODO Auto-generated method stub
			for (Tank tank : mEnermyTanks) {
				tank.killSelf();
			}
			mEnermyTanks.clear();
		}

		// Hàm chuyển bức tường của đại bàng thành Đá trong 1 thời gian 
		// và phục hồi hư tổn cho tường gách sau khi hết thời gian 
		public void MakeStoneWallFortress() {
			// TODO Auto-generated method stub
			
		}

		// Hàm đóng băng tất cả kẻ địch
		public void FreezeTime() {
			// TODO Auto-generated method stub
			for (Tank tank : mEnermyTanks) {
				tank.FreezeSelf();
			}
		}

		public void AddExtraLife() {
			// TODO Auto-generated method stub
			
		}

}