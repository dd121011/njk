package com.njk.bean;

import java.io.Serializable;

public class FavoritesBean implements Serializable {
	private String id;
	private String user_id;
	private String message_id;
	private String status;
	private String thetype;
	private String create_time;
	private String source;
	private String title;
	private String img;
	private String tag;
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
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getThetype() {
		return thetype;
	}
	public void setThetype(String thetype) {
		this.thetype = thetype;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
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
		return "FavoritesBean [id=" + id + ", user_id=" + user_id + ", message_id=" + message_id + ", status=" + status + ", thetype=" + thetype + ", create_time=" + create_time + ", source=" + source + ", title=" + title + ", img=" + img + ", tag=" + tag + ", address=" + address + ", view=" + view + ", per_capita=" + per_capita + ", comment=" + comment + "]";
	}
	
}
