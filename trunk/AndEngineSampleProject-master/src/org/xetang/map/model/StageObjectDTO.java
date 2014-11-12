package org.xetang.map.model;

import java.util.List;

import android.graphics.Point;
import android.util.Pair;

public class StageObjectDTO {
	int _id;
	List<Pair<Point, Point>> _areas; // Position and size

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public List<Pair<Point, Point>> getAreas() {
		return _areas;
	}

	public void setAreas(List<Pair<Point, Point>> areas) {
		this._areas = areas;
	}
}
