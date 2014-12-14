package org.xetang.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xetang.manager.GameManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RoundActivity extends Activity implements OnClickListener {

	class Round {
		public String ImageAssert;
		public int Index;
		public boolean IsUnlock;

		public Round(String image, int idx, boolean isUnlock) {
			ImageAssert = image;
			Index = idx;
			IsUnlock = isUnlock;
		}
	}

	final String DEFAUILT_IMAGE = "default.jpg";

	final int NUM_COLUMN = 1;
	final int NUM_ROW = 1;

	int titleHeight;
	int paddingWidth;
	int paddingHeight;
	int frameWidth;
	int frameHeight;
	int frameStartX;
	int frameStartY;
	DisplayMetrics display;

	List<Round> rounds = new ArrayList<RoundActivity.Round>();

	LinearLayout linear;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		calcFrameSize();

		initRootView();

		loadRounds();
		showRound();
	}

	private void initRootView() {

		// Root
		LinearLayout root = new LinearLayout(this);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		root.setBackgroundColor(Color.BLACK);

		// title
		TextView text = new TextView(this);
		text.setGravity(Gravity.CENTER);
		text.setTypeface(Typeface
				.createFromAsset(getAssets(), "font/font2.ttf"));
		text.setText("Select Stage");
		text.setTextColor(Color.WHITE);
		text.setTextSize(64f);
		root.addView(text, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
				titleHeight));

		// scroll view
		HorizontalScrollView horizontalView = new HorizontalScrollView(this);
		horizontalView.setBackgroundColor(Color.BLACK);
		horizontalView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		horizontalView.setHorizontalScrollBarEnabled(false);
		linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.HORIZONTAL);
		horizontalView.addView(linear, new HorizontalScrollView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		root.addView(horizontalView);
		this.setContentView(root);
	}

	private void calcFrameSize() {
		this.display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);

		this.titleHeight = display.heightPixels / 100 * 20;
		this.paddingWidth = display.widthPixels / 100 * 30;
		this.paddingHeight = display.heightPixels / 100 * 10;
		this.frameWidth = (display.widthPixels - (NUM_COLUMN + 1)
				* this.paddingWidth)
				/ NUM_COLUMN;
		this.frameHeight = (display.heightPixels - titleHeight - (NUM_ROW + 1)
				* this.paddingHeight)
				/ NUM_ROW;
		this.frameStartX = 0;
		this.frameStartY = 0;

	}

	private void showRound() {
		for (Round r : rounds) {
			addRoundView(r);
		}
	}

	@SuppressLint("NewApi")
	private void addRoundView(final Round r) {

		RelativeLayout layout = new RelativeLayout(this);

		// int x = frameStartX + numPage * display.widthPixels + paddingWidth +
		// (r.Index-1) % NUM_COLUMN * frameWidth;
		// int y = frameStartY + (r.Index-1) / NUM_COLUMN * frameHeight +
		// paddingHeight;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = this.frameWidth;
		params.height = this.frameHeight;

		params.leftMargin = paddingWidth;
		params.topMargin = paddingHeight;

		layout.setBackgroundColor(Color.WHITE);
		layout.setGravity(Gravity.CENTER);
		linear.addView(layout, params);

		try {
			ImageView image = new ImageView(this);
			image.setScaleType(ScaleType.FIT_XY);
			image.setImageBitmap(BitmapFactory.decodeStream(getAssets().open(
					r.ImageAssert)));
			layout.addView(image, new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT));

			Button b = new Button(this);
			b.setText("Stage " + r.Index);
			RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			paramsButton.setMargins(0, layout.getHeight() / 2 - b.getHeight()
					/ 2, 0, 0);

			layout.addView(b, paramsButton);
			b.setTextColor(Color.WHITE);
			b.setTextSize(32f);
			b.setTag(r);
			b.setOnClickListener(this);

		} catch (Exception e) {
		}

	}

	private void loadRounds() {
		try {
			String[] names = getAssets().list("gfx/round");

			for (String name : names) {
				if (!name.contains("default")) {
					int idx = Integer.parseInt(getNamePart(name));
					rounds.add(new Round(
							GameManager.getReachedStage() >= idx ? "gfx/round/"
									+ name : "gfx/round/" + DEFAUILT_IMAGE,
							idx, GameManager.getReachedStage() >= idx));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getNamePart(String name) {
		int start = name.lastIndexOf(File.separator);
		start = start == -1 ? 0 : start;
		int end = name.lastIndexOf(".");
		end = end == -1 ? name.length() : end;
		return name.substring(start, end);
	}

	@Override
	public void onClick(View view) {
		if (view.getTag() instanceof Round) {
			Round round = (Round) view.getTag();
			if (round.Index > GameManager.getReachedStage())
				return;
			Intent i = new Intent(this, GameActivity.class);

			i.putExtra("stage", round.Index);
			startActivityForResult(i, 1);
			this.overridePendingTransition(0, 0);
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
