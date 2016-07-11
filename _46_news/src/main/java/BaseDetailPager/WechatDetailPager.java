package BaseDetailPager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lhycode.news.NewsDetailActivity;
import com.lhycode.news.R;
import com.lhycode.news.bean.WechatItems;
import com.lhycode.news.view.RefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * Created by liuhaoyuan on 2016/6/10.
 */
public class WechatDetailPager extends BaseDetailMenuPager {

    private View view;
    private RefreshListView refreshListView;
    private ArrayList<WechatItems.Article> articleList;
    private WechatItems wechatItems;
    private WechatListAdapter adapter;

    public WechatDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        view = View.inflate(activity, R.layout.wechat_pager, null);
        refreshListView = (RefreshListView) view.findViewById(R.id.listview_wechat);
        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("apikey", "93b6d2e9b7bb5f09b925732431b7e991");
        httpUtils.send(HttpMethod.GET, "http://apis.baidu.com/3023/weixin/channel?" + "id=popular&page=1", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        wechatItems = gson.fromJson(result, WechatItems.class);
        WechatItems.Data data = wechatItems.data;
        articleList = data.article;
        adapter = new WechatListAdapter();
        refreshListView.setAdapter(adapter);
    }

    class WechatListAdapter extends BaseAdapter {

        private BitmapUtils utils;

        public WechatListAdapter() {
            this.utils = new BitmapUtils(activity);
            utils.configDefaultLoadingImage(R.drawable.news_logo);
        }

        @Override
        public int getCount() {
            return articleList.size();
        }

        @Override
        public Object getItem(int position) {
            return articleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(activity, R.layout.wechat_list_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.tv_wechat_title);
                holder.author = (TextView) convertView.findViewById(R.id.tv_auhtor);
                holder.pic = (ImageView) convertView.findViewById(R.id.iv_wechat_pic);
                holder.button = (Button) convertView.findViewById(R.id.btn_wechat);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(articleList.get(position).title);
            holder.author.setText(articleList.get(position).author);
            utils.display(holder.pic,articleList.get(position).img);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity,NewsDetailActivity.class);
                    intent.putExtra("url",articleList.get(position).url);
                    activity.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView title;
        TextView author;
        ImageView pic;
        Button button;
    }
}
