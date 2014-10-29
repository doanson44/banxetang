package org.xetang.map;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.xetang.root.GameEntity;
import org.xetang.tank.Tank;

/**
 * 
 */
public class Map extends GameEntity {

	public enum MapObjectType {
		Empty(0), // Không có gì
		Eagle(1), // Đại bàng
		Brick(2), // Gạch bình thường
		WeakBrick(3), // Gạch thiếu 1 phần
		Stone(4), // Đá
		WeakStone(5), // Đá thiếu 1 phần
		Grass(6), // Cỏ
		Water(7); // Nước

		private final int value;

		private MapObjectType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	MapObjectType[][] mMapMatrix; // Ma trận bản đồ, kích thước 13 dòng x 13 cột
									// x x x
									// x x x
									// x x x
									// ...
	Dictionary<Integer, MapObject> mMapObjects;
	List<Item> mItems;
	List<Tank> mTanks;
	int mIndex; // Chỉ số của màn chơi

	public Map() {
		mTanks = new ArrayList<Tank>();
	}

	private void loadDefaultMapData() {
		// Load bản đồ mặc định
		// ...

	}

	public void loadMapData(int idx) {
		// Tải bản đồ thứ idx trong số các bản đồ có sẵn.
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

	public void registerTank(Tank tank) {
		// Thêm đối tượng xe tăng vào bản đồ
		// Cần thực hiện các bước sau:
		// 1/ Thêm tank vào danh sách.
		// 2/ Từ tọa độ của Tank, xác định xem tọa độ đó nằm ở Ô nào của bản đồ.
		// 3/ Cập nhật lại MapMatrix.
		// ...
	}

	public void unregisterTank(Tank tank) {
		// Xóa đối tượng Tank khỏi bản đồ
		// Cần thực hiện các bước sau:
		// 1/ Xóa Tank khỏi danh sách.
		// 2/ Cập nhật lại giá trị của Ô có xe tăng trong MapMatrix.
		// ...
	}

	public void registerGameItem(Item item) {

	}

	public void unregisterGameItem(Item item) {

	}

}