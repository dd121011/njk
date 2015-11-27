package com.njk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njk.Global;
import com.njk.R;
import com.njk.bean.MyCheapBean;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Random;

public class MyCheapListAdapter extends BaseAdapter {
	private Activity context;
	private List<MyCheapBean> mListItems;

	private DisplayImageOptions options;

	private ViewGroup.LayoutParams layoutParams;

	public MyCheapListAdapter(Activity context, List<MyCheapBean> mListItems) {
		this.context = context;
		this.mListItems = mListItems;	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mListItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1 == null){
			arg1 = LayoutInflater.from(context).inflate(R.layout.mycheap_item, null);
		}
		MyCheapBean item = mListItems.get(arg0);

		int tmp1 = new Random().nextInt(getCount()+1)+1;
		int tmp2 = new Random().nextInt(arg0+1)+1;

		View coupon_item_bg_layout = ViewHolder.get(arg1, R.id.coupon_item_bg_layout);
		if(tmp1%(tmp2) == 0){
			coupon_item_bg_layout.setBackgroundResource(R.mipmap.coupon_item_bg2);
		}else {
			coupon_item_bg_layout.setBackgroundResource(R.mipmap.coupon_item_bg1);
		}

		ImageView item_img = ViewHolder.get(arg1, R.id.item_img);
//		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.face_test1, faceImg, options);
//		ImageLoader.getInstance().displayImage("https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", faceImg, options);
		ImageLoader.getInstance().displayImage(Global.base_url + item.family_img, item_img, options);

		TextView family_title_text = ViewHolder.get(arg1,R.id.family_title_text);
		family_title_text.setText(item.family_title);
		TextView areaname_text = ViewHolder.get(arg1,R.id.areaname_text);
		areaname_text.setText(item.areaname);
		TextView valid_date_text = ViewHolder.get(arg1,R.id.valid_date_text);
		valid_date_text.setText(item.starttime+"到"+item.endtime);
		TextView content_text = ViewHolder.get(arg1,R.id.content_text);
		content_text.setText(item.content);
		TextView money_text = ViewHolder.get(arg1,R.id.money_text);
		money_text.setText(item.money+"元");
		return arg1;
	}

}
