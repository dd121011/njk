package com.njk.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private String id;
	private String nickname;
	private String avatar;
	private String sex;
	private String mobile;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", nickname=" + nickname + ", avatar=" + avatar + ", sex=" + sex + ", mobile=" + mobile + "]";
	}
	
}
