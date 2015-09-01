package com.njk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.njk.R;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.LinkedList;

public class RemarkListAdapter extends BaseAdapter {
	private Activity context;
	private LinkedList mListItems;
	
	private DisplayImageOptions options;	
	
	private ViewGroup.LayoutParams layoutParams;
	
	public RemarkListAdapter(Activity context, LinkedList mListItems) {
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.shop_remark_item, null);
		}
//		ImageView item_img = ViewHolder.get(arg1, R.id.combo_item_img);
//		
//		TextView info_text = ViewHolder.get(arg1, R.id.combo_info_text);
//		
//		TextView name_text = ViewHolder.get(arg1, R.id.combo_name_text);

		View topLine = ViewHolder.get(arg1, R.id.top_line);
		if(arg0 == 0){
			topLine.setVisibility(View.INVISIBLE);
		}else{
			topLine.setVisibility(View.VISIBLE);
		}
		View bottomLine = ViewHolder.get(arg1, R.id.bottom_line);
		if(arg0 == getCount()-1){
			bottomLine.setVisibility(View.INVISIBLE);
		}else{
			bottomLine.setVisibility(View.VISIBLE);
		}
		return arg1;
	}

}
