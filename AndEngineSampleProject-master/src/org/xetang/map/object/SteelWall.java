package org.xetang.map;

import org.andengine.entity.sprite.TiledSprite;
import org.xetang.manager.GameManager;
import org.xetang.map.MapObjectFactory.ObjectType;

public class SteelWall extends Wall {

	public SteelWall(SteelWall steelWall) {
		super(steelWall);

		_sprite = new TiledSprite(steelWall.getX(), steelWall.getY(), steelWall
				.getSprite().getTiledTextureRegion(),
				GameManager.Activity.getVertexBufferObjectManager());
		_sprite.setSize(steelWall.getSprite().getWidth(), steelWall.getSprite()
				.getHeight());

		attachChild(_sprite);
	}

	public SteelWall(float posX, float posY) {
		super(MapObjectFactory.getFixtureDef(ObjectType.SteelWall),
				MapObjectFactory.getTextureRegion(ObjectType.SteelWall),
				MapObjectFactory.STEEL_WALL_CELL_PER_MAP, posX, posY);
	}

	@Override
	public MapObject clone() {
		return new SteelWall(this);
	}

	@Override
	public void doContact(IMapObject object) {
		// try {
		// if (object.getType() == ObjectType.Bullet
		// && DecideHelpder.canDestroy(this, (IBullet) object)) {
		// _sprite.setVisible(false);
		// _body.getFixtureList().get(0).setSensor(true);
		// // DestroyHelper.add(this);
		// }
		// } catch (Exception e) {
		// Debug.d("Collsion", "Nothing to contact!");
		// }
	}

	@Override
	public ObjectType getType() {
		return ObjectType.SteelWall;
	}
}