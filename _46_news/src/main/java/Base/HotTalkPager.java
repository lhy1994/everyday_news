package Base;

import java.util.ArrayList;
import java.util.List;

import com.aphidmobile.flip.FlipViewController;
import com.google.gson.Gson;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.huxq17.swipecardsview.SwipeCardsView;
import com.lhycode.news.NewsDetailActivity;
import com.lhycode.news.R;
import com.lhycode.news.bean.HotTalkItems;
import com.lhycode.news.bean.HotTalkItems.HotTalkItem;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HotTalkPager extends BasePager {

    private FlipViewController flipView;
    private HotTalkItems hotTalkItems;
    private ArrayList<HotTalkItem> hotTalkItemList;
    private ListView listView;
    private View view;
    private SwipeCardsView swipeCardsView;
    private Button look;
    private MyCardAdapter cardAdapter;

    public HotTalkPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void initData() {
        title.setText("当前热词");
        setSlidingMenuEnable(false);
        buttonMenu.setVisibility(View.GONE);
//		flipView = new FlipViewController(activity, FlipViewController.HORIZONTAL);
//		listView = new ListView(activity);
//		listView.setDividerHeight(0);
        view = View.inflate(activity, R.layout.hottalk_pager, null);
        swipeCardsView = (SwipeCardsView) view.findViewById(R.id.swipCardsView);
        look = (Button) view.findViewById(R.id.btn_look);
        getDataFromServer();
        content.removeAllViews();
        content.addView(view);
//		content.addView(flipView);
//		content.addView(listView);
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
        httpUtils.send(HttpMethod.GET,
                "http://apis.baidu.com/tngou/top/news" + "?" + "id=0&rows=20",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        parseData(result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                });
    }

    protected void parseData(String result) {
        Gson gson = new Gson();
        hotTalkItems = gson.fromJson(result, HotTalkItems.class);
        hotTalkItemList = hotTalkItems.list;
        if (hotTalkItemList == null) {
            Toast.makeText(activity, "������", Toast.LENGTH_SHORT).show();
        } else {
//			flipView.setAdapter(new TravelAdapter(activity));
//			listView.setAdapter(new TravelAdapter(activity));
            cardAdapter = new MyCardAdapter(hotTalkItemList,activity);
            swipeCardsView.setAdapter(cardAdapter);
            look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeCardsView.setAdapter(cardAdapter);
                    swipeCardsView.notifyDatasetChanged(0);
                }
            });
        }


    }

    public class MyCardAdapter extends BaseCardAdapter {
        private List<HotTalkItem> datas;
        private Context context;
        private BitmapUtils utils;

        public MyCardAdapter(List<HotTalkItem> datas, Context context) {
            this.datas = datas;
            this.context = context;
            utils = new BitmapUtils(activity);
            utils.configDefaultLoadingImage(R.drawable.news_logo);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public int getCardLayoutId() {
            return R.layout.hottalk_list_item;
        }

        @Override
        public void onBindData(final int position, View cardview) {
            if (datas == null || datas.size() == 0) {
                return;
            }

            final TextView title = (TextView) cardview.findViewById(R.id.hot_title);
            ImageView pic = (ImageView) cardview.findViewById(R.id.hot_image);
            TextView desc = (TextView) cardview.findViewById(R.id.hot_description);
            Button button = (Button) cardview.findViewById(R.id.hot_button);

            final HotTalkItem item = datas.get(position);
            title.setText(item.keywords);
            utils.display(pic, "http://tnfs.tngou.net/img" + item.img);
            desc.setText(item.description);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(activity, NewsDetailActivity.class);
                    intent.putExtra("url", item.fromurl);
                    activity.startActivity(intent);
                }
            });
        }

        /**
         * 如果可见的卡片数是3，则可以不用实现这个方法
         *
         * @return
         */
        @Override
        public int getVisibleCardCount() {
            return datas.size();
        }
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
            return hotTalkItemList.size() * repeatCount;
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
                        R.layout.hottalk_list_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.hot_image);
                holder.title = (TextView) convertView
                        .findViewById(R.id.hot_title);
                holder.description = (TextView) convertView
                        .findViewById(R.id.hot_description);
                holder.getInfo = (Button) convertView
                        .findViewById(R.id.hot_button);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final HotTalkItem item = hotTalkItemList.get(position);
            holder.title.setText(item.keywords);
            holder.description.setText(item.description);
            utils.display(holder.imageView, "http://tnfs.tngou.net/img" + item.img);
            holder.getInfo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(activity, NewsDetailActivity.class);
                    intent.putExtra("url", item.fromurl);
                    activity.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView title;
        public TextView description;
        public Button getInfo;
    }

}
