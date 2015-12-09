package com.njk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.adapter.MyCheapListAdapter;
import com.njk.bean.MyCheapBean;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Logger;
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

public class MyCheapActivity extends BaseActivity implements OnClickListener{
	private final String TAG = "MyCheapActivity";
	public final static int UPDATE_DATA_LIST = 1;
	public final static int MORE_DATE_LIST = 2;
	public final static int UPATE_LIST_LAYOUT = 3;
	public final static int GET_DATE_FAIL = 100;
	private ListView listView;

	private Activity context;
	private List<MyCheapBean> mListItems;
	private PtrClassicFrameLayout mPtrFrame;
	private MyCheapListAdapter mAdapter;
	private RequestQueue mQueue;

	private int offset = 0;
	private int per_page = 10;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_DATA_LIST:
					List<MyCheapBean> list = (List<MyCheapBean>) msg.getData().getSerializable("data");
					if(offset == 0){
						mListItems.clear();
					}
					offset = per_page;
					mListItems.addAll(list);
					handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					break;
				case MORE_DATE_LIST:
					offset += per_page;
					List<MyCheapBean> moreList = (List<MyCheapBean>) msg.getData().getSerializable("data");
					mListItems.addAll(moreList);
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(context);
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.mycheap_list_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "我的优惠", TOP_BTN_MODE.SHOWBACK, "", "");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);
		
		listView = (ListView) rootView
				.findViewById(R.id.rotate_header_list_view);
		listView.setOnItemClickListener(itemClickListener);

		mListItems = new ArrayList<MyCheapBean>();
		mAdapter = new MyCheapListAdapter(context, mListItems);
		listView.setAdapter(mAdapter);
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						//加载更多功能的代码
						startGetData();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				// 判断是否滚动到底部
				if (totalItemCount>visibleItemCount && view.getLastVisiblePosition() == view.getCount() - 2) {
					//加载更多功能的代码
//						startGetData();
				}
			}
		});
		mPtrFrame = (PtrClassicFrameLayout) rootView
				.findViewById(R.id.rotate_header_list_view_frame);
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				offset = 0;
				startGetData();
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

		startGetData();
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
			// TODO Auto-generated method stub
			MyCheapBean bean = (MyCheapBean)parent.getAdapter().getItem(position);
			Intent intent = new Intent(context, CouponDetailActivity.class);
			intent.putExtra("obj",bean.message_id);
			intent.putExtra("user_id",Config.getUserId(context)+"");
			context.startActivity(intent);
		}
		
	};

	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		DialogUtil.progressDialogShow(context, context.getResources().getString(R.string.is_loading));
		isStart = true;
		Map<String, String> params = new HashMap<String, String>();
		params.put("Token", Config.getUserToken(context)+"");
		params.put("user_id", Config.getUserId(context)+"");
//		params.put("offset", offset+"");
//		params.put("per_page", per_page+"");

		RequestUtils.startStringRequest(Request.Method.POST, mQueue, RequestCommandEnum.COUPON_MY_COUPON, new RequestUtils.ResponseHandlerInterface() {

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				isStart = false;
				DialogUtil.progressDialogDismiss();
				try {
					if (!TextUtils.isEmpty(response)) {
						JSONObject obj = new JSONObject(response);
						if (obj.has("stacode") && obj.getString("stacode").equals("1000")) {
							String jsonArray = obj.getString("data");
							Gson gson = new Gson();
							ArrayList<MyCheapBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<MyCheapBean>>() {
							}.getType());
							Logger.d(TAG, dataList + "");
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
						} else {
							handler.sendEmptyMessage(GET_DATE_FAIL);
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
				isStart = false;
				DialogUtil.progressDialogDismiss();
				handler.sendEmptyMessage(GET_DATE_FAIL);
			}

		}, params);

	}
}
