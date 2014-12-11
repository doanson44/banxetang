package org.xetang.map.model;

import java.util.List;

public class StageDTO {
	int _id;
	int _lives;
	List<StageObjectDTO> _objects;
	List<String> _tanksNameQueue;

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}
	
	public int getLives() {
		return _lives;
	}

	public void setLives(int _lives) {
		this._lives = _lives;
	}

	public List<StageObjectDTO> getObjects() {
		return _objects;
	}

	public void setObjects(List<StageObjectDTO> objects) {
		this._objects = objects;
	}

	public List<String> getTanksNameQueue() {
		return _tanksNameQueue;
	}

	public void setTanksNameQueue(List<String> tanksNameQueue) {
		this._tanksNameQueue = tanksNameQueue;
	}
}
