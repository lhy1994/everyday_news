package com.lhycode.news.bean;

import java.util.ArrayList;

public class FunnyTextItems {
public String showapi_res_code;
public String showapi_res_error;
public FunnyTextPages showapi_res_body;
@Override
public String toString() {
	return "FunnyTextItems [showapi_res_code=" + showapi_res_code
			+ ", showapi_res_error=" + showapi_res_error
			+ ", showapi_res_body=" + showapi_res_body + "]";
}
public class FunnyTextPages{
	public String allNum;
	public String allPages;
	public ArrayList<FunnyTextItem> contentlist;
	public String currentPage;
	public String maxResult;
	public String ret_code;
	@Override
	public String toString() {
		return "FunnyTextPages [allNum=" + allNum + ", allPages=" + allPages
				+ ", contentlist=" + contentlist + ", currentPage="
				+ currentPage + ", maxResult=" + maxResult + ", ret_code="
				+ ret_code + "]";
	}
}
public class FunnyTextItem{
	public String ct;
	public String text;
	public String title;
	public String type;
	@Override
	public String toString() {
		return "FunnyTextItem [ct=" + ct + ", text=" + text + ", title="
				+ title + ", type=" + type + "]";
	}
}
}
