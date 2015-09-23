package com.njk.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.njk.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	public enum TOP_BTN_MODE {
		SHOWBACK,SHOWLEFTTEXT, SHOUSHARE, SHOWRIGHTTEXT,SHOWBOTH,SHOWTEXT,NOSHOWTEXT;		
	}
	
	
	public enum COMBO_ENUM{
		COMBO_1,COMBO_2,COMBO_3,COMBO_4,COMBO_5
	}

	/**
	 * 显示头部按钮
	 * 
	 * @param rootView
	 * @param title
	 */
	public static void showTopBtn(View rootView, String title,
			TOP_BTN_MODE mode,String leftText, String rightText) {
		TextView textView = (TextView) rootView.findViewById(R.id.title_text);
		textView.setText(title);
		switch (mode) {
		case SHOWBACK:
			rootView.findViewById(R.id.back_btn).setVisibility(View.VISIBLE);
			rootView.findViewById(R.id.share_btn).setVisibility(View.INVISIBLE);
			break;
		case SHOUSHARE:
			rootView.findViewById(R.id.back_btn).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.share_btn).setVisibility(View.VISIBLE);
			break;
		case SHOWBOTH:
			rootView.findViewById(R.id.back_btn).setVisibility(View.VISIBLE);
			rootView.findViewById(R.id.share_btn).setVisibility(View.VISIBLE);
			break;
		case SHOWTEXT:
			rootView.findViewById(R.id.back_btn).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.share_btn).setVisibility(View.INVISIBLE);
			break;
		case NOSHOWTEXT:
			textView.setVisibility(View.INVISIBLE);
			break;
		case SHOWRIGHTTEXT:
			ViewGroup group1 = (ViewGroup) rootView.findViewById(R.id.share_btn);
			group1.setVisibility(View.VISIBLE);
			Button btn1 = (Button) group1.getChildAt(0);
			btn1.setBackgroundColor(Color.TRANSPARENT);
			btn1.setText(rightText);
			break;
		case SHOWLEFTTEXT:
			ViewGroup group = (ViewGroup) rootView.findViewById(R.id.back_btn);
			group.setVisibility(View.VISIBLE);
			Button btn = (Button) group.getChildAt(0);
			btn.setBackgroundColor(Color.TRANSPARENT);
			btn.setText(leftText);
			break;
		default:
			break;
		}

	}
	
	/**
	 * 获得屏幕分辨率
	 * 
	 * @param activity
	 * @return Integer[width, height]
	 */
	public static int[] getDisplayMetrics(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return new int[] { dm.widthPixels, dm.heightPixels };
	}


	public static int getMonth(String s){
		int month = 1;
//		2015-05-29 10:53:47
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		try {
			Date date = format.parse(s);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			month = calendar.get(Calendar.MONTH)+1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return month;
	}

	public static String getDay(String s){
		String day = "";
//		2015-05-29 10:53:47
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		SimpleDateFormat format2 = new SimpleDateFormat("dd");
		try {
			Date date = format.parse(s);
			day = format2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	public static String getTime(String s){
		String time = "";
//		2015-05-29 10:53:47
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		SimpleDateFormat format2 = new SimpleDateFormat("H:m");
		try {
			Date date = format.parse(s);
			time = format2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String monthString(String s){
		int month = getMonth(s);
		String m = "一月";
		switch (month){
			case 1:
				m = "一月";
				break;
			case 2:
				m = "二月";
				break;
			case 3:
				m = "三月";
				break;
			case 4:
				m = "四月";
				break;
			case 5:
				m = "五月";
				break;
			case 6:
				m = "六月";
				break;
			case 7:
				m = "七月";
				break;
			case 8:
				m = "八月";
				break;
			case 9:
				m = "九月";
				break;
			case 10:
				m = "十月";
				break;
			case 11:
				m = "十一月";
				break;
			case 12:
				m = "十二月";
				break;
		}
		return m;
	}
}
