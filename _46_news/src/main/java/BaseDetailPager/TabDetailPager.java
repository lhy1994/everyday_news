package BaseDetailPager;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lhycode.news.NewsDetailActivity;
import com.lhycode.news.PreferenceUtils;
import com.lhycode.news.R;
import com.lhycode.news.bean.NewsData.NewsTabData;
import com.lhycode.news.bean.NewsDetailData;
import com.lhycode.news.bean.NewsDetailData.NewsItem;
import com.lhycode.news.bean.NewsDetailData.NewsItems;
import com.lhycode.news.bean.NewsDetailData.NewsPages;
import com.lhycode.news.bean.TabData;
import com.lhycode.news.bean.TabData.TabNewsData;
import com.lhycode.news.bean.TabData.TopNewsData;
import com.lhycode.news.global.GlobalContent;
import com.lhycode.news.utils.ApiUtils;
import com.lhycode.news.utils.CacheUtil;
import com.lhycode.news.view.RefreshListView;
import com.lhycode.news.view.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TabDetailPager extends BaseDetailMenuPager implements
		OnPageChangeListener {

	public NewsTabData newsTabData;
	private String url;
	private TabData tabDetailData;
	private ViewPager viewPager;
	private TextView textView;
	private ArrayList<TopNewsData> topNewsList;
	private CirclePageIndicator circlePageIndicator;
	private RefreshListView listView;
	private ArrayList<TabNewsData> newsList;
	private String moreUrl;
	private NewsListAdapter newsListAdapter;
	private Handler handler;
	private Handler handler2;
	private String channelId;
	private NewsDetailData newsDetailData;
	private NewsItems newsItems;
	private NewsPages newsPages;
	private ArrayList<NewsItem> newsItemList;
	private ArrayList<NewsItem> topNewsItemList;
	private int allPages;
	private int currentPage;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		this.newsTabData = newsTabData;
		url = GlobalContent.SERVER_URL + newsTabData.url;
	}

	public TabDetailPager(Activity activity, String channelId) {
		super(activity);
		this.channelId = channelId;
	}

	@Override
	public View initViews() {
		View view = View.inflate(activity, R.layout.tab_detail_pager, null);
		View headerView = View.inflate(activity, R.layout.header_list, null);
		viewPager = (ViewPager) headerView.findViewById(R.id.vp_news);
		textView = (TextView) headerView.findViewById(R.id.tv_title);
		// viewPager.setOnPageChangeListener(this);
		circlePageIndicator = (CirclePageIndicator) headerView
				.findViewById(R.id.indicator);
		listView = (RefreshListView) view.findViewById(R.id.lv_list);
		listView.addHeaderView(headerView);
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (currentPage<=allPages) {
					 getMoreDataFromServer();
				} else {
					Toast.makeText(activity, "没有更多了", Toast.LENGTH_SHORT).show();
					listView.onRefreshComplete(false);
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("ѡ�У�" + position);
				String ids = PreferenceUtils
						.getString(activity, "read_ids", "");

				// String readID = newsList.get(position).id;
				String readID = newsItemList.get(position).title;
				if (!ids.contains(readID)) {
					ids = ids + readID + ",";
					PreferenceUtils.setString(activity, "read_ids", ids);
				}
				// newsListAdapter.notifyDataSetChanged();
				// �ֲ�ˢ��
				changeReadState(view);
				// ҳ����ת
				Intent intent = new Intent();
				intent.setClass(activity, NewsDetailActivity.class);
				// intent.putExtra("url", newsList.get(position).url);
				intent.putExtra("url", newsItemList.get(position).link);
				activity.startActivity(intent);
			}
		});
		return view;
	}

	private void changeReadState(View view) {
		TextView titleTextView = (TextView) view
				.findViewById(R.id.tv_list_item_title);
		titleTextView.setTextColor(Color.GRAY);
	}

	@Override
	public void initData() {
		String cache = CacheUtil.getCache(activity, "newsContent");
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache, false);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {

		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/showapi_open_bus/channel_news/search_news"
						+ "?" + "channelId=" + channelId, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
//						System.out.println("ҳǩ����ҳ2��������" + result);
						parseData(result,false);
						listView.onRefreshComplete(true);
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(activity, msg, 0).show();
						error.printStackTrace();
						listView.onRefreshComplete(false);
					}
				});
	}

	private void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/showapi_open_bus/channel_news/search_news"
						+ "?" + "channelId=" + channelId+"&page="+currentPage, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						parseData(result,true);
						listView.onRefreshComplete(true);
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(activity, msg, 0).show();
						error.printStackTrace();
						listView.onRefreshComplete(false);
					}
				});
	}

	protected void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		newsDetailData = gson.fromJson(result, NewsDetailData.class);
		newsItems = newsDetailData.showapi_res_body;
		newsPages = newsItems.pagebean;
		allPages = Integer.valueOf(newsPages.allPages);
		currentPage = Integer.valueOf(newsPages.currentPage)+1;
		// String more = tabDetailData.data.more;
		// if (!TextUtils.isEmpty(more)) {
		// moreUrl = GlobalContent.SERVER_URL + more;
		// } else {
		// moreUrl = null;
		// }
		if (!isMore) {
			// topNewsList = tabDetailData.data.topnews;
			// if (topNewsList != null) {
			// newsList = tabDetailData.data.news;
			// viewPager.setAdapter(new TopNewsAdapter());
			// circlePageIndicator.setViewPager(viewPager);
			// circlePageIndicator.setSnap(true);
			// circlePageIndicator.setOnPageChangeListener(this);
			// circlePageIndicator.onPageSelected(0);// ָʾ����������λ��
			// textView.setText(topNewsList.get(0).title);
			// }
			// if (newsList != null) {
			// newsListAdapter = new NewsListAdapter();
			// listView.setAdapter(newsListAdapter);
			// }
			newsItemList = newsPages.contentlist;
			if (newsItemList != null) {
				topNewsItemList = new ArrayList<NewsDetailData.NewsItem>();
				for (int i = 0; i < 5; i++) {
					NewsItem newsItem = newsItemList.get(i);
					topNewsItemList.add(newsItem);
				}
				viewPager.setAdapter(new TopNewsAdapter());
				circlePageIndicator.setViewPager(viewPager);
				circlePageIndicator.setSnap(true);
				circlePageIndicator.setOnPageChangeListener(this);
				circlePageIndicator.onPageSelected(0);// ָʾ����������λ��
				textView.setText(topNewsItemList.get(0).title);

				newsListAdapter = new NewsListAdapter();
				listView.setAdapter(newsListAdapter);
			}

			if (handler == null) {
				handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						int currentItem = viewPager.getCurrentItem();
						if (currentItem < topNewsItemList.size() - 1) {
							currentItem++;
						} else {
							currentItem = 0;
						}
						viewPager.setCurrentItem(currentItem);
						handler.sendMessageDelayed(Message.obtain(), 2000);
					}
				};
				handler.sendMessageDelayed(Message.obtain(), 2000);
			}
		} else {
//			 ArrayList<TabNewsData> news = tabDetailData.data.news;
//			 newsList.addAll(news);
			 ArrayList<NewsItem> moreNews=newsPages.contentlist;
			 newsItemList.addAll(moreNews);
			 newsListAdapter.notifyDataSetChanged();
		}
	}

	class TopNewsAdapter extends PagerAdapter {
		private BitmapUtils bitmapUtils;

		public TopNewsAdapter() {
			bitmapUtils = new BitmapUtils(activity);
		}

		@Override
		public int getCount() {
			// return tabDetailData.data.topnews.size();
			return topNewsItemList.size();

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(activity);
			imageView.setImageResource(R.drawable.news_logo);
			imageView.setScaleType(ScaleType.FIT_XY);
			// TopNewsData topNewsData =
			// tabDetailData.data.topnews.get(position);
			// bitmapUtils.display(imageView, topNewsData.topimage);
			if (topNewsItemList.get(position).imageurls.size() > 0) {
				bitmapUtils.display(imageView,
						topNewsItemList.get(position).imageurls.get(0).url);
			}
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		// TopNewsData topNewsData = tabDetailData.data.topnews.get(position);
		// textView.setText(topNewsData.title);
		textView.setText(topNewsItemList.get(position).title);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	class NewsListAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public NewsListAdapter() {
			bitmapUtils = new BitmapUtils(activity);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.news_logo);
		}

		@Override
		public int getCount() {
			return newsItemList.size();
		}

		@Override
		public NewsItem getItem(int position) {
			return newsItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(activity, R.layout.news_list_item,
						null);
				holder = new ViewHolder();
				holder.dateTextView = (TextView) convertView
						.findViewById(R.id.tv_date);
				holder.titleTextView = (TextView) convertView
						.findViewById(R.id.tv_list_item_title);
				holder.picImageView = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				holder.src = (TextView) convertView.findViewById(R.id.tv_src);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// TabNewsData tabNewsData = (TabNewsData) getItem(position);
			// holder.dateTextView.setText(tabNewsData.pubdate);
			// holder.titleTextView.setText(tabNewsData.title);
			// bitmapUtils.display(holder.picImageView, tabNewsData.listimage);
			NewsItem item = getItem(position);
			holder.dateTextView.setText(item.pubDate);
			holder.titleTextView.setText(item.title);
			holder.src.setText(item.source);
			if (item.imageurls.size() > 0) {
				bitmapUtils.display(holder.picImageView,
						item.imageurls.get(0).url);
			} else {
				holder.picImageView.setImageResource(R.drawable.news_logo);
			}

			String ids = PreferenceUtils.getString(activity, "read_ids", "");
			if (ids.contains(item.title)) {
				holder.titleTextView.setTextColor(Color.GRAY);
			} else {
				holder.titleTextView.setTextColor(Color.BLACK);
			}
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView titleTextView;
		public TextView dateTextView;
		public ImageView picImageView;
		public TextView src;
	}
}
