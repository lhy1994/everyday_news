package com.lhycode.news.bean;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class PhotoNewsItems {
	public String errNum;
	public String errMsg;
	public ArrayList<PhotoNewsItem> retData;

	public class PhotoNewsItem {
		public String title;
		public String url;

		@Override
		public String toString() {
			return "PhotoNewsItem [title=" + title + ", url=" + url
					+ ", isabstract=" + isabstract + ", image_url=" + image_url
					+ "]";
		}
		@SerializedName("abstract") 
		public String isabstract;
		public String image_url;
	}
}
