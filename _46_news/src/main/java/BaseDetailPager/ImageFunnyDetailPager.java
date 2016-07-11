package BaseDetailPager;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lhycode.news.R;
import com.lhycode.news.bean.FunnyImageItems;
import com.lhycode.news.bean.FunnyImageItems.FunnyImageItem;
import com.lhycode.news.bean.FunnyImageItems.FunnyImagePages;
import com.lhycode.news.bean.FunnyTextItems;
import com.lhycode.news.bean.FunnyTextItems.FunnyTextItem;
import com.lhycode.news.utils.MyBitmapUtils;
import com.lhycode.news.view.RefreshListView;
import com.lhycode.news.view.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import BaseDetailPager.TextFunnyDetailPager.FunnyTextAdapter;
import BaseDetailPager.TextFunnyDetailPager.ViewHolder;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageFunnyDetailPager extends BaseDetailMenuPager {

	private RefreshListView listView;
	private FunnyImageItems funnyImageItems;
	private FunnyImagePages funnyImagePages;
	private int allPages;
	private int currentPage;
	private ArrayList<FunnyImageItem> funnyImageItemList;
	private FunnyImageAdapter adapter;

	public ImageFunnyDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		View view = View.inflate(activity, R.layout.funny_image_pager, null);
		// View headerView = View.inflate(activity, R.layout.header_list, null);

		listView = (RefreshListView) view.findViewById(R.id.funny_image_list);
		// listView.addHeaderView(headerView);
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (currentPage < allPages) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(activity, "û�и�����", Toast.LENGTH_SHORT).show();
					listView.onRefreshComplete(false);
				}
			}
		});
		return view;
	}

	@Override
	public void initData() {
		getDataFromServer();
	}

	protected void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_pic"
						+ "?" + "page=1", requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						System.out.println("funnyImage........." + result);
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
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_pic"
						+ "?" + "&page=" + currentPage, requestParams,
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
		funnyImageItems = gson.fromJson(result, FunnyImageItems.class);
		funnyImagePages = funnyImageItems.showapi_res_body;

		if (funnyImagePages != null) {

			allPages = Integer.valueOf(funnyImagePages.allPages);
			currentPage = Integer.valueOf(funnyImagePages.currentPage) + 1;
			if (!isMore) {
				funnyImageItemList = funnyImagePages.contentlist;
				adapter = new FunnyImageAdapter();
				listView.setAdapter(adapter);
			} else {
				ArrayList<FunnyImageItem> list = funnyImagePages.contentlist;
				funnyImageItemList.addAll(list);
				adapter.notifyDataSetChanged();
			}
		}
	}

	class FunnyImageAdapter extends BaseAdapter {

//		private BitmapUtils bitmapUtils;

		private MyBitmapUtils utils;
		public FunnyImageAdapter(){
			utils=new MyBitmapUtils();
		}
	/*public FunnyImageAdapter() {
			bitmapUtils = new BitmapUtils(activity);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}
*/
		@Override
		public int getCount() {
			return funnyImageItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return funnyImageItemList.get(position);
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
						R.layout.funny_image_list_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.funny_image_title);
				holder.content = (ImageView) convertView
						.findViewById(R.id.funny_image_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			FunnyImageItem item = funnyImageItemList.get(position);
			holder.title.setText(item.title);
			utils.display(holder.content, item.img);
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView title;
		public ImageView content;
	}

}
