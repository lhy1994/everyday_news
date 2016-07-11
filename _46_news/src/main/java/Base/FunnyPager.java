package Base;

import com.lhycode.news.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import BaseDetailPager.BaseDetailMenuPager;
import BaseDetailPager.ImageFunnyDetailPager;
import BaseDetailPager.TabDetailPager;
import BaseDetailPager.TextFunnyDetailPager;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FunnyPager extends BasePager {

	private TabPageIndicator pageIndicator;
	private ViewPager viewPager;
	private View view;

	public FunnyPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initViews() {
		super.initViews();
	}

	@Override
	public void initData() {

		setSlidingMenuEnable(false);
		buttonMenu.setVisibility(View.INVISIBLE);
		title.setText("搞笑");
		view = View.inflate(activity, R.layout.funny_pager, null);

		viewPager = (ViewPager) view.findViewById(R.id.funny_viewpager);
		ViewUtils.inject(this, view);
		pageIndicator = (TabPageIndicator) view
				.findViewById(R.id.funny_indicator);

		viewPager.setAdapter(new FunnyPagerAdapter());
		pageIndicator.setViewPager(viewPager);
		content.removeAllViews();
		content.addView(view);

	}


	class FunnyPagerAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return "搞笑段子";
			} else if (position == 1) {
				return "搞笑图片";
			}
			return "not find";
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BaseDetailMenuPager pager = null;
			if (position == 0) {
				pager = new TextFunnyDetailPager(activity);

			} else if (position == 1) {
				pager = new ImageFunnyDetailPager(activity);
			}
			container.addView(pager.rootView);
			pager.initData();
			return pager.rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
