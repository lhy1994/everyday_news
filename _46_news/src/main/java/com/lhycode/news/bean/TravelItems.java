package com.lhycode.news.bean;

import java.util.ArrayList;

public class TravelItems {
	public String errNum;
	public String errMsg;
	public TravelItem retData;

	public class TravelItem {
		public String pageNo;
		public String pageSize;
		public String totalRecord;
		public ArrayList<TicketItem> ticketList;
	}

	public class TicketItem {
		public String productId;
		public String spotName;
		public ArrayList<String> spotAliasName;
	}

}
