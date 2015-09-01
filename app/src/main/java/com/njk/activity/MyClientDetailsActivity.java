package com.njk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.njk.BaseActivity;
import com.njk.Global;
import com.njk.R;
import com.njk.adapter.MyClientGuestbookAdapter;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import java.util.Arrays;
import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class MyClientDetailsActivity extends BaseActivity implements OnClickListener{
	
	private ListView listView;
	private LinkedList<String> mListItems;
	private PtrClassicFrameLayout mPtrFrame;
	private MyClientGuestbookAdapter mAdapter;
	
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		View rootView = LayoutInflater.from(this).inflate(R.layout.my_client_details_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "我的美客", TOP_BTN_MODE.SHOWBACK,"","");
		findViewById(R.id.back_btn).setOnClickListener(this);
		

		listView = (ListView) rootView.findViewById(R.id.list_layout);
        listView.setOnItemClickListener(null);
        
        mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(Global.mStrings));
		mAdapter = new MyClientGuestbookAdapter(this, mListItems);
		
		listView.setAdapter(mAdapter);
	        
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;

		default:
			break;
		}
		
	}
	
}
