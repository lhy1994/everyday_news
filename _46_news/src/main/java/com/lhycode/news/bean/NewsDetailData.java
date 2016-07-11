package com.lhycode.news.bean;

import java.util.ArrayList;

public class NewsDetailData {
	public String showapi_res_code;
	public String showapi_res_error;

	@Override
	public String toString() {
		return "NewsDetailData [showapi_res_code=" + showapi_res_code
				+ ", showapi_res_error=" + showapi_res_error
				+ ", showapi_res_body=" + showapi_res_body + "]";
	}

	public NewsItems showapi_res_body;

	public class NewsItems {
		public NewsPages pagebean;
		public String ret_code;

		@Override
		public String toString() {
			return "NewsItems [pagebean=" + pagebean + ", ret_code=" + ret_code
					+ "]";
		}
	}

	public class NewsPages {
		public String allNum;
		public String allPages;
		public ArrayList<NewsItem> contentlist;
		public String currentPage;

		@Override
		public String toString() {
			return "NewsPages [allNum=" + allNum + ", allPages=" + allPages
					+ ", contentlist=" + contentlist + ", currentPage="
					+ currentPage + ", maxResult=" + maxResult + "]";
		}

		public String maxResult;
	}

	public class NewsItem {
		public String channelId;
		public String channelName;
		public String desc;
		public ArrayList<ImageInfo> imageurls;
		public String link;
		public String nid;
		public String pubDate;
		public String source;

		@Override
		public String toString() {
			return "NewsItem [channelId=" + channelId + ", channelName="
					+ channelName + ", desc=" + desc + ", imageurls="
					+ imageurls + ", link=" + link + ", nid=" + nid
					+ ", pubDate=" + pubDate + ", source=" + source
					+ ", title=" + title + "]";
		}

		public String title;
	}

	public class ImageInfo {
		public String height;
		public String url;
		public String width;

		@Override
		public String toString() {
			return "ImageInfo [height=" + height + ", url=" + url + ", width="
					+ width + "]";
		}
	}
}
