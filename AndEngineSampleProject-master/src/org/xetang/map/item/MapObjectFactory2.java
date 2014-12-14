package org.xetang.map.item;

import java.util.Hashtable;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.xetang.manager.GameManager;

import android.content.Context;

public class MapObjectFactory2 {

	static Hashtable<String, BaseTextureRegion> Textures = new Hashtable<String, BaseTextureRegion>();

	public static void InitTextures() {

		Context context = GameManager.Context;

		// Load Item
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas ItemsTExtureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 448, 64, TextureOptions.DEFAULT);
		TiledTextureRegion items = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(ItemsTExtureAtlas, context,
						"map/item/items.png", 0, 0, 7, 1);
		ItemsTExtureAtlas.load();
		Textures.put("Items", items);
		
		// Load Player Tank
		BitmapTextureAtlas playerTankTExtureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 256, 256, TextureOptions.DEFAULT);
		TiledTextureRegion playerTank = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTankTExtureAtlas, context,
						"tank/player_tanks.png", 0, 0, 4, 4);
		playerTankTExtureAtlas.load();
		Textures.put("PlayerTank", playerTank);
		
		// Load Enermy Tank
		BitmapTextureAtlas EnermyTankTExtureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 256, 192, TextureOptions.DEFAULT);
		TiledTextureRegion enermyTank = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(EnermyTankTExtureAtlas, context,
						"tank/enermy_tanks.png", 0, 0, 4, 3);
		EnermyTankTExtureAtlas.load();
		Textures.put("EnermyTank", enermyTank);
		
		// Load Enemy Bots


		BitmapTextureAtlas enemyBigMomTExtureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 128, 320, TextureOptions.DEFAULT);

		TiledTextureRegion enemyBigMom = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(enemyBigMomTExtureAtlas, context,
						"tank/big_mom.png", 0, 0, 2, 5);

		enemyBigMomTExtureAtlas.load();

		Textures.put("EnemyBigMom", enemyBigMom);

		// Load Vong bao ve
		BitmapTextureAtlas protectionTExtureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 100, 50, TextureOptions.DEFAULT);
		TiledTextureRegion protection = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(protectionTExtureAtlas, context,
						"protection/protection.png", 0, 0, 2, 1);
		protectionTExtureAtlas.load();

		Textures.put("Protection", protection);

		// Load icon right Menu
		BitmapTextureAtlas RightMenuAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 64, 64, TextureOptions.DEFAULT);
		TiledTextureRegion Enermytank = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(RightMenuAtlas, context,
						"RightMenu/enermytank.png", 0, 0, 1, 1);
		RightMenuAtlas.load();

		Textures.put("Enermytank", Enermytank);

		// Load Flicker
		BitmapTextureAtlas FlickerAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 256, 64, TextureOptions.DEFAULT);
		TiledTextureRegion Flicker = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(FlickerAtlas, context,
						"flicker/flicker.png", 0, 0, 4, 1);
		FlickerAtlas.load();
		Textures.put("Flicker", Flicker);
	}

	public static BaseTextureRegion getTexture(String key) {
		return Textures.get(key);
	}
}
