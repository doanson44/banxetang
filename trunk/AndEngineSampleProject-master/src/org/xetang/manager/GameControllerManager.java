package org.xetang.manager;

import java.util.Dictionary;
import java.util.Hashtable;



import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;


public class GameControllerManager {
	public static final int SIZE_CELL = 32;

	static Dictionary<String, TiledTextureRegion> mResources;

	
	static {
		mResources = new Hashtable<String, TiledTextureRegion>();
	}

	public static void loadResource() {
		// Tải tài nguyên của Controller ở đây, vd: ảnh rẽ trái,...
		// ...

		// Ví dụ mẫu, load nút Fire
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/controller/");
		BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 90, 90);
		TiledTextureRegion texture = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"fire_button_90.png", 0, 0, 1, 1);
		textureAtlas.load();
		mResources.put("fire button", texture);

		
		
		// 4 nút di chuyển
		String[] name = {"button up", "button_up_sprite_64.png",
						"button right", "button_right_sprite_64.png",
						"button down", "button_down_sprite_64.png",
						"button left", "button_left_sprite_64.png"};
		
		BitmapTextureAtlas textureAtlasMove = new BitmapTextureAtlas(
				GameManager.TextureManager, SIZE_CELL*4 * name.length/2, SIZE_CELL*2);
		
		for (int i = 0; i < name.length; i += 2) {
			TiledTextureRegion t = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(textureAtlasMove, GameManager.AssetManager,
							name[i+1], i/2*SIZE_CELL*4, 0, 2, 1);
			
			
			mResources.put(name[i], t);
		}
		textureAtlasMove.load();
		
		
		//middle circle
		textureAtlas = new BitmapTextureAtlas(
				GameManager.TextureManager, 128, 128, TextureOptions.BILINEAR);
		TiledTextureRegion textureMiddle = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(textureAtlas, GameManager.AssetManager,
						"circle1_128.png", 0, 0, 1, 1);
		textureAtlas.load();
		mResources.put("middle button", textureMiddle);
		
	}
	
	

	public static TiledTextureRegion getResource(String key) {
		return mResources.get(key);
	}



}
