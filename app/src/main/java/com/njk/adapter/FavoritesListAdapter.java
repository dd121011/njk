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
import com.njk.bean.FavoritesBean;
import com.njk.view.CustomListView;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class FavoritesListAdapter extends BaseAdapter {
	private Activity context;
	private List<FavoritesBean> dataList;
	
	private DisplayImageOptions options;	
	
	
	public FavoritesListAdapter(Activity context, List<FavoritesBean> dataList) {
		this.context = context;
		this.dataList = dataList;
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.img_default_icon)
		.showImageForEmptyUri(R.mipmap.img_default_icon)
		.showImageOnFail(R.mipmap.img_default_icon)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
//		.displayer(new RoundedBitmapDisplayer(90))
		.displayer(new SimpleBitmapDisplayer())
		.build();
		

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public FavoritesBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataList.get(arg0);
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.favorites_item_layout, null);
		}
		FavoritesBean item = dataList.get(arg0);
		
		ImageView itemImg = ViewHolder.get(arg1, R.id.item_img);
		TextView name_text = ViewHolder.get(arg1, R.id.name_text);
		name_text.setText(item.getTitle());
		
		TextView address_text = ViewHolder.get(arg1, R.id.address_text);
		address_text.setText(item.getAddress());
		
//		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.face_test1, faceImg, options);	
		ImageLoader.getInstance().displayImage(Global.base_url + item.getImg(), itemImg, options);

		TextView view_text = ViewHolder.get(arg1, R.id.view_text);
		view_text.setText("人气" + item.getView());
		TextView comment_text = ViewHolder.get(arg1, R.id.comment_text);
		comment_text.setText("点评"+item.getComment());
		TextView per_capita_text = ViewHolder.get(arg1, R.id.per_capita_text);
		per_capita_text.setText("人均(￥)"+item.getPer_capita());

		CustomListView list = ViewHolder.get(arg1, R.id.list_layout);
		List<String> tag = item.getTag();
		if(tag!=null){
			list.setAdapter(new TextTypeAdapter(context, tag));
		}

		return arg1;
	}

}
