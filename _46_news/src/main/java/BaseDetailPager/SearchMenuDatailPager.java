package BaseDetailPager;

import java.util.ArrayList;


import com.google.gson.Gson;
import com.lhycode.news.R;
import com.lhycode.news.bean.NewsDetailData;
import com.lhycode.news.bean.NewsDetailData.NewsItem;
import com.lhycode.news.bean.NewsDetailData.NewsItems;
import com.lhycode.news.bean.NewsDetailData.NewsPages;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import BaseDetailPager.TabDetailPager.ViewHolder;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class SearchMenuDatailPager extends BaseDetailMenuPager {

	private NewsDetailData newsDetailData;
	private NewsItems newsItems;
	private NewsPages newsPages;
	private ArrayList<NewsItem> newsItemList;
	private ListView listView;
	private SearchView searchView;
	private SearchAdapter searchAdapter;

	public SearchMenuDatailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		View view = View.inflate(activity, R.layout.search_menu_pager, null);
		searchView = (SearchView) view.findViewById(R.id.searchview);
		listView = (ListView) view.findViewById(R.id.search_listview);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				getDataFromServer(query);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}

		});

		searchView.setSubmitButtonEnabled(true);
		return view;
	}

	@Override
	public void initData() {
		super.initData();
	}

	protected void getDataFromServer(String query) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/showapi_open_bus/channel_news/search_news"
						+ "?" + "title=" + query, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						parseData(result);
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
		newsDetailData = gson.fromJson(result, NewsDetailData.class);
		newsItems = newsDetailData.showapi_res_body;
		newsPages = newsItems.pagebean;
		newsItemList = newsPages.contentlist;
		if(newsItemList==null)
		{
			Toast.makeText(activity, "没找到",Toast.LENGTH_SHORT).show();
		}else {
			searchAdapter = new SearchAdapter();
			listView.setAdapter(searchAdapter);
			searchAdapter.notifyDataSetChanged();
		}
	}

	class SearchAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public SearchAdapter() {
			bitmapUtils = new BitmapUtils(activity);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
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
