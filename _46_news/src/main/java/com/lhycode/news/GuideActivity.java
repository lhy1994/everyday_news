package com.lhycode.news;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	private ViewPager vpGuide;
	private static final int[] imageviewIds = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };
	private List<ImageView> imageViews;
	private LinearLayout linearLayout;
	private View redPoint;
	private int width;
	private Button starButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_guide);
		redPoint = findViewById(R.id.red_point);
		starButton = (Button) findViewById(R.id.bt_start);

		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		linearLayout = (LinearLayout) findViewById(R.id.ll_pointGroup);
		initView();
		vpGuide.setAdapter(new GuideAdapter());
		vpGuide.setOnPageChangeListener(new GuidePageListener());
		starButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����sharedPreference
				// SharedPreferences preferences=getSharedPreferences("config",
				// MODE_PRIVATE);
				PreferenceUtils
						.setBoolean(GuideActivity.this, "isShowed", true);
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				finish();
			}
		});
	}

	private void initView() {
		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imageviewIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageviewIds[i]);
			imageViews.add(imageView);
		}

		for (int i = 0; i < imageviewIds.length; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					10, 10);
			View pointView = new View(this);
			if (i > 0) {
				params.leftMargin = 20;
			}
			pointView.setBackgroundResource(R.drawable.point_gray);
			pointView.setLayoutParams(params);
			linearLayout.addView(pointView);
		}
		// ��ȡ��ͼ��
		linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						width = linearLayout.getChildAt(1).getLeft()
								- linearLayout.getChildAt(0).getLeft();
						linearLayout.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageviewIds.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(imageViews.get(position));
			return imageViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}

	class GuidePageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub
			int distance = (int) (width * positionOffset) + position * width;
			RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) redPoint
					.getLayoutParams();
			params.leftMargin = distance;
			redPoint.setLayoutParams(params);
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			if (position == imageviewIds.length - 1) {
				starButton.setVisibility(View.VISIBLE);

			} else {
				starButton.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub

		}

	}
}
