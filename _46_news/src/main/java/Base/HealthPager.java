package Base;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lhycode.news.R;
import com.lhycode.news.bean.HealthChannels;
import com.lhycode.news.bean.HealthChannels.HealthChannel;
import com.lhycode.news.utils.CacheUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import Base.FunnyPager.FunnyPagerAdapter;
import BaseDetailPager.HealthDetailPager;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HealthPager extends BasePager {

	private ViewPager viewPager;
	private TabPageIndicator pageIndicator;
	private HealthChannels healthChannels;
	private ArrayList<HealthChannel> healthChannelList;
	private ArrayList<HealthDetailPager> healthDetailPagers;
	private View view;
	private ImageButton imageButton;

	public HealthPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		setSlidingMenuEnable(false);
		buttonMenu.setVisibility(View.INVISIBLE);
		title.setText("健康问答");

		view = View.inflate(activity, R.layout.health_pager, null);

		viewPager = (ViewPager) view.findViewById(R.id.health_viewpager);
		ViewUtils.inject(this, view);
		imageButton = (ImageButton) view.findViewById(R.id.health_next);
		pageIndicator = (TabPageIndicator) view
				.findViewById(R.id.health_indicator);

		String cache = CacheUtil.getCache(activity, "healthChannel");
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache);
		}
		getDataFromServer();

	}

	@OnClick(R.id.health_next)
	public void healthnext(View view) {
		int currentItem = viewPager.getCurrentItem();
		if(currentItem<healthChannelList.size()){
			viewPager.setCurrentItem(++currentItem);
		}else {
			currentItem=0;
		}
		
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		utils.send(HttpMethod.GET, "http://apis.baidu.com/tngou/ask/classify"
				+ "?" + "", requestParams, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo responseInfo) {
				String result = (String) responseInfo.result;
				System.out.println(result);
				parseData(result);
				CacheUtil.setCache(activity, "healthChannel", result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(activity, msg, 0).show();
				error.printStackTrace();
			}
		});
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		healthChannels = gson.fromJson(result, HealthChannels.class);
		healthChannelList = healthChannels.tngou;

		healthDetailPagers = new ArrayList<HealthDetailPager>();
		for (int i = 0; i < healthChannelList.size(); i++) {
			HealthDetailPager healthDetailPager = new HealthDetailPager(
					activity, healthChannelList.get(i).id);
			healthDetailPagers.add(healthDetailPager);
		}

		viewPager.setAdapter(new HealthPagerAdapter());
		pageIndicator.setViewPager(viewPager);
		content.removeAllViews();
		content.addView(view);
	}

	class HealthPagerAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {
			return healthChannelList.get(position).title;
		}

		@Override
		public int getCount() {
			return healthChannelList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			HealthDetailPager pager = healthDetailPagers.get(position);
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
