package com.njk.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class CustomListView extends LinearLayout {
	private String TAG = "CustomListView";
	private Context context;
	private BaseAdapter adapter;
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public CustomListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context){
		this.context = context;
	}
	
	
	public void setAdapter(BaseAdapter adapter){
		if(adapter == null){
			return;
		}
		
		adapter.registerDataSetObserver(dataSetObserver);
		this.adapter = adapter;
		
		initChildViews();
	}
	
	
	private void initChildViews() {
		if(this.getChildCount()>0){
			this.removeAllViews();
		}

//		float offsetTop = context.getResources().getDimension(R.dimen.order_volume_offset_top);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
//		lp.setMargins(0,(int)offsetTop, 0, 0);
		for(int i=0;i<adapter.getCount();i++){
//			if(i==0){
				this.addView(adapter.getView(i, null, this));
//			}else{
//				this.addView(adapter.getView(i, null, this),lp);
//			}
		}
	}
	
	private DataSetObserver dataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			// TODO Auto-generated method stub
			super.onChanged();
			initChildViews();
		}
	};
}
