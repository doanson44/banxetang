package org.xetang.map;

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

	public static void InitTextures (){
		
		Context context = GameManager.Context;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/map/");
		BitmapTextureAtlas mBitmapTexture = new BitmapTextureAtlas(GameManager.TextureManager, 52, 364, TextureOptions.DEFAULT);


		// Load Item
		TiledTextureRegion bomb = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTexture,GameManager.Context,
					"item/bomb.png", 0, 0,1, 1);

		TiledTextureRegion tank = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, context, "item/tank.png", 0, 52,1,1);
		TiledTextureRegion clock = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, context, "item/clock.png",0,104, 1,1);
		TiledTextureRegion gun = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, context, "item/gun.png", 0, 156,1,1);
		TiledTextureRegion hat = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, context, "item/helmet.png", 0, 208,1,1);
		TiledTextureRegion shovel = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, context, "item/shovel.png", 0, 260,1,1);
		TiledTextureRegion star = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTexture, context, "item/star.png", 0, 312,1,1);
		mBitmapTexture.load();
		
		Textures.put("Bomb", bomb);
		Textures.put("Clock", clock);
		Textures.put("Gun", gun);
		Textures.put("Helmet", hat);
		Textures.put("Shovel", shovel);
		Textures.put("Star", star);
		Textures.put("Tank", tank);
		
		
		// Load Tank
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas playerTExtureAtlas = new BitmapTextureAtlas(GameManager.TextureManager, 50, 50, TextureOptions.DEFAULT);
		TiledTextureRegion player1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTExtureAtlas, context, "Player1/normal.png", 0, 0,1,1);
		playerTExtureAtlas.load();
		Textures.put("Player1", player1);
		
		// Load Vong bao ve 
		BitmapTextureAtlas protectionTExtureAtlas = new BitmapTextureAtlas(GameManager.TextureManager, 100, 50, TextureOptions.DEFAULT);
		TiledTextureRegion protection = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(protectionTExtureAtlas, context, "protection/protection.png", 0, 0,2,1);
		protectionTExtureAtlas.load();
		
		Textures.put("Protection", protection);
		
		// Load icon right Menu
		BitmapTextureAtlas RightMenuAtlas = new BitmapTextureAtlas(GameManager.TextureManager, 64*4, 64, TextureOptions.DEFAULT);
		TiledTextureRegion Enermytank = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(RightMenuAtlas, context, "RightMenu/enermytank.png", 0, 0,1,1);
		TiledTextureRegion Flag = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(RightMenuAtlas, context, "RightMenu/enermytank.png", 64, 0,1,1);
		TiledTextureRegion ip = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(RightMenuAtlas, context, "RightMenu/enermytank.png", 64*2, 0,1,1);
		TiledTextureRegion LifeIcon = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(RightMenuAtlas, context, "RightMenu/LifeIcon.png", 64*3, 0,1,1);
		RightMenuAtlas.load();
		
	
		Textures.put("Enermytank", Enermytank);
		Textures.put("Flag", Flag);
		Textures.put("IP", ip);
		Textures.put("LifeIcon", LifeIcon);
	}
	
	public static BaseTextureRegion getTexture(String key) {
		return Textures.get(key);
	}
}
