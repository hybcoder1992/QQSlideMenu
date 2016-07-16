package com.hyb.qqslidemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AnimationActivity extends Activity {

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);
		Animation anim_iv=AnimationUtils.loadAnimation(this, R.anim.anim_iv);
		imageView = (ImageView)findViewById(R.id.iv_anim);
		imageView.startAnimation(anim_iv);
	}
}
