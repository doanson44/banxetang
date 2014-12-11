package org.xetang.map;

import java.util.Stack;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontUtils;
import org.xetang.manager.GameManager;
import org.xetang.map.item.EnermyIcon;
import org.xetang.map.object.MapObjectFactory;
import org.xetang.root.GameEntity;

public class Frame extends GameEntity {

	public static final float FRAME_X = GameManager.MAP_SIZE
			+ GameManager.SMALL_CELL_SIZE;
	public static final float FRAME_Y = GameManager.SMALL_CELL_SIZE;

	public static final String STAGE_TEXT = "STAGE ";
	public static final String PLAYER_1_TEXT = "P1: ";
	public static final String PLAYER_2_TEXT = "P2: ";

	protected Stack<EnermyIcon> _enemies = new Stack<EnermyIcon>();
	protected int _enemyLeft;
	protected Text _player1Text = null;
	protected Text _player2Text = null;

	/*
	 * ĂN ĐI KU Load những thông tin phụ cho màn chơi VD: Số xe tăng địch, sổ
	 * điểm, mạng còn lại của người chơi... Nói chung là nơi chưa những thông
	 * tin bên lề (thêm vào từ từ)
	 */
	public Frame(int player1Life, int player2Life, int enemyNumber) {
		_enemyLeft = enemyNumber;

		createStageName();
		createTankIcons();
		createPlayerInfo(player1Life, player2Life);
	}

	private void createStageName() {

		Font font = GameManager.getFont("fontInMap");
		String stageName = STAGE_TEXT
				+ String.format("%2d", GameManager.getCurrentStage());
		float textLength = FontUtils.measureText(font, stageName);

		Text stageNameText = new Text(
				-(textLength + GameManager.SMALL_CELL_SIZE),
				GameManager.SMALL_CELL_SIZE, font, stageName,
				GameManager.VertexBufferObject);

		this.attachChild(stageNameText);
	}

	private void createTankIcons() {

		float yStart = (int) FRAME_X;

		EnermyIcon icon;
		for (int i = 0; i < _enemyLeft; i++) {

			icon = new EnermyIcon(
					FRAME_X
							+ (i % 2 * (EnermyIcon.GetSize() + MapObjectFactory.TINY_CELL_SIZE)),
					yStart + FRAME_Y);

			_enemies.push(icon);
			this.attachChild(icon);

			if (i % 2 == 1) {
				yStart += icon.GetSprite().getWidth()
						+ MapObjectFactory.TINY_CELL_SIZE;
			}

		}

		this.setIgnoreUpdate(true);
	}

	private void createPlayerInfo(int player1Life, int player2Life) {

		Font font = GameManager.getFont("fontInMap");
		String lifeText = PLAYER_1_TEXT + String.format("%2d", player1Life);
		float textLength = FontUtils.measureText(font, lifeText);

		_player1Text = new Text(-(textLength + GameManager.SMALL_CELL_SIZE),
				GameManager.LARGE_CELL_SIZE + GameManager.SMALL_CELL_SIZE,
				font, lifeText, GameManager.VertexBufferObject);
		this.attachChild(_player1Text);

		if (player2Life < 0) {
			return;
		}

		lifeText = PLAYER_2_TEXT + String.format("%2d", player2Life);
		textLength = FontUtils.measureText(font, lifeText);

		_player2Text = new Text(-(textLength + GameManager.SMALL_CELL_SIZE),
				GameManager.LARGE_CELL_SIZE * 3, font, lifeText,
				GameManager.VertexBufferObject);
		this.attachChild(_player2Text);
	}

	public void dropAnEnemy() {
		this.detachChild(_enemies.pop());
	}

	public void updatePlayer1Life(int life) {
		_player1Text.setText(PLAYER_1_TEXT + String.format("%2d", life));
	}

	public void updatePlayer2Life(int life) {
		_player2Text.setText(PLAYER_2_TEXT + String.format("%2d", life));
	}
}