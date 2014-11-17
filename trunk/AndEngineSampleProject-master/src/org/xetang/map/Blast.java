package org.xetang.map;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;
import org.xetang.map.helper.BlastQueryCallback;

import com.badlogic.gdx.math.Vector2;

public class Blast extends BlowUp {

	public Blast(Blast blast) {
		super(blast);

		((AnimatedSprite) _sprite).animate(MapObjectFactory.BLAST_ANIMATE,
				false, MapObjectFactory.getBlowUpListener());
	}

	public Blast(float posX, float posY) {
		super(null, MapObjectFactory.getBlastTextureRegion(),
				MapObjectFactory.BLAST_CELL_PER_MAP, posX, posY,
				MapObjectFactory.Z_INDEX_BLAST);
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
		return ObjectType.Blast;
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

		Vector2 blastRadius = bullet.getBlowRadius();
//		blastRadius.rotate(360 - CalcHelper.direction2Degrees(bullet
//				.getDirection()));
		
		Vector2 leftBound = null;
		Vector2 rightBound = null;

		switch (bullet.getDirection()) {

		case Up:

			leftBound = new Vector2(bulletCenter.x - blastRadius.x,
					targetCenter.y - blastRadius.y);
			rightBound = new Vector2(bulletCenter.x + blastRadius.x,
					targetCenter.y);
			break;

		case Down:

			leftBound = new Vector2(bulletCenter.x - blastRadius.x,
					targetCenter.y);
			rightBound = new Vector2(bulletCenter.x + blastRadius.x,
					targetCenter.y + blastRadius.y);
			break;

		case Left:

			leftBound = new Vector2(targetCenter.x - blastRadius.y,
					bulletCenter.y - blastRadius.x);
			rightBound = new Vector2(targetCenter.x,
					bulletCenter.y + blastRadius.x);
			break;

		case Right:

			leftBound = new Vector2(targetCenter.x,
					bulletCenter.y - blastRadius.x);
			rightBound = new Vector2(targetCenter.x + blastRadius.y,
					bulletCenter.y + blastRadius.x);
			break;

		default:
			break;
		}

		/*
		 * Dùng để debug
		 * Hiện phạm vi nổ của đạn
		 */
//		Rectangle rec = new Rectangle(leftBound.x * 32, leftBound.y * 32,
//				(rightBound.x - leftBound.x) * 32,
//				(rightBound.y - leftBound.y) * 32,
//				GameManager.VertexBufferObject);
//
//		rec.setAlpha(0.25f);
//
//		GameManager.Scene.attachChild(rec);

		_inRangeBodies.setBullet((IBullet) _ownObject);
		GameManager.PhysicsWorld.QueryAABB(_inRangeBodies, leftBound.x,
				leftBound.y, rightBound.x, rightBound.y);

		_inRangeBodies.goOff();
	}
}
