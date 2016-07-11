package com.lhycode.news.bean;

import java.util.ArrayList;

public class HealthChannels {
public String status;
public ArrayList<HealthChannel> tngou;
public class HealthChannel{
	public String description;
	public String id;
	public String keywords;
	public String name;
	public String seq;
	public String title;
	@Override
	public String toString() {
		return "HealthChannel [description=" + description + ", id=" + id
				+ ", keywords=" + keywords + ", name=" + name + ", seq=" + seq
				+ ", title=" + title + "]";
	}
}
}
