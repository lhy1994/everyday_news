package BaseDetailPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.utils.AphidLog;
import com.aphidmobile.utils.IO;
import com.aphidmobile.utils.UI;
import com.google.gson.Gson;
import com.lhycode.news.R;
import com.lhycode.news.bean.HistoryItems;
import com.lhycode.news.bean.HistoryItems.HistoryItem;
import com.lhycode.news.bean.PhotoNewsItems.PhotoNewsItem;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import BaseDetailPager.PhotoMenuDatailPager.ViewHolder;
import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryMenuDatailPager extends BaseDetailMenuPager {
	private FlipViewController flipView;
	private HistoryItems historyItems;
	private ArrayList<HistoryItem> historyItemList;

	public HistoryMenuDatailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {

		flipView = new FlipViewController(activity);
		flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
		flipView.setDrawingCacheBackgroundColor(Color.WHITE);

		return flipView;
	}

	@Override
	public void initData() {
		super.initData();
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		Calendar instance = Calendar.getInstance();
		int day = instance.get(Calendar.DAY_OF_MONTH);
		int month = instance.get(Calendar.MONTH)+1;
		System.out.println("day:"+day+"month:"+month);
		RequestParams requestParams = new RequestParams();
		httpUtils
				.send(HttpMethod.GET,
						"http://apis.haoservice.com/lifeservice/toh?key=6c0cbd1e3812433a88ca11d5d2105ca3"
								+ "&month=" + month + "&day=" + day,
						new RequestCallBack<String>() {

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								String result = responseInfo.result;
								parseData(result);
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
								error.printStackTrace();
							}
						});
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		historyItems = gson.fromJson(result, HistoryItems.class);
		historyItemList = historyItems.result;
		flipView.setAdapter(new TravelAdapter(activity));
	}

	class TravelAdapter extends BaseAdapter {

		private int repeatCount = 1;
		Activity activity;
		private BitmapUtils utils;

		TravelAdapter(Activity context) {
			this.activity = context;
			utils = new BitmapUtils(activity);
			utils.configDefaultLoadingImage(R.drawable.news_logo);
		}

		@Override
		public int getCount() {
			return historyItemList.size() * repeatCount;
		}

		public int getRepeatCount() {
			return repeatCount;
		}

		public void setRepeatCount(int repeatCount) {
			this.repeatCount = repeatCount;
		}

		@Override
		public Object getItem(int position) {
			return position;
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
						R.layout.history_list_item, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.history_image);
				holder.title = (TextView) convertView
						.findViewById(R.id.history_title);
				holder.date = (TextView) convertView
						.findViewById(R.id.history_date);
				holder.lunar = (TextView) convertView
						.findViewById(R.id.history_lunar);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HistoryItem item = historyItemList.get(position);
			holder.title.setText(item.title);
			holder.date.setText(item.year);
			holder.lunar.setText(item.lunar);
			utils.display(holder.imageView, item.pic);
			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView imageView;
		public TextView title;
		public TextView lunar;
		public TextView date;
	}
}
