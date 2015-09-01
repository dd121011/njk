package com.njk.bean;

import java.io.Serializable;

public class ReviewBean implements Serializable {
	private String id;
	private String user_id;
	private String family_id;
	private String status;
	private String source;
	private String content;
	private String createtime;
	private String nickname;
	private String datetime;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	@Override
	public String toString() {
		return "ReviewBean [id=" + id + ", user_id=" + user_id + ", family_id=" + family_id + ", status=" + status + ", source=" + source + ", content=" + content + ", createtime=" + createtime + ", nickname=" + nickname + ", datetime=" + datetime + "]";
	}
	
}
