package org.xetang.map.object;

import com.badlogic.gdx.math.Vector2;

public interface IBlowUp extends IMapObject {

	/**
	 * Gán đối tượng tác nhân gây nên BlowUp (vụ nổ). <br>
	 * �?i chung với hàm <code>setTargetObject</code>
	 * 
	 * @param ownObject
	 *            : �?ối tượng tác nhân
	 */
	public void setOwnObject(IMapObject ownObject);

	/**
	 * Gán đối tượng mục tiêu mà tác nhân ra tay <br>
	 * �?i chung với hàm <code>setOwnObject</code>
	 * 
	 * @param targetObject
	 *            : �?ối tượng nạn nhân
	 */
	public void setTargetObject(IMapObject targetObject);

	/**
	 * Thực hiện nổ có <b>tâm</b> ngay vị trí hiện tại
	 */
	public void blowUpAtHere();

	/**
	 * Thực hiện nổ có <b>tâm</b> tại vị trí tương ứng
	 */
	public void blowUpAt(Vector2 pos);

	/**
	 * Thực hiện nổ có <b>tâm</b> tại vị trí tương ứng
	 */
	public void blowUpAt(float posX, float posY);
}
