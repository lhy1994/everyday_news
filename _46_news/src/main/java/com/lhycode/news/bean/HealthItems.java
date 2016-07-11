package com.lhycode.news.bean;

import java.util.ArrayList;

public class HealthItems {
public String status;
public String total;
public ArrayList<HealthItem> tngou;
public class HealthItem{
	public String askclass;
	public String count;
	public String description;
	public String fcount;
	public String id;
	public String img;
	public String keywords;
	public String rcount;
	public String time;
	public String title;
	@Override
	public String toString() {
		return "HealthItem [askclass=" + askclass + ", count=" + count
				+ ", description=" + description + ", fcount=" + fcount
				+ ", id=" + id + ", img=" + img + ", keywords=" + keywords
				+ ", rcount=" + rcount + ", time=" + time + ", title=" + title
				+ "]";
	}
}
}
