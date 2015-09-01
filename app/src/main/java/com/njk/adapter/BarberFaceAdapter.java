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
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.LinkedList;

public class BarberFaceAdapter extends BaseAdapter {
	private Activity context;
	private LinkedList mListItems;
	
	private DisplayImageOptions options;	
	
	private LayoutParams layoutParams;
	
	public BarberFaceAdapter(Activity context, LinkedList mListItems) {
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.barber_face_item, null);
		}
		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
		
		
		
//		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
//		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.face_test1, faceImg, options);	
//		ImageLoader.getInstance().displayImage("https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", faceImg, options);
		
		return arg1;
	}

}
