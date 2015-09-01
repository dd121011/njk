package com.njk.adapter;

import android.app.Activity;
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
	public Object getItem(int arg0) {
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
		
//		ImageView faceImg = ViewHolder.get(arg1, R.id.face_img);
//		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.face_test1, faceImg, options);	
		ImageLoader.getInstance().displayImage("http://img0.bdstatic.com/img/image/4a75a05f8041bf84df4a4933667824811426747915.jpg", faceImg, options);
		ImageLoader.getInstance().displayImage(Global.base_url+item.img, itemImg, options);
		
		CustomListView list = ViewHolder.get(arg1, R.id.list_layout);
		list.setAdapter(new TextAdapter(context, textArra));
		
		TextView titleText = ViewHolder.get(arg1, R.id.title_text);
		titleText.setText(item.title);
		
		return arg1;
	}
	
	String[] textArra = {"采摘","垂钓","祈福","农家"};
	class TextAdapter extends BaseAdapter{
		String[] texts = null;
		Activity context = null;
		public TextAdapter(Activity context, String[] textArra) {
			texts = textArra;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return texts.length;
		}

		@Override
		public Object getItem(int position) {

			return texts[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.near_item_text_layout, null);
			}
			TextView t = ViewHolder.get(convertView, R.id.item_text);
			t.setText(texts[position]);
			
			
			return convertView;
		}
		
	}
}
