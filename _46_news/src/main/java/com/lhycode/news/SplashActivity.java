package com.lhycode.news;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	RelativeLayout rlroot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rlroot = (RelativeLayout) findViewById(R.id.root);
		startAnimation();

	}

	private void startAnimation() {
		AnimationSet animationSet = new AnimationSet(false);
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);

		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);

		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(rotateAnimation);
		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

				jumoNextPage();
			}
		});
		rlroot.startAnimation(animationSet);
	}

	private void jumoNextPage() {
		
//		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean b = PreferenceUtils.getBoolean(this, "isShowed", false);
		if (!b) {
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		}else {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		}
		finish();
	}
}
