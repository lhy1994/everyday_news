package com.lhycode.news.bean;

import java.util.ArrayList;

public class TicketInfo {
public String errNum;
public String errMsg;
public Retdata retData;
public class Retdata{
	public String id;
	public TicketDetail ticketDetail;
	@Override
	public String toString() {
		return "Retdata [id=" + id + ", ticketDetail=" + ticketDetail + "]";
	}
}
public class TicketDetail{
	public String loc;
	public String lastmod;
	public String changefreq;
	public String priority;
	public Data data;
	@Override
	public String toString() {
		return "TicketDetail [loc=" + loc + ", lastmod=" + lastmod
				+ ", changefreq=" + changefreq + ", priority=" + priority
				+ ", data=" + data + "]";
	}
}
public class Data{
	public Display display;

	@Override
	public String toString() {
		return "Data [display=" + display + "]";
	}
}
public class Display{
	public Ticket ticket;
	public String category;
	public String subcate;
	public String source;
	@Override
	public String toString() {
		return "Display [ticket=" + ticket + ", category=" + category
				+ ", subcate=" + subcate + ", source=" + source + "]";
	}
}
public class Ticket{
	public String spotName;
	public String alias;
	public String spotID;
	public String description;
	public String detailUrl;
	public String address;
	public String province;
	public String city;
	public String coordinates;
	public String imageUrl;
	public String comments;
//	public ArrayList<Price> priceList;
//	@Override
	/*public String toString() {
		return "Ticket [spotName=" + spotName + ", alias=" + alias
				+ ", spotID=" + spotID + ", description=" + description
				+ ", detailUrl=" + detailUrl + ", address=" + address
				+ ", province=" + province + ", city=" + city
				+ ", coordinates=" + coordinates + ", imageUrl=" + imageUrl
				+ ", comments=" + comments + ", priceList=" + priceList + "]";
	}*/
}
public class Price{
	public String ticketTitle;
	public String ticketID;
	public String priceType;
	public String price;
	public String normalPrice;
	public String num;
	public String type;
	public String bookUrl;
	@Override
	public String toString() {
		return "Price [ticketTitle=" + ticketTitle + ", ticketID=" + ticketID
				+ ", priceType=" + priceType + ", price=" + price
				+ ", normalPrice=" + normalPrice + ", num=" + num + ", type="
				+ type + ", bookUrl=" + bookUrl + "]";
	}
}
}
