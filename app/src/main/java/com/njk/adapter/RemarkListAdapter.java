package com.njk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njk.R;
import com.njk.bean.ReviewBean;
import com.njk.utils.Utils;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

public class RemarkListAdapter extends BaseAdapter {
	private Activity context;
	private List<ReviewBean> reviewBeanList;
	
	private DisplayImageOptions options;	
	
	private ViewGroup.LayoutParams layoutParams;
	
	public RemarkListAdapter(Activity context, List<ReviewBean>  reviewBeanList) {
		this.context = context;
		this.reviewBeanList = reviewBeanList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return reviewBeanList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return reviewBeanList.get(arg0);
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

		ReviewBean item = reviewBeanList.get(arg0);

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

		TextView nick_name_text = ViewHolder.get(arg1,R.id.nick_name_text);
		nick_name_text.setText(item.getNickname());
		TextView create_time_text = ViewHolder.get(arg1,R.id.create_time_text);
		create_time_text.setText(Utils.getTime(item.getDatetime()));
		TextView content_text = ViewHolder.get(arg1,R.id.content_text);
		content_text.setText(item.getContent());
		TextView day_text = ViewHolder.get(arg1,R.id.day_text);
		day_text.setText(Utils.getDay(item.getDatetime())+"");
		TextView month_text = ViewHolder.get(arg1,R.id.month_text);
		month_text.setText(Utils.monthString(item.getDatetime())+"");


		return arg1;
	}

}
