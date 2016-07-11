package Base;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lhycode.news.MainActivity;
import com.lhycode.news.bean.NewsChannel;
import com.lhycode.news.bean.NewsChannel.NewsChannelItem;
import com.lhycode.news.bean.NewsChannel.NewsChannelItems;
import com.lhycode.news.bean.NewsData;
import com.lhycode.news.bean.NewsData.NewsMenuData;
import com.lhycode.news.fragment.LeftMenuFragment;
import com.lhycode.news.global.GlobalContent;
import com.lhycode.news.utils.ApiUtils;
import com.lhycode.news.utils.CacheUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import BaseDetailPager.BaseDetailMenuPager;
import BaseDetailPager.SearchMenuDatailPager;
import BaseDetailPager.NewsMenuDetailPager;
import BaseDetailPager.PhotoMenuDatailPager;
import BaseDetailPager.HistoryMenuDatailPager;
import BaseDetailPager.WechatDetailPager;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewsCenterPager extends BasePager {
    public List<BaseDetailMenuPager> detailMenuPagers;
    private NewsData mNewsData;
    private NewsChannel newsChannelList;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        title.setText("新闻中心");
        setSlidingMenuEnable(true);

        String cache = CacheUtil.getCache(activity,
                "newsChannel");
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
        getDataFromServer();
    }

    private Handler handler;
    private NewsChannelItems newsChannelItems;
    private ArrayList<NewsChannelItem> newsChannelItemList;

    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("apikey", "052f6a481873bd14b533c8b45745a845");
        utils.send(HttpMethod.GET,
                "http://apis.baidu.com/showapi_open_bus/channel_news/channel_news"
                        + "?" + "", requestParams,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String result = (String) responseInfo.result;
                        System.out.println(result);
                        parseData(result);
                        CacheUtil.setCache(activity, "newsChannel", result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
    }

    public void parseData(String result) {
        Gson gson = new Gson();
        newsChannelList = gson.fromJson(result, NewsChannel.class);
        newsChannelItems = newsChannelList.showapi_res_body;
        newsChannelItemList = newsChannelItems.channelList;

        MainActivity mainActivity = (MainActivity) activity;
        LeftMenuFragment fragment = mainActivity.getLeftFragment();
        fragment.setMenuData();

        detailMenuPagers = new ArrayList<BaseDetailMenuPager>();
        // detailMenuPagers.add(new NewsMenuDetailPager(activity, mNewsData.data
        // .get(0).children,newsChannel.result));
        detailMenuPagers.add(new NewsMenuDetailPager(activity,
                newsChannelItemList));
//        detailMenuPagers.add(new HistoryMenuDatailPager(activity));
        detailMenuPagers.add(new WechatDetailPager(activity));
        detailMenuPagers.add(new PhotoMenuDatailPager(activity, gridButton));
        detailMenuPagers.add(new SearchMenuDatailPager(activity));
        setCurrentDetailPager(0);
    }

    public void setCurrentDetailPager(int position) {
        BaseDetailMenuPager detailMenuPager = detailMenuPagers.get(position);
        content.removeAllViews();
        content.addView(detailMenuPager.rootView);
        // NewsMenuData menuData = mNewsData.data.get(position);
        // title.setText(menuData.title);
        switch (position) {
            case 0:
                title.setText("新闻中心");
                break;
            case 1:
                title.setText("微信头条");
                break;
            case 2:
                title.setText("组图");
                break;
            case 3:
                title.setText("搜索");
                break;
            default:
                break;
        }
        detailMenuPager.initData();
        if (detailMenuPager instanceof PhotoMenuDatailPager) {
            gridButton.setVisibility(View.VISIBLE);
        } else {
            gridButton.setVisibility(View.INVISIBLE);
        }
    }
}
