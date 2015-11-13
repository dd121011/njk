package com.njk.net;

public enum RequestCommandEnum {
	FAMILY_LIST("/api/family/familylist"),
	SEND_CODE_DO("/api/register/send_code_do"),
	CHECK_MOBILE_DO("/api/register/check_mobile_do"),
	LOGIN("/api/account/login"),
	APPINFOS_AREAS("/api/appinfos/areas"),
	FAMILY_GETSCENIC("/api/family/getscenic"),
	FAMILY_INFO("/api/family/family_info"),
	FINDFAMILY_LISTLBS("/api/family/findfamilyListlbs"),
	FAMILY_FAV_DO("/api/family/fav_do"),
	FAMILY_CANCEL_FAV("/api/family/cancel_fav"),
	FAMILY_REVIEW_DO("/api/family/review_do"),
	FAMILY_DETAIL("/api/family/detail"),
	FAMILY_REVIEW_LIST("/api/family/review_list"),
	USERINFO_INDEX("/api/userinfo/index"),
	USERINFO_MY_FAV("/api/userinfo/my_fav"),
	ACCOUNT_LOGOUT("/api/account/logout"),
	COUPON_INDEX("/api/coupon/index"),
	COUPON_DETAILS("/api/coupon/details"),
	COUPON_MY_COUPON("/api/coupon/my_coupon"),
	ACCOUNT_MOBILE_AUTHEN("/api/account/mobile_authen"),
	ACCOUNT_UPDATE_PASSWORD("/api/account/update_password");

	public String command;
	RequestCommandEnum(String name) {
		this.command = name;
	}
	

}
