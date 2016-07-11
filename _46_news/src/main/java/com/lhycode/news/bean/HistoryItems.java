package com.lhycode.news.bean;

import java.util.ArrayList;

public class HistoryItems {
public String error_code;
public String reason;
public ArrayList<HistoryItem> result;
public class HistoryItem{
	public String id;
	public String day;
	public String des;
	public String lunar;
	public String month;
	public String pic;
	public String title;
	public String year;
}
}
