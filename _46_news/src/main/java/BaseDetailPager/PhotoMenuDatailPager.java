package BaseDetailPager;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lhycode.news.MainActivity;
import com.lhycode.news.NewsDetailActivity;
import com.lhycode.news.R;
import com.lhycode.news.bean.PhotoNewsItems;
import com.lhycode.news.bean.PhotoNewsItems.PhotoNewsItem;
import com.lhycode.news.bean.PhotosData;
import com.lhycode.news.bean.PhotosData.PhotoInfo;
import com.lhycode.news.fragment.LeftMenuFragment;
import com.lhycode.news.global.GlobalContent;
import com.lhycode.news.utils.ApiUtils;
import com.lhycode.news.utils.CacheUtil;
import com.lhycode.news.utils.MyBitmapUtils;
import com.lhycode.news.view.RefreshGridView;
import com.lhycode.news.view.RefreshGridView.RefreshListener;
import com.lhycode.news.view.RefreshListView;
import com.lhycode.news.view.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoMenuDatailPager extends BaseDetailMenuPager {

	private RefreshListView listView;
	private GridView gridView;
	private ArrayList<PhotoInfo> photoInfoList;
	private ImageButton gridButton;

	public PhotoMenuDatailPager(Activity activity, ImageButton gridButton) {
		super(activity);
		this.gridButton = gridButton;
		this.gridButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeDisplay();
			}
		});
	}

	@Override
	public View initViews() {
		View view = View.inflate(activity, R.layout.photo_menu_pager, null);
		listView = (RefreshListView) view.findViewById(R.id.listview_photo);
		gridView = (GridView) view.findViewById(R.id.gridview);
		OnItemClickListener itemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(activity, NewsDetailActivity.class);
				intent.putExtra("url", photoNewsItemList.get(position).url);
				activity.startActivity(intent);
			}
		};
		listView.setOnItemClickListener(itemClickListener);
		gridView.setOnItemClickListener(itemClickListener);
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

		return view;
	}

	@Override
	public void initData() {
		super.initData();
		MainActivity mainActivity = (MainActivity) activity;
		mainActivity.getSlidingMenu().setTouchModeAbove(
				SlidingMenu.TOUCHMODE_FULLSCREEN);
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/songshuxiansheng/news/news" + "?" + "",
				requestParams, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						parseData(result, false);
						listView.onRefreshComplete(true);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(activity, msg, 0).show();
						listView.onRefreshComplete(false);
						error.printStackTrace();
					}
				});
	}
	private void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/songshuxiansheng/news/news" + "?" + "",
				requestParams, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						// System.out.println("ͼ�� ��������������" + result);
						parseData(result, true);
						listView.onRefreshComplete(true);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(activity, msg, 0).show();
						listView.onRefreshComplete(false);
						error.printStackTrace();
					}
				});
	}

	private void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		// PhotosData photosData = gson.fromJson(result, PhotosData.class);
		// photoInfoList = photosData.data.news;
		photoNewsItems = gson.fromJson(result, PhotoNewsItems.class);
		if (photoNewsItems != null) {

			if (!isMore) {
				photoNewsItemList = photoNewsItems.retData;
				listAdapter = new PhotoListAdapter();
				listView.setAdapter(listAdapter);
				gridView.setAdapter(listAdapter);
			} else {
				ArrayList<PhotoNewsItem> retData = photoNewsItems.retData;
				photoNewsItemList.addAll(retData);
				listAdapter.notifyDataSetChanged();
			}
		}

	}

	class PhotoListAdapter extends BaseAdapter {

		private MyBitmapUtils utils;
//		BitmapUtils utils;
		public PhotoListAdapter(){
//			utils=new BitmapUtils(activity);
			utils=new MyBitmapUtils();
		}
		

		@Override
		public int getCount() {
			// return photoInfoList.size();
			return photoNewsItemList.size();
		}

		@Override
		public PhotoNewsItem getItem(int position) {
			// return photoInfoList.get(position);
			return photoNewsItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(activity, R.layout.photo_list_item,
						null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.photo_image);
				holder.textView = (TextView) convertView
						.findViewById(R.id.photo_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// PhotoInfo photoInfo = getItem(position);
			// holder.textView.setText(photoInfo.title);
			// utils.display(holder.imageView, photoInfo.listimage);
			PhotoNewsItem item = getItem(position);
			holder.textView.setText(item.title);
			utils.display(holder.imageView, item.image_url);
			return convertView;
		}

	}

	static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}

	private boolean isListDisplay = true;
	private PhotoNewsItems photoNewsItems;
	private ArrayList<PhotoNewsItem> photoNewsItemList;
	private Handler handler;
	private PhotoListAdapter listAdapter;

	private void changeDisplay() {
		if (isListDisplay) {
			isListDisplay = false;
			gridView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.INVISIBLE);
		} else {
			isListDisplay = true;
			gridView.setVisibility(View.INVISIBLE);
			listView.setVisibility(View.VISIBLE);
		}
	}

}
