package com.njk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.njk.BaseActivity;
import com.njk.Global;
import com.njk.R;
import com.njk.adapter.VipListAdapter;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import java.util.Arrays;
import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class VipCardListActivity extends BaseActivity implements
		OnClickListener {

	private ListView listView;

	private Activity context;
	private LinkedList<String> mListItems;
	private PtrClassicFrameLayout mPtrFrame;
	private VipListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.vip_list_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "我的会员卡", TOP_BTN_MODE.SHOWBACK,"","");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);
		
		listView = (ListView) rootView
				.findViewById(R.id.rotate_header_list_view);
		listView.setOnItemClickListener(itemClickListener);

		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(Global.mStrings3));
		mAdapter = new VipListAdapter(context, mListItems);
		listView.setAdapter(mAdapter);

		mPtrFrame = (PtrClassicFrameLayout) rootView
				.findViewById(R.id.rotate_header_list_view_frame);
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				new GetDataTask().execute();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
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
				// mPtrFrame.autoRefresh();
			}
		}, 100);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.share_btn:

			break;
		default:
			break;
		}

	}

	private OnItemClickListener itemClickListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(context,VipDetailsActivity.class);
			startActivity(intent);
		}
		
	};
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return Global.mStrings;
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
