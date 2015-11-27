package com.njk.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njk.Global;
import com.njk.R;
import com.njk.bean.NearBean;
import com.njk.utils.Utils;
import com.njk.view.CustomListView;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class NearListAdapter extends BaseAdapter {
	private Activity context;
	private List<NearBean> nearBeanList;
	
	private DisplayImageOptions options;	
	
	private LayoutParams layoutParams;
	
	public NearListAdapter(Activity context, List<NearBean> nearBeanList) {
		this.context = context;
		this.nearBeanList = nearBeanList;
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.img_default_icon)
		.showImageForEmptyUri(R.mipmap.img_default_icon)
		.showImageOnFail(R.mipmap.img_default_icon)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
		
		int[] wh = Utils.getDisplayMetrics(context);

		layoutParams = new LayoutParams(wh[0], wh[0]*4/9);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nearBeanList.size();
	}

	@Override
	public NearBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return nearBeanList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		NearBean item = nearBeanList.get(arg0);
		
		if(arg1 == null){
			arg1 = LayoutInflater.from(context).inflate(R.layout.near_item_layout, null);
		}
		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
		
		ImageView itemImg = ViewHolder.get(arg1, R.id.item_img);
		
		itemImg.getLayoutParams().width = layoutParams.width;
		itemImg.getLayoutParams().height = layoutParams.height;

//		ImageLoader.getInstance().displayImage("http://img0.bdstatic.com/img/image/4a75a05f8041bf84df4a4933667824811426747915.jpg", faceImg, options);
		ImageLoader.getInstance().displayImage(Global.base_url + item.img, itemImg, options);

		List<String> tag = item.tag;
		CustomListView list = ViewHolder.get(arg1, R.id.list_layout);
		list.setAdapter(new TextTypeAdapter(context, tag));

		TextView range_text = ViewHolder.get(arg1,R.id.range_text);
		if(TextUtils.isEmpty(item.range)){
			item.range = "0";
		}
		range_text.setText(item.range+"km");

		TextView titleText = ViewHolder.get(arg1, R.id.title_text);
		titleText.setText(item.title);

		TextView discount_num_text = ViewHolder.get(arg1,R.id.discount_num_text);
		discount_num_text.setText("￥"+item.per_capita);

		TextView enjoy_num_text = ViewHolder.get(arg1,R.id.enjoy_num_text);
		enjoy_num_text.setText(item.view);
		
		return arg1;
	}
}
