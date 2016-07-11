package com.lhycode.news.fragment;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lhycode.news.MainActivity;
import com.lhycode.news.R;
import com.lhycode.news.bean.NewsData;
import com.lhycode.news.bean.NewsData.NewsMenuData;

import Base.NewsCenterPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenuFragment extends BaseFragment {

    public ListView listView;
    private ArrayList<NewsMenuData> menuList;
    public int currentListItem;
    private MenuListAdapter adapter;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public View initView() {
        View view = View.inflate(activity, R.layout.fragment_left_menu, null);
        listView = (ListView) view.findViewById(R.id.lv_left);
        setMenuData();
        return view;
    }

    @Override
    public void initData() {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                currentListItem = position;
                arrayAdapter.notifyDataSetChanged();

                setCurrentDetailPager(position);

                toggleSlidingMenu();
            }
        });

    }

    protected void toggleSlidingMenu() {
        MainActivity mainActivity = (MainActivity) activity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }

    protected void setCurrentDetailPager(int position) {
        MainActivity mainActivity = (MainActivity) activity;
        ContentFragment fragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        newsCenterPager.setCurrentDetailPager(position);
    }

    public void setMenuData() {
        String[] objects = new String[]{
                "新闻中心",
                "微信头条",
                "新闻组图",
                "新闻搜索"
        };
        arrayAdapter=new ArrayAdapter<String>(activity,R.layout.left_menu_item,R.id.tv_left_menu,objects);
//        arrayAdapter = new ArrayAdapter<String>(activity, R.layout.left_menu_item, R.id.tv_left_menu, objects);
        if (listView!=null){
            listView.setAdapter(arrayAdapter);
        }
    }

    class MenuListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return menuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = View.inflate(activity, R.layout.left_menu_item, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_left_menu);
            NewsMenuData data = (NewsMenuData) getItem(position);
            textView.setText(data.title);
            if (currentListItem == position) {
                textView.setEnabled(true);

            } else {
                textView.setEnabled(false);
            }

            return view;
        }

    }
}
