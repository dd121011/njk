package com.njk.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.R;
import com.njk.adapter.SubscribeListAdapter;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import java.util.Arrays;
import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class SubscribeFragmentPage extends Fragment{
	private static String TAG="NearFragmentPage";
	
	private View rootView;
	private LinkedList<String> mListItems;
	private PtrClassicFrameLayout mPtrFrame;
	private SubscribeListAdapter mAdapter;
	
	private ViewGroup switch_near_btn,switch_hot_btn;

	private Activity activity;
	private RequestQueue mQueue;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
		mQueue = Volley.newRequestQueue(activity);  
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
	
		if(rootView == null){
			rootView = inflater.inflate(R.layout.subscribe_fragment_layout, container, false);

			Utils.showTopBtn(rootView, "用户预约单", TOP_BTN_MODE.SHOWTEXT,"","");
			
	        ListView listView = (ListView) rootView.findViewById(R.id.rotate_header_list_view);

			mListItems = new LinkedList<String>();
			mListItems.addAll(Arrays.asList(mStrings));
			mAdapter = new SubscribeListAdapter(getActivity(), mListItems);
			
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
//		                mPtrFrame.autoRefresh();
		            }
		        }, 100);
			
			
	//		startGetData();
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
		
		return rootView;		
	}	
	
	public void startGetData(){
		
		
		
		RequestUtils.startStringRequest(Method.GET,mQueue, RequestCommandEnum.FAMILY_LIST,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response); 
				 mPtrFrame.refreshComplete();
                 mAdapter.notifyDataSetChanged();
			}

			@Override
			public void handlerError(String error) {
				// TODO Auto-generated method stub
				Log.e(TAG, error);  
			}
			
		},null);

	}
	
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mListItems.addFirst("Added after refresh...");
			mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			 mPtrFrame.refreshComplete();
             mAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}
	}
	
	
	private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };
}