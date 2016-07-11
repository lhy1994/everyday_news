package com.lhycode.news.bean;

import java.util.ArrayList;

public class TabData {
public int retcode;
@Override
public String toString() {
	return "TabData [data=" + data + "]";
}
public TabDetail data;
public class TabDetail{
	public String title;
	public String more;
	public ArrayList<TabNewsData> news;
	public ArrayList<TopNewsData> topnews;
	@Override
	public String toString() {
		return "TabDetail [title=" + title + ", news=" + news + ", topnews="
				+ topnews + "]";
	}
}
public class TabNewsData{
	public String id;
	public String listimage;
	public String pubdate;
	public String title;
	public String url;
	@Override
	public String toString() {
		return "TabNewsData [title=" + title + "]";
	}
	public String type;
}
public class TopNewsData{
	public String id;
	public String topimage;
	public String pubdate;
	public String title;
	@Override
	public String toString() {
		return "TopNewsData [title=" + title + "]";
	}
	public String url;
	public String type;
}
}
