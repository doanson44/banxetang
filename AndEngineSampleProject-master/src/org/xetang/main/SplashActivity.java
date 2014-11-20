package org.xetang.main;

import org.nhom7.battlecity.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashActivity extends Activity implements OnCompletionListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		this.finish();
		overridePendingTransition(0, 0);
		Intent i = new Intent(this, RoundActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(i);
	}
}
