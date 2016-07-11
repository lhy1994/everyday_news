package com.lhycode.news;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lhycode.news.fragment.ContentFragment;
import com.lhycode.news.fragment.LeftMenuFragment;

import android.R.transition;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_maina);

		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffset(300);
		initFragment();
	}

	private void initFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transition = fragmentManager.beginTransaction();
		transition.replace(R.id.left_menu, new LeftMenuFragment(),
				"FRAGMENT_LEFT_MENU");
		transition
				.replace(R.id.main, new ContentFragment(), "FRAGMENT_CONTENT");
		transition.commit();
	}

	public LeftMenuFragment getLeftFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fragmentManager
				.findFragmentByTag("FRAGMENT_LEFT_MENU");
		return fragment;
	}
	public ContentFragment getContentFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fragmentManager
				.findFragmentByTag("FRAGMENT_CONTENT");
		return fragment;
	}
	
}
