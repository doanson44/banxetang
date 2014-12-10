package org.xetang.map.object;

import org.xetang.manager.GameManager.Direction;
import org.xetang.tank.Tank;

import com.badlogic.gdx.math.Vector2;

public interface IBullet extends IMapObject {

	/**
	 * Khởi tạo thông số cho Bullet (đạn)
	 * 
	 * @param damage
	 *            : Sát thương
	 * @param speed
	 *            : Tốc độ bay
	 * @param blowRadius
	 *            : Phạm vi nổ
	 */
	public void initSpecification(int damage, float speed, Vector2 blowRadius);

	/**
	 * Gán Tank (xe tăng) bắn ra viên đạn này
	 */
	public void setTank(Tank tank);

	public int getDamage();

	public Direction getDirection();

	public Vector2 getBlowRadius();

	/**
	 * Lên nòng cho đạn theo hướng tương ứng
	 */
	public void readyToFire(Direction direction);

	/**
	 * Bắn đạn 
	 */
	public void beFired();

	/**
	 * Lấy vị trí <b>đầu</b> đạn
	 */
	public Vector2 getTopPoint();
}
