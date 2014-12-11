package org.xetang.map.object;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.helper.BlastQueryCallback;
import org.xetang.map.helper.CalcHelper;
import org.xetang.map.object.MapObjectFactory.ObjectType;

import com.badlogic.gdx.math.Vector2;

public class Blast extends BlowUp {

	public Blast(Blast blast) {
		super(blast);

		((AnimatedSprite) _sprite).animate(MapObjectFactory.BLAST_ANIMATE,
				false, MapObjectFactory.getBlowUpListener());
	}

	public Blast(float posX, float posY) {
		super(null, MapObjectFactory.getTextureRegion(ObjectType.BLAST), posX,
				posY, MapObjectFactory.BLAST_CELL_SIZE);
	}

	@Override
	public IMapObject clone() {
		return new Blast(this);
	}

	@Override
	protected void createSprite(TiledTextureRegion objectTextureRegion,
			float posX, float posY) {
		super.createSprite(objectTextureRegion, posX, posY);

		((AnimatedSprite) _sprite).animate(MapObjectFactory.BLAST_ANIMATE,
				false, MapObjectFactory.getBlowUpListener());
	}

	@Override
	public ObjectType getType() {
		return ObjectType.BLAST;
	}

	@Override
	public void blowUpAt(float posX, float posY) {
		super.blowUpAt(posX, posY);

		if (_targetObject != null) {
			destroySurroundingObjects();
		}
	}

	private void destroySurroundingObjects() {
		_inRangeBodies = new BlastQueryCallback();

		IBullet bullet = (IBullet) _ownObject;

		Vector2 bulletCenter = bullet.getBody().getWorldCenter();
		Vector2 targetCenter = _targetObject.getBody().getWorldCenter();
		float targetInsideRadius = CalcHelper.pixels2Meters(_targetObject
				.getCellWidth() / 2 - MapObjectFactory.TINY_CELL_SIZE);
		Vector2 blastRadius = bullet.getBlowRadius();

		Vector2 leftBound = null;
		Vector2 rightBound = null;

		switch (bullet.getDirection()) {

		case UP:

			leftBound = new Vector2(bulletCenter.x - blastRadius.x,
					targetCenter.y + targetInsideRadius - blastRadius.y);
			rightBound = new Vector2(bulletCenter.x + blastRadius.x,
					targetCenter.y + targetInsideRadius);
			break;

		case DOWN:

			leftBound = new Vector2(bulletCenter.x - blastRadius.x,
					targetCenter.y - targetInsideRadius);
			rightBound = new Vector2(bulletCenter.x + blastRadius.x,
					targetCenter.y - targetInsideRadius + blastRadius.y);
			break;

		case LEFT:

			leftBound = new Vector2(targetCenter.x + targetInsideRadius
					- blastRadius.y, bulletCenter.y - blastRadius.x
					/ GameManager.MAP_RATIO);
			rightBound = new Vector2(targetCenter.x + targetInsideRadius,
					bulletCenter.y + blastRadius.x / GameManager.MAP_RATIO);
			break;

		case RIGHT:

			leftBound = new Vector2(targetCenter.x - targetInsideRadius,
					bulletCenter.y - blastRadius.x / GameManager.MAP_RATIO);
			rightBound = new Vector2(targetCenter.x - targetInsideRadius
					+ blastRadius.y, bulletCenter.y + blastRadius.x
					/ GameManager.MAP_RATIO);
			break;

		default:
			break;
		}

		/*
		 * Dùng để debug, hiển thị phạm vi nổ của đạn
		 */
		// Rectangle rec = new Rectangle(leftBound.x * 32, leftBound.y * 32,
		// (rightBound.x - leftBound.x) * 32,
		// (rightBound.y - leftBound.y) * 32,
		// GameManager.VertexBufferObject);
		//
		// rec.setAlpha(0.25f);
		//
		// GameManager.Scene.attachChild(rec);

		_inRangeBodies.setBullet((IBullet) _ownObject);
		GameManager.PhysicsWorld.QueryAABB(_inRangeBodies, leftBound.x,
				leftBound.y, rightBound.x, rightBound.y);

		_inRangeBodies.goOff();
	}
}
