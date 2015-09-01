package com.njk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.njk.BaseActivity;
import com.njk.Global;
import com.njk.R;
import com.njk.adapter.MessageListAdapter;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import java.util.Arrays;
import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MessgeListActivity extends BaseActivity implements OnClickListener{
	
	private ListView listView;
	private LinkedList<String> mListItems;
	private PtrClassicFrameLayout mPtrFrame;
	private MessageListAdapter mAdapter;
	
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		View rootView = LayoutInflater.from(this).inflate(R.layout.myclient_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "消息列表", TOP_BTN_MODE.SHOWBACK,"","");
		findViewById(R.id.back_btn).setOnClickListener(this);


		listView = (ListView) rootView.findViewById(R.id.rotate_header_list_view);
        listView.setOnItemClickListener(null);
        
        mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(Global.mStrings));
		mAdapter = new MessageListAdapter(this, mListItems);
		
		listView.setAdapter(mAdapter);
		
		
		mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.rotate_header_list_view_frame);
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.setPtrHandler(new PtrHandler() {
		   @Override
		   public void onRefreshBegin(PtrFrameLayout frame) {
		        new GetDataTask().execute();
		   }
		
		   @Override
		   public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
		         return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
		   }
		});
		    // the following are default settings
		mPtrFrame.setResistance(1.7f);
		mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
		mPtrFrame.setDurationToClose(200);
		mPtrFrame.setDurationToCloseHeader(1000);
		// default is false
		mPtrFrame.setPullToRefresh(false);
		// default is true
		mPtrFrame.setKeepHeaderWhenRefresh(true);
		mPtrFrame.postDelayed(new Runnable() {
		    @Override
		    public void run() {
//		        mPtrFrame.autoRefresh();
		    }
		}, 100);
	        
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
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return Global.mStrings2;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mListItems.addFirst("Added after refresh...");
			mPtrFrame.refreshComplete();
            mAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}
	}
	
}
