package Base;

import java.util.ArrayList;
import java.util.Iterator;

import com.aphidmobile.flip.FlipViewController;
import com.google.gson.Gson;
import com.lhycode.news.NewsDetailActivity;
import com.lhycode.news.R;
import com.lhycode.news.TravelDetailActivity;
import com.lhycode.news.bean.TicketInfo;
import com.lhycode.news.bean.TicketInfo.Ticket;
import com.lhycode.news.bean.TravelItems;
import com.lhycode.news.bean.TravelItems.TicketItem;
import com.lhycode.news.bean.TravelItems.TravelItem;
import com.lhycode.news.utils.ApiUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TravelPager extends BasePager {

	private FlipViewController flipView;
	public TravelItems travelItems;
	public TravelItem travelItem;
	public ArrayList<TicketItem> ticketList;
	public TicketInfo ticketInfo;
	public ArrayList<TicketInfo.Ticket> ticketInfoList;
	private TravelAdapter adapter;

	public TravelPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		setSlidingMenuEnable(false);
		buttonMenu.setVisibility(View.GONE);
		title.setText("旅游");
		flipView = new FlipViewController(activity,
				FlipViewController.HORIZONTAL);
		getDataFromServer();

		content.removeAllViews();
		content.addView(flipView);
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/apistore/qunaerticket/querylist" + "?"
						+ "", requestParams, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						System.out.println("ticketItem..........." + result);
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
		travelItems = gson.fromJson(result, TravelItems.class);
		travelItem = travelItems.retData;
		ticketList = travelItem.ticketList;
		System.out.println(ticketList.size());
		ticketInfoList = new ArrayList<TicketInfo.Ticket>();
		for (int i = 0; i < ticketList.size(); i++) {
			getTicketInfo(ticketList.get(i).productId);
		}
	}

	public void getTicketInfo(String productId) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
		httpUtils.send(HttpMethod.GET,
				"http://apis.baidu.com/apistore/qunaerticket/querydetail" + "?"
						+ "id="+productId, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						System.out.println("ticket info............" + result);
						parseTicketInfo(result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(activity, msg, 0).show();
						error.printStackTrace();
					}
				});
	}

	protected void parseTicketInfo(String result) {
		Gson gson = new Gson();
		ticketInfo = gson.fromJson(result, TicketInfo.class);
		TicketInfo.Ticket ticket = ticketInfo.retData.ticketDetail.data.display.ticket;
		System.out.println("ticket is ..............." + ticket);
		ticketInfoList.add(ticket);
		System.out.println(ticketInfoList.size()
				+ "////////////////////////////////");
		if(ticketInfoList.size()==ticketList.size()){
			adapter = new TravelAdapter(activity);
			flipView.setAdapter(adapter);
		}
	}

	public void getInfo() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				String resultString = ApiUtils
						.request(
								"http://apis.baidu.com/apistore/qunaerticket/querydetail",
								"id=1361653183");
				Gson gson=new Gson();
				TicketInfo ticketInfo = gson.fromJson(resultString, TicketInfo.class);
				TicketInfo.Ticket ticket = ticketInfo.retData.ticketDetail.data.display.ticket;
				Message message=new Message();
				message.obj=ticket;
			}
		};
		Handler handler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				ticketInfoList.add((Ticket)msg.obj);
			};
		};
		thread.start();
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
			return ticketList.size() * repeatCount;
		}

		public int getRepeatCount() {
			return repeatCount;
		}

		public void setRepeatCount(int repeatCount) {
			this.repeatCount = repeatCount;
		}

		@Override
		public Ticket getItem(int position) {
			return ticketInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(activity, R.layout.travel_list_item,
						null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.travel_image);
				holder.spotName = (TextView) convertView
						.findViewById(R.id.travel_spotname);
				holder.description = (TextView) convertView
						.findViewById(R.id.travel_description);
				holder.book = (Button) convertView
						.findViewById(R.id.travel_book);
				holder.address = (TextView) convertView
						.findViewById(R.id.travel_address);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final TicketInfo.Ticket item = ticketInfoList.get(position);
			holder.spotName.setText(item.spotName);
			holder.description.setText(item.description);
			holder.address.setText(item.address);
			utils.display(holder.imageView, item.imageUrl);
			holder.book.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(activity, TravelDetailActivity.class);
					intent.putExtra("url", item.detailUrl);
					activity.startActivity(intent);
				}
			});
			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView imageView;
		public TextView spotName;
		public TextView description;
		public Button book;
		public TextView address;
	}

}
