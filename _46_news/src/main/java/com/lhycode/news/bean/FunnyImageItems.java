package com.lhycode.news.bean;

import java.util.ArrayList;

import android.R.string;

public class FunnyImageItems {
public String showapi_res_code;
public String showapi_res_error;
public FunnyImagePages showapi_res_body;
public class FunnyImagePages{
	public String allNum;
	public String allPages;
	public ArrayList<FunnyImageItem> contentlist;
	public String currentPage;
	public String maxResult;
	public String ret_code;

}
public class FunnyImageItem{
	public String ct;
	public String img;
	public String title;
	public String type;
}
}
