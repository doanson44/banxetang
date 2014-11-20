package org.xetang.controller;

public interface IGameController {
	public void onLeft(); //Sự kiện khi bấm nút rẽ trái
	public void onRight(); //Sự kiện khi bấm nút rẽ phải
	public void onForward(); //Sự kiện khi bấm nút đi thẳng
	public void onBackward(); //Sự kiện khi bấm nút đi lùi
	public void onFire(); //Sự kiện khi bấm nút bắn
	public void onCancelMove(); // Sự kiện khi thả nút bắn
}
