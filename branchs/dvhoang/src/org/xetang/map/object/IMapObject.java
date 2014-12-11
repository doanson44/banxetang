package org.xetang.map.object;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.map.object.MapObjectFactory.ObjectType;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface IMapObject {

	public abstract IMapObject clone();

	/**
	 * Khởi tạo Body (Thể xác) của đối tượng ở vị trí hiện tại và đặt Body vào
	 * trong PhysicsWorld trong GameManager
	 */
	public void putToWorld();

	/**
	 * Khởi tạo Body (Thể xác) của đối tượng ở vị trí đưa vào và đặt Body vào
	 * trong PhysicsWorld trong GameManager
	 */
	public void putToWorld(float posX, float posY);

	/**
	 * Gán tỿa độ cho Sprite của đối tượng, phải được dùng <b>TRƯỚC</b> khi dùng
	 * hàm <code>putToWorld</code>
	 */
	public void setPosition(float posX, float posY);

	/**
	 * Lấy hoành độ của Sprite đối tượng
	 */
	public float getX();

	/**
	 * Lấy tung độ của Sprite đối tượng
	 */
	public float getY();

	/**
	 * Tịnh tiến Sprite đối tượng theo độ dỿi tương ứng
	 */
	public void transform(float x, float y);

	/**
	 * Lấy độ rộng Cell (ô) của Sprite đối tượng
	 */
	public float getCellWidth();

	/**
	 * Lấy độ cao Cell (ô) của Sprite đối tượng
	 */
	public float getCellHeight();

	/**
	 * Ŀối tượng còn sống hay không (đã bị loại khỿi trò chơi)
	 */
	public boolean isAlive();

	/**
	 * Gán trạng thái của đối tượng, cho biết đối tượng còn sống hay không
	 */
	public void setAlive(boolean alive);

	public TiledSprite getSprite();

	public Body getBody();

	/**
	 * Lấy thông tin khởi tạo Body đối tượng
	 */
	public FixtureDef getObjectFixtureDef();

	/**
	 * Thực hiện hành động khi đối tượng va chạm với đối tượng khác
	 * 
	 * @param object
	 *            : Ŀối tượng va chạm với đối tượng này
	 */
	public abstract void doContact(IMapObject object);

	public abstract ObjectType getType();
}
