package com.lhycode.news.bean;

import java.util.ArrayList;

/**
 * Created by liuhaoyuan on 2016/6/10.
 */
public class WechatItems {
    public String code;
    public Data data;

    public class Data {
        public String channel;
        public ArrayList<Article> article;
    }

    public class Article {
        public String title;
        public String url;
        public String img;
        public String author;
        public String time;
    }
}


