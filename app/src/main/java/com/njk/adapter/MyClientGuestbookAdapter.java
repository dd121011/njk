package com.njk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.njk.R;
import com.njk.utils.Utils;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.LinkedList;

public class MyClientGuestbookAdapter extends BaseAdapter {
	private Activity context;
	private LinkedList mListItems;
	
	private DisplayImageOptions options;	
	
	private LayoutParams layoutParams;
	
	public MyClientGuestbookAdapter(Activity context, LinkedList mListItems) {
		this.context = context;
		this.mListItems = mListItems;
		
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.my_client_guestbook_item_layout, null);
		}
		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
		
		

		
//		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
		ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.img_default_icon, faceImg, options);
		ImageLoader.getInstance().displayImage("http://img0.bdstatic.com/img/image/4a75a05f8041bf84df4a4933667824811426747915.jpg", faceImg, options);
		
		return arg1;
	}

}
