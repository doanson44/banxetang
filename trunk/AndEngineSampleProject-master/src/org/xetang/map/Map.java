package org.xetang.map;

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

	List<Item> mItems;
	List<Tank> mEnermyTanks;
	List<Tank> mPlayerTanks;
	int mICurrentStage; // Chỉ số của màn chơi

	public Map(int iCurrentStage, StageDTO stage) {
		mICurrentStage = iCurrentStage;

		loadMapData(stage);
		createBorders();
	}

	public void loadMapData(StageDTO stage) {
		// Tải bản đồ thứ mICurrentStage trong số các bản đồ có sẵn.
		// Hàm này thực hiện 2 công việc:
		// 1/ Load ma trận mMapMatrix
		// 2/ Phát sinh các đối tượng đồ họa tương ứng với ma trận bản đồ
		// ...

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
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle left = new Rectangle(-GameManager.BORDER_WIDTH, 0,
				GameManager.BORDER_WIDTH, GameManager.MAP_HEIGHT,
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle roof = new Rectangle(-GameManager.BORDER_WIDTH,
				-GameManager.BORDER_WIDTH, GameManager.MAP_WIDTH
						+ GameManager.BORDER_WIDTH * 2,
				GameManager.BORDER_WIDTH,
				GameManager.Activity.getVertexBufferObjectManager());

		Rectangle right = new Rectangle(GameManager.MAP_WIDTH, 0,
				GameManager.BORDER_WIDTH, GameManager.MAP_HEIGHT,
				GameManager.Activity.getVertexBufferObjectManager());

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
		// Kiểm tra xem điểm(x,y) hiện tại thuộc ô nào của bản đồ,
		// và ô đó xe tăng có thể đi vào?
		// ...
		return false;
	}

	public int[] pointToCoordinate(float x, float y) {
		// Xác định xem điểm(x,y) thuộc ô nào trên bản đồ?
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
		 * Kiểm tra Đại bàng còn sống không ?
		 */

		return true; // dummy
	}

}