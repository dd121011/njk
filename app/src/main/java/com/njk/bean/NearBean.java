package com.njk.bean;

import java.io.Serializable;
import java.util.List;

public class NearBean implements Serializable {
//	"id": "250",
//			"title": "浦江仙人居农家乐",
//			"img": "/tmp/upload/c/4/1/family/1f/0e/14414538174170.jpg",
//			"name": "方宣义",
//			"province": "12",
//			"city": "256",
//			"view": "109",
//			"per_capita": "100",
//			"tag": [
//			"农家饭",
//			"住宿"
//			],
//			"lat": "",
//			"lng": "",
//			"range": "0"
	public String id;
	public String title;
	public String img;
	public String name;
	public String province;
	public String city;
	public String view;
	public String per_capita;
	public List<String> tag;
	public String lat;
	public String lng;
	public String range;

	@Override
	public String toString() {
		return "NearBean{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", img='" + img + '\'' +
				", name='" + name + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", view='" + view + '\'' +
				", per_capita='" + per_capita + '\'' +
				", tag=" + tag +
				", lat='" + lat + '\'' +
				", lng='" + lng + '\'' +
				", range='" + range + '\'' +
				'}';
	}
}
