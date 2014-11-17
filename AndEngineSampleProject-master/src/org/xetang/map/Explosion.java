package org.xetang.map;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.map.MapObjectFactory.ObjectType;

public class Explosion extends BlowUp {

	public Explosion(Explosion explosion) {
		super(explosion);
		
		((AnimatedSprite) _sprite).animate(MapObjectFactory.EXPLOSION_ANIMATE,
				false, MapObjectFactory.getBlowUpListener());
	}

	public Explosion(float posX, float posY) {
		super(null, MapObjectFactory.getExplosionTextureRegion(),
				MapObjectFactory.EXPLOSION_CELL_PER_MAP, posX, posY,
				MapObjectFactory.Z_INDEX_EXPLOSION);
	}

	@Override
	public IMapObject clone() {
		return new Explosion(this);
	}

	@Override
	protected void createSprite(TiledTextureRegion objectTextureRegion,
			float posX, float posY) {
		super.createSprite(objectTextureRegion, posX, posY);

		((AnimatedSprite) _sprite).animate(MapObjectFactory.EXPLOSION_ANIMATE,
				false, MapObjectFactory.getBlowUpListener());
	}

	@Override
	public ObjectType getType() {
		return ObjectType.Explosion;
	}
}
