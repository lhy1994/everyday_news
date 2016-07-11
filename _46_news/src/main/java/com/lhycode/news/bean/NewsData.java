package com.lhycode.news.bean;

import java.security.PublicKey;
import java.util.ArrayList;

public class NewsData {
	public int retcode;
	public ArrayList<NewsMenuData> data;

	@Override
	public String toString() {
		return "NewsData [data=" + data + "]";
	}

	public class NewsMenuData {
		public String id;
		public String title;
		public int type;
		public String url;
		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}
		public ArrayList<NewsTabData> children;
	}

	public class NewsTabData {
		public String id;
		public String title;
		public int type;
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
		public String url;

	}
}
