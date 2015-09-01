package com.njk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.njk.R;
import com.njk.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.LinkedList;

public class BarberDetailsWorksAdapter extends BaseAdapter {
	private Context context;
	private LinkedList mListItems;
	
	private DisplayImageOptions options;	
	private int[] screenWH;
	private int WSpace;
	
	public BarberDetailsWorksAdapter(Activity context, LinkedList mListItems) {
		this.context = context;
		this.mListItems = mListItems;
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.img_default_icon)
		.showImageForEmptyUri(R.mipmap.img_default_icon)
		.showImageOnFail(R.mipmap.img_default_icon)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(90))
		.build();
		
		screenWH = Utils.getDisplayMetrics(context);
		WSpace = context.getResources().getDimensionPixelSize(R.dimen.content_margin_outside);
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.barber_works_item_layout, null);
		}
		ImageView faceImg = (ImageView) arg1.findViewById(R.id.work_img);
//		Logger.d("WSpace = "+WSpace);
//		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
//		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.face_test1, faceImg, options);	
//		ImageLoader.getInstance().displayImage("https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", faceImg, options);
		
		return arg1;
	}

}
