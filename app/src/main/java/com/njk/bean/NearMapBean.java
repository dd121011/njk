package com.njk.bean;

import java.io.Serializable;
import java.util.List;

public class NearMapBean implements Serializable {
//			"id": "241",
//			"title": "长峪城逍乐原农家院",
//			"img": "/tmp/upload/c/4/1/family/8e/29/14406422907799.jpg",
//			"name": "王广友",
//			"province": "1",
//			"city": "200",
//			"view": "103",
//			"per_capita": "100",
//			"tag": [
//			"农家饭",
//			"自助烧烤"
//			],
//			"category_id": "2",
//			"address": "北京市昌平区S219(南雁路)",
//			"lat": "40.184217",
//			"lng": "116.079688",
//			"range": "43.27"
	public String id;
	public String title;
	public String img;
	public String name;
	public String province;
	public String city;
	public String view;
	public String per_capita;
	public List<String> tag;
	public String category_id;
	public String address;
	public String lat;
	public String lng;
	public String range;

	@Override
	public String toString() {
		return "NearMapBean{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", img='" + img + '\'' +
				", name='" + name + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", view='" + view + '\'' +
				", per_capita='" + per_capita + '\'' +
				", tag=" + tag +
				", category_id='" + category_id + '\'' +
				", address='" + address + '\'' +
				", lat='" + lat + '\'' +
				", lng='" + lng + '\'' +
				", range='" + range + '\'' +
				'}';
	}
}
