package com.lhycode.news.bean;

import java.util.ArrayList;

public class NewsChannel {
public String showapi_res_code;
public String showapi_res_error;
public NewsChannelItems showapi_res_body;
public class NewsChannelItems{
	public ArrayList<NewsChannelItem> channelList;
	public String ret_code;
	public String totalNum;
}
public class NewsChannelItem{
	public String channelId;
	public String name;
}
}
