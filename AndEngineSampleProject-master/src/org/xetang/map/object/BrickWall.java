package org.xetang.map.object;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;
import org.xetang.map.object.MapObjectFactory.ObjectType;

public class BrickWall extends Wall {

	public BrickWall(BrickWall brickWall) {
		super(brickWall);

		_sprite = new TiledSprite(brickWall.getX(), brickWall.getY(), brickWall
				.getSprite().getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(brickWall.getSprite().getWidth(), brickWall.getSprite()
				.getHeight());

		attachChild(_sprite);
	}

	public BrickWall(float posX, float posY) {
		super(MapObjectFactory.getFixtureDef(ObjectType.BrickWall),
				MapObjectFactory.getTextureRegion(ObjectType.BrickWall),
				MapObjectFactory.BRICK_WALL_CELL_PER_MAP, posX, posY);
	}

	@Override
	public MapObject clone() {
		return new BrickWall(this);
	}

	@Override
	public void doContact(IMapObject object) {
		// try {
		// if (object.getType() == ObjectType.Bullet) {
		// _sprite.setVisible(false);
		// _body.getFixtureList().get(0).setSensor(true);
		// DestroyHelper.add(this);
		// }
		// } catch (Exception e) {
		// Debug.d("Collsion", "Nothing to contact!");
		// }
	}

	@Override
	public ObjectType getType() {
		return ObjectType.BrickWall;
	}
}