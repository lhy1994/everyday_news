package BaseDetailPager;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lhycode.news.MainActivity;
import com.lhycode.news.R;
import com.lhycode.news.bean.NewsChannel.NewsChannelItem;
import com.lhycode.news.bean.NewsData.NewsMenuData;
import com.lhycode.news.bean.NewsData.NewsTabData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class NewsMenuDetailPager extends BaseDetailMenuPager implements OnPageChangeListener{

	public ViewPager viewPager;
	public List<TabDetailPager> tabDetailPagers;
	ArrayList<NewsTabData> newsTabDataList;
	private TabPageIndicator pageIndicator;
	ArrayList<NewsChannelItem> newsChannelItemList;

	public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children, ArrayList<String> result) {
		super(activity);
		newsTabDataList=children;
	}
	public NewsMenuDetailPager(Activity activity, ArrayList<NewsChannelItem> newsChannelItemList) {
		super(activity);
		this.newsChannelItemList=newsChannelItemList;
	}

	@Override
	public View initViews() {
		View view = View.inflate(activity, R.layout.news_menu_detail, null);
		viewPager = (ViewPager) view.findViewById(R.id.vp_news_detail);
		
		ViewUtils.inject(this, view);
		pageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		pageIndicator.setOnPageChangeListener(this);
		
		return view;
	}

	@Override
	public void initData() {
		super.initData();
		tabDetailPagers=new ArrayList<TabDetailPager>();
		for(int i=0;i<newsChannelItemList.size();i++)
		{
			TabDetailPager pager=new TabDetailPager(activity,newsChannelItemList.get(i).channelId);
			tabDetailPagers.add(pager);
		}
		viewPager.setAdapter(new MenuDetailAdapter());
		pageIndicator.setViewPager(viewPager);
	}
	@OnClick(R.id.btn_next)
	public void next(View view) {
		int currentItem=viewPager.getCurrentItem();
		viewPager.setCurrentItem(++currentItem);
	}

	class MenuDetailAdapter extends PagerAdapter {
		@Override
		public CharSequence getPageTitle(int position) {
//			return newsTabDataList.get(position).title;
			return newsChannelItemList.get(position).name;
		}

		@Override
		public int getCount() {
//			return tabDetailPagers.size();
			return newsChannelItemList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager=tabDetailPagers.get(position);
			container.addView(pager.rootView);
			pager.initData();
			return pager.rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		MainActivity mainActivity=(MainActivity) activity;
		SlidingMenu slidingMenu=mainActivity.getSlidingMenu();
		
		if(position==0)
		{
			slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);
		}else {
			slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_NONE);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

}
