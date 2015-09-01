package com.njk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.njk.R;
import com.njk.utils.LocalDisplay;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.LinkedList;

public class EncircleListAdapter extends BaseAdapter {
	private Activity context;
	private LinkedList mListItems;
	
	private DisplayImageOptions options;	
	
	private int sGirdImageSize;
	
	public EncircleListAdapter(Activity context, LinkedList mListItems) {
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
		

		sGirdImageSize = (LocalDisplay.SCREEN_WIDTH_PIXELS - LocalDisplay.dp2px(12 + 12 + 10)) / 2;
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.encircle_item_layout, null);
		}
//		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
		
		ImageView itemImg = ViewHolder.get(arg1, R.id.item_img);
		
		itemImg.getLayoutParams().width = sGirdImageSize;
		itemImg.getLayoutParams().height = sGirdImageSize;
		
//		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
//		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.face_test1, itemImg, options);	
		ImageLoader.getInstance().displayImage("http://d.hiphotos.baidu.com/image/pic/item/b64543a98226cffc2d1e40b8ba014a90f603ea80.jpg", itemImg, options);
		
		
		View tmp_view =ViewHolder.get(arg1, R.id.tmp_view);
		if(arg0 == mListItems.size()-1){
			tmp_view.setVisibility(View.INVISIBLE);
		}else{
			tmp_view.setVisibility(View.GONE);
		}
		return arg1;
	}

}
