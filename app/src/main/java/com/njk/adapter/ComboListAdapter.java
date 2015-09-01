package com.njk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njk.R;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.LinkedList;

public class ComboListAdapter extends BaseAdapter {
	private Activity context;
	private LinkedList mListItems;
	
	private DisplayImageOptions options;	
	
	private ViewGroup.LayoutParams layoutParams;
	
	public ComboListAdapter(Activity context, LinkedList mListItems) {
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.shop_combo_item, null);
		}
		ImageView item_img = ViewHolder.get(arg1, R.id.combo_item_img);
		
		TextView info_text = ViewHolder.get(arg1, R.id.combo_info_text);
		
		TextView name_text = ViewHolder.get(arg1, R.id.combo_name_text);
		switch (arg0) {
		case 0:
			item_img.setImageResource(R.mipmap.combo_icon1);
			name_text.setText("洗剪吹");
			break;
		case 1:
			item_img.setImageResource(R.mipmap.combo_icon2);
			name_text.setText("染发");
			break;
		case 2:
			item_img.setImageResource(R.mipmap.combo_icon3);
			name_text.setText("烫发");
			break;
		case 3:
			item_img.setImageResource(R.mipmap.combo_icon4);
			name_text.setText("护发");
			break;
		case 4:
			item_img.setImageResource(R.mipmap.combo_icon5);
			name_text.setText("其他");
			break;
		default:
			break;
		}

		return arg1;
	}

}
