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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.adapter.FavoritesListAdapter;
import com.njk.bean.FavoritesBean;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
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

public class FavoritesNjyActivity extends BaseActivity implements OnClickListener{
	private final String TAG = "FavoritesNjyActivity";
	public final static int UPDATE_DATA_LIST = 1;
	public final static int MORE_DATE_LIST = 2;
	public final static int UPATE_LIST_LAYOUT = 3;
	public final static int GET_DATE_FAIL = 100;

	
	private ListView listView;

	private Activity context;
	private List<FavoritesBean> dataList;
	private PtrClassicFrameLayout mPtrFrame;
	private FavoritesListAdapter mAdapter;

	private RequestQueue mQueue;
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_DATA_LIST:
				List<FavoritesBean> list = (List<FavoritesBean>) msg.getData().getSerializable("data");
				dataList.clear();
				dataList.addAll(list);
				handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
				break;
			case MORE_DATE_LIST:
				
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
				R.layout.favorites_list_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "收藏农家院", TOP_BTN_MODE.SHOWBACK, "", "");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);
		
		listView = (ListView) rootView
				.findViewById(R.id.rotate_header_list_view);
		listView.setOnItemClickListener(itemClickListener);

		dataList = new ArrayList<FavoritesBean>();
		mAdapter = new FavoritesListAdapter(context, dataList);
		listView.setAdapter(mAdapter);

		mPtrFrame = (PtrClassicFrameLayout) rootView
				.findViewById(R.id.rotate_header_list_view_frame);
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
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
			FavoritesListAdapter adapter = (FavoritesListAdapter)parent.getAdapter();
			FavoritesBean item = adapter.getItem(position);
			Intent intent = new Intent(context,ShopDetailsActivity.class);
			intent.putExtra("id",item.getMessage_id());
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

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.USERINFO_MY_FAV,new ResponseHandlerInterface(){

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
							 ArrayList<FavoritesBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<FavoritesBean>>(){}.getType());
							 Logger.d(TAG, dataList+"");
							 Bundle data = new Bundle();
							 data.putSerializable("data", dataList);
							 Message msg = handler.obtainMessage(UPDATE_DATA_LIST);							 
							 msg.setData(data);
							 msg.sendToTarget();
						 }else{
							 handler.sendEmptyMessage(GET_DATE_FAIL);
						 }
					 }
				} catch (Exception e) {
					e.printStackTrace();
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
			
		},params);

	}
}
