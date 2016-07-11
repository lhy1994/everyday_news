package BaseDetailPager;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lhycode.news.NewsDetailActivity;
import com.lhycode.news.PreferenceUtils;
import com.lhycode.news.R;
import com.lhycode.news.bean.HealthDetail;
import com.lhycode.news.bean.HealthItems;
import com.lhycode.news.bean.HealthItems.HealthItem;
import com.lhycode.news.bean.NewsDetailData.NewsItem;
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

import BaseDetailPager.TabDetailPager.ViewHolder;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HealthDetailPager extends BaseDetailMenuPager {

	private String id;
	private RefreshListView listView;
	private HealthItems healthItems;
	private ArrayList<HealthItem> healthItemsList;
	private int nextPage;
	private HealthDetailAdapter adapter;
	public ArrayList<HealthDetail> healthDetailsList=new ArrayList<HealthDetail>();


	public HealthDetailPager(Activity activity, String id) {
		super(activity);
		this.id = id;
	}

	@Override
	public View initViews() {
		View view = View.inflate(activity, R.layout.health_detail_pager, null);
		listView = (RefreshListView) view.findViewById(R.id.health_list);
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				getMoreDataFromServer();
			}

		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(activity, NewsDetailActivity.class);
				if (healthDetailsList.size()>0){
					intent.putExtra("url", healthDetailsList.get(position).url);
					activity.startActivity(intent);
				}
			}
		});
		return view;
	}

	public void getHealthDetail(int position) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET, "http://apis.baidu.com/tngou/ask/show"
				+ "?" + "id="+position, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						System.out.println("asdasdasd"+result);
						parseDetail(result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(activity, msg, 0).show();
						error.printStackTrace();
					}
				});
	}
	public void parseDetail(String result) {
		Gson gson=new Gson();
		HealthDetail healthDetail = gson.fromJson(result, HealthDetail.class);
		if(healthDetail==null)
		{
			System.out.println("������");
		}
		healthDetailsList.add(healthDetail);
	}
	@Override
	public void initData() {
		super.initData();
		getDataFromServer();
	}

	protected void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET, "http://apis.baidu.com/tngou/ask/list"
				+ "?" + "id=" + id, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						parseData(result, false);
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

	protected void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET, "http://apis.baidu.com/tngou/ask/list"
				+ "?" + "id=" + id + "&page=" + nextPage, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						parseData(result, true);
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
		healthItems = gson.fromJson(result, HealthItems.class);
		

		if (!isMore) {
			nextPage = 2;
			healthItemsList = healthItems.tngou;
			adapter = new HealthDetailAdapter();
			listView.setAdapter(adapter);
		} else {
			nextPage = nextPage + 1;
			ArrayList<HealthItem> list=healthItems.tngou;
			healthItemsList.addAll(list);
			adapter.notifyDataSetChanged();
		}
		for(int i=0;i<healthItemsList.size();i++)
		{
			getHealthDetail(i);
		}
	}

	class HealthDetailAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public HealthDetailAdapter() {
			bitmapUtils = new BitmapUtils(activity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_logo);
		}

		@Override
		public int getCount() {
			return healthItemsList.size();
		}

		@Override
		public HealthItem getItem(int position) {
			return healthItemsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(activity,
						R.layout.health_detail_list_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.health_title);
				holder.description = (TextView) convertView
						.findViewById(R.id.health_description);
				holder.image = (ImageView) convertView
						.findViewById(R.id.health_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HealthItem item = getItem(position);
			holder.title.setText(item.title);
			holder.description.setText(item.description);
			bitmapUtils.display(holder.image, "http://tnfs.tngou.net/img"
					+ item.img);
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView title;
		public TextView description;
		public ImageView image;
	}

}
