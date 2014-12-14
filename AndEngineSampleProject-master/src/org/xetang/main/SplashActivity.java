package org.xetang.main;

import org.nhom7.battlecity.R;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashActivity extends GameActivity implements
		OnCompletionListener {
	LoadResouceTask task;
	Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		final DisplayMetrics display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);

		VideoView view = new VideoView(this) {
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

				setMeasuredDimension(display.widthPixels, display.heightPixels);
			}
		};
		// view.setLayoutParams(new LayoutParams(display.widthPixels,
		// display.heightPixels));

		this.setContentView(view);
		String path = "android.resource://" + getPackageName() + "/"
				+ R.raw.intro;
		view.setVideoPath(path);

		view.setOnCompletionListener(this);
		view.start();

		task = new LoadResouceTask();

		thread = new Thread(task);
		thread.start();

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.finish();
		overridePendingTransition(0, 0);
		Intent i = new Intent(this, RoundActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(i);
	}

	class LoadResouceTask implements Runnable {
		boolean mIsCompleted = false;

		@Override
		public void run() {
			// GameManager.Activity = SplashActivity.this;
			// GameManager.Context = SplashActivity.this;
			// GameManager.TextureManager = new TextureManager();
			// GameManager.AssetManager = SplashActivity.this.getAssets();
			// GameManager.VertexBufferObject = new VertexBufferObjectManager();
			// GameManager.FontManager = new FontManager();
			// GameManager.MusicManager = new MusicManager();
			// GameManager.loadResource();

			mIsCompleted = true;
		}

		public boolean isCompleted() {
			return mIsCompleted;
		}
	}
}
