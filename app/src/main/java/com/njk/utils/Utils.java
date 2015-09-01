package com.njk.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.njk.R;

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

}
