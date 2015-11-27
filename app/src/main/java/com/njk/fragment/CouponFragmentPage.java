package com.njk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.R;
import com.njk.activity.CouponDetailActivity;
import com.njk.adapter.CouponListAdapter;
import com.njk.bean.CouponBean;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class CouponFragmentPage extends Fragment{
	private static String TAG="CouponFragmentPage";

	public final static int UPDATE_DATA_LIST = 1;
	public final static int MORE_DATE_LIST = 2;
	public final static int UPATE_LIST_LAYOUT = 3;
	public final static int GET_DATE_FAIL = 100;


	private View rootView;
	private PtrClassicFrameLayout mPtrFrame;
	private CouponListAdapter mAdapter;
	
	private ViewGroup switch_near_btn,switch_hot_btn;

	private Activity activity;
	private RequestQueue mQueue;
	private List<CouponBean> couponBeanList;

	private int offset = 0;
	private int per_page = 10;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_DATA_LIST:
					List<CouponBean> dataList = (List<CouponBean>) msg.getData().getSerializable("data");
					if(offset == 0){
						couponBeanList.clear();
					}
					offset = per_page;
					couponBeanList.addAll(dataList);
					handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					break;
				case MORE_DATE_LIST:
					offset += per_page;
					List<CouponBean> moreList = (List<CouponBean>) msg.getData().getSerializable("data");
					couponBeanList.addAll(moreList);
					handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					break;
				case UPATE_LIST_LAYOUT:
					mPtrFrame.refreshComplete();
					mAdapter.notifyDataSetChanged();
					break;
				case GET_DATE_FAIL:
					mPtrFrame.refreshComplete();
					break;
				default:
					break;
			}
		}

	};

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
			rootView = inflater.inflate(R.layout.coupon_fragment_layout, container, false);

			Utils.showTopBtn(rootView, "优惠券", TOP_BTN_MODE.SHOWTEXT,"","");
			
	        ListView listView = (ListView) rootView.findViewById(R.id.rotate_header_list_view);

			couponBeanList = new ArrayList<CouponBean>();
			mAdapter = new CouponListAdapter(getActivity(), couponBeanList);
			
			listView.setAdapter(mAdapter);
			listView.setOnScrollListener(new AbsListView.OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
									 int visibleItemCount, int totalItemCount) {
					if (totalItemCount>visibleItemCount && view.getLastVisiblePosition() == view.getCount() - 2) {
						startGetData();
					}

				}
			});
			listView.setOnItemClickListener(itemClickListener);
		   mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.rotate_header_list_view_frame);
			mPtrFrame.setLastUpdateTimeRelateObject(this);
			mPtrFrame.setPtrHandler(new PtrHandler() {
				@Override
				public void onRefreshBegin(PtrFrameLayout frame) {
					offset = 0;
					startGetData();
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


			startGetData();
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
		
		return rootView;		
	}
	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		DialogUtil.progressDialogShow(activity, activity.getResources().getString(R.string.is_loading));
		isStart = true;
		Map<String, String> params = new HashMap<String, String>();
		params.put("Token", Config.getUserToken(activity)+"");
		params.put("keywords", "");
		params.put("offset", offset+"");
		params.put("per_page", per_page+"");

		RequestUtils.startStringRequest(Method.GET,mQueue, RequestCommandEnum.COUPON_INDEX,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response);
				isStart = false;
				DialogUtil.progressDialogDismiss();
				try {
					if(!TextUtils.isEmpty(response)){
						JSONObject obj = new JSONObject(response);
						if(obj.has("stacode") && obj.getString("stacode").equals("1000")){
							String jsonArray = obj.getString("data");
							Gson gson = new Gson();
							ArrayList<CouponBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<CouponBean>>(){}.getType());
							Bundle data = new Bundle();
							data.putSerializable("data", dataList);
							Message msg = null;
							if(offset == 0){
								msg = handler.obtainMessage(UPDATE_DATA_LIST);
							}else{
								msg = handler.obtainMessage(MORE_DATE_LIST);
							}
							msg.setData(data);
							msg.sendToTarget();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					isStart = false;
					DialogUtil.progressDialogDismiss();
					handler.sendEmptyMessage(GET_DATE_FAIL);
				}
			}

			@Override
			public void handlerError(String error) {
				// TODO Auto-generated method stub
				Log.e(TAG, error);  
			}
			
		},params);

	}

	AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			CouponBean bean = (CouponBean)parent.getAdapter().getItem(position);
			Intent intent = new Intent(activity, CouponDetailActivity.class);
			intent.putExtra("obj",bean.id);
			activity.startActivity(intent);
		}
	};
}