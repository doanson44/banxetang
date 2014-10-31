package org.xetang.root;


/**
 * 
 */
public class Frame extends GameEntity {

	protected int _totalTanksLeft;

	/*
     * ĂN ĐI KU
     * Load những thông tin phụ cho màn chơi
     * VD: Số xe tăng địch, sổ điểm, mạng còn lại của người chơi...
     * Nói chung là nơi chưa những thông tin bên lề (thêm vào từ từ)
     */
    public Frame(int currentStage) {
		// TODO Auto-generated constructor stub
	}

	public void update(int totalEnermyTanks) {
		_totalTanksLeft = totalEnermyTanks;
	}

}