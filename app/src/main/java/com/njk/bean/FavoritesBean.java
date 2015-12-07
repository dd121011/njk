package com.njk.bean;

import java.io.Serializable;
import java.util.List;

public class FavoritesBean implements Serializable {
//	{
//		"id": "7",
//			"user_id": "23",
//			"family_id": "241",
//			"title": "长峪城逍乐原农家院",
//			"img": "/tmp/upload/c/4/1/family/8e/29/14406422907799.jpg",
//			"tag": [
//		"农家饭",
//				"自助烧烤"
//		],
//		"address": "北京市昌平区S219(南雁路)",
//			"view": "187",
//			"per_capita": "100",
//			"comment": "0"
//	}
	private String id;
	private String user_id;
	private String family_id;
	private String title;
	private String img;
	private List<String> tag;
	private String address;
	private String view;
	private String per_capita;
	private String comment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFamily_id() {
		return family_id;
	}

	public void setFamily_id(String family_id) {
		this.family_id = family_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public List<String> getTag() {
		return tag;
	}

	public void setTag(List<String> tag) {
		this.tag = tag;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getPer_capita() {
		return per_capita;
	}

	public void setPer_capita(String per_capita) {
		this.per_capita = per_capita;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "FavoritesBean{" +
				"id='" + id + '\'' +
				", user_id='" + user_id + '\'' +
				", family_id='" + family_id + '\'' +
				", title='" + title + '\'' +
				", img='" + img + '\'' +
				", tag=" + tag +
				", address='" + address + '\'' +
				", view='" + view + '\'' +
				", per_capita='" + per_capita + '\'' +
				", comment='" + comment + '\'' +
				'}';
	}
}
