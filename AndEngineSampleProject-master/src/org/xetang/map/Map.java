package org.xetang.map;

import java.util.List;

import org.xetang.root.GameEntity;
import org.xetang.tank.Tank;

/**
 * 
 */
public class Map extends GameEntity {
	//
	// public enum MapObjectType {
	// Empty(0), // Không có gì
	// Eagle(1), // Đại bàng
	// Brick(2), // Gạch bình thường
	// WeakBrick(3), // Gạch thiếu 1 phần
	// Stone(4), // Đá
	// WeakStone(5), // Đá thiếu 1 phần
	// Grass(6), // Cỏ
	// Water(7); // Nước
	//
	// private final int value;
	//
	// private MapObjectType(int value) {
	// this.value = value;
	// }
	//
	// public int getValue() {
	// return value;
	// }
	//
	// }

	MapObject[][] mMapMatrix; 	// Ma trận bản đồ, kích thước 13 dòng x 13 cột
								// x x x
								// x x x
								// x x x
								// ...

	List<Item> mItems;
	List<Tank> mEnermyTanks;
	List<Tank> mPlayerTanks;
	int mICurrentStage; // Chỉ số của màn chơi

	public Map(int iCurrentStage) {
		mICurrentStage = iCurrentStage;

		loadMapData();
	}
//
//	private void loadDefaultMapData() {
//		// Load bản đồ mặc định
//		// ...
//
//	}

	public void loadMapData() {
		// Tải bản đồ thứ mICurrentStage trong số các bản đồ có sẵn.
		// Hàm này thực hiện 2 công việc:
		// 1/ Load ma trận mMapMatrix
		// 2/ Phát sinh các đối tượng đồ họa tương ứng với ma trận bản đồ
		// ...

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
	
	public MapObject[][] getMapMatrix() {
		return mMapMatrix;
	}
}