package BaseDetailPager;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lhycode.news.R;
import com.lhycode.news.bean.FunnyTextItems;
import com.lhycode.news.bean.FunnyTextItems.FunnyTextItem;
import com.lhycode.news.bean.FunnyTextItems.FunnyTextPages;
import com.lhycode.news.view.RefreshListView;
import com.lhycode.news.view.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TextFunnyDetailPager extends BaseDetailMenuPager {

	private RefreshListView listView;
	private FunnyTextItems funnyTextItems;
	private FunnyTextPages funnyTextPages;
	private ArrayList<FunnyTextItem> funnyTextItemList;
	private int allPages;
	private int currentPage;
	private FunnyTextAdapter funnyTextAdapter;

	public TextFunnyDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		View view = View.inflate(activity, R.layout.funny_text_pager, null);
		// View headerView = View.inflate(activity, R.layout.header_list, null);

		listView = (RefreshListView) view.findViewById(R.id.funny_text_list);
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
					Toast.makeText(activity, "没有更多了", 0).show();
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
				"http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text"
						+ "?" + "page=1", requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						System.out.println("funnyText........." + result);
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
				"http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text"
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
		funnyTextItems = gson.fromJson(result, FunnyTextItems.class);
		funnyTextPages = funnyTextItems.showapi_res_body;

		if (funnyTextPages.allPages != null) {
			allPages = Integer.valueOf(funnyTextPages.allPages);
			currentPage = Integer.valueOf(funnyTextPages.currentPage) + 1;
			if (!isMore) {
				funnyTextItemList = funnyTextPages.contentlist;
				System.out.println("�������ݡ���������������" + funnyTextItemList);
				funnyTextAdapter = new FunnyTextAdapter();
				listView.setAdapter(funnyTextAdapter);
			} else {
				ArrayList<FunnyTextItem> list = funnyTextPages.contentlist;
				funnyTextItemList.addAll(list);
				funnyTextAdapter.notifyDataSetChanged();
			}
		}
	}

	class FunnyTextAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return funnyTextItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return funnyTextItemList.get(position);
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
						R.layout.funny_text_list_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.funny_text_title);
				holder.content = (TextView) convertView
						.findViewById(R.id.funny_text_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			FunnyTextItem item = funnyTextItemList.get(position);
			holder.title.setText(item.title);
			holder.content.setText(item.text);
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView title;
		public TextView content;
	}

}
