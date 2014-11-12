package org.xetang.map.model;

import java.util.ArrayList;
import java.util.List;

import org.xetang.manager.GameManager;
import org.xetang.map.MapObject;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Pair;

public class MapObjectBlockDTO {
	private List<MapObject> _objectsBlock;
	private PointF _rootPosition;
	private int CellWidth, CellHeight;

	public MapObjectBlockDTO(int cellWidth, int cellHeight, int cellBlockWidth,
			int cellBlockHeight) {
		this(cellWidth, cellHeight, new Pair<Point, Point>(new Point(
				cellHeight, cellBlockWidth), new Point()));
	}

	public MapObjectBlockDTO(int cellWidth, int cellHeight,
			Pair<Point, Point> posAndSize) {
		_objectsBlock = new ArrayList<MapObject>(posAndSize.second.x
				* posAndSize.second.y);
		CellWidth = cellWidth;
		CellHeight = cellHeight;
		_rootPosition = new PointF(GameManager.SMALL_CELL_WIDTH
				* posAndSize.first.x, GameManager.SMALL_CELL_HEIGHT
				* posAndSize.first.y);
	}

	public void add(MapObject mapObject, int row, int col) {
		mapObject.setPosition(_rootPosition.x + CellWidth * col,
				_rootPosition.y + CellHeight * row);
		_objectsBlock.add(mapObject);
	}

	public void setPosition(float posX, float posY) {
		for (int i = 0; i < _objectsBlock.size(); i++) {
			_objectsBlock.get(i).transform(posX - _rootPosition.x,
					posY - _rootPosition.y);
		}

		_rootPosition = new PointF(posX, posY);
	}

	public List<MapObject> getObjectsBlock() {
		return _objectsBlock;
	}
}
