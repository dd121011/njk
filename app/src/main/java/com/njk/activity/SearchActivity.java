package com.njk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.adapter.NearListAdapter;
import com.njk.bean.NearBean;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.DialogUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class SearchActivity extends BaseActivity implements OnClickListener{
	private final String  TAG = "SearchActivity";
	public final static int UPDATE_DATA_LIST = 1;
	public final static int MORE_DATE_LIST = 2;
	public final static int UPATE_LIST_LAYOUT = 3;
	public final static int GET_DATE_FAIL = 100;

	private final static int GET_CITY_DATA_SUCCES = 4;
	
	private EditText search_text;
	private View clear_btn;
	private ListView listView ;

	private RequestQueue mQueue;
	private Activity activity;
	private List<NearBean> nearBeanList;
	private PtrClassicFrameLayout mPtrFrame;
	private NearListAdapter mAdapter;

	private int offset = 0;
	private int per_page = 10;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GET_CITY_DATA_SUCCES:

					break;
				case UPDATE_DATA_LIST:
					List<NearBean> dataList = (List<NearBean>) msg.getData().getSerializable("data");
					if(offset == 0){
						nearBeanList.clear();
					}
					offset = per_page;
					nearBeanList.addAll(dataList);
					handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					break;
				case MORE_DATE_LIST:
					offset += per_page;
					List<NearBean> moreList = (List<NearBean>) msg.getData().getSerializable("data");
					nearBeanList.addAll(moreList);
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
		activity = this;
		mQueue = Volley.newRequestQueue(this);
		View rootView = LayoutInflater.from(this).inflate(R.layout.search_layout, null);
		setContentView(rootView);

		findViewById(R.id.back_btn).setOnClickListener(this);
		search_text = (EditText)findViewById(R.id.search_text);
		search_text.addTextChangedListener(textWatcher);
		search_text.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				//监听搜索输入框回车按键
				if (keyCode == KeyEvent.KEYCODE_ENTER && v.getId() == R.id.search_text) {//判断是否为回车
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm.isActive())
						imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
								InputMethodManager.HIDE_NOT_ALWAYS);
//					search();
					return true;
				}
				return false;
			}
		});

		clear_btn = findViewById(R.id.clear_btn);
		clear_btn.setOnClickListener(this);

		listView = (ListView) rootView.findViewById(R.id.rotate_header_list_view);
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
		mPtrFrame.setPullToRefresh(true);
		// default is true
		mPtrFrame.setKeepHeaderWhenRefresh(false);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.clear_btn:

			break;
		default:
			break;
		}
		
	}

	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		isStart = true;
		DialogUtil.progressDialogShow(activity, activity.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>();
		params.put("offset", offset+"");
		params.put("per_page", per_page+"");
//		params.put("Token", Config.getUserToken(activity)+"");
//		params.put("keyword", "");
//		params.put("lat", Config.getCurLat(activity));
//		params.put("lng", Config.getCurLng(activity));
//		params.put("city_id", Config.getCurrCityId(activity));
//		params.put("scenic_id", "scenic_id");
//		params.put("orderby", "orderby");
//		Token	Token值
//		offset	偏移量(0,10,20)默认0
//		per_page	每页显示数(10)
//		source	1web 2 android 3 ios
//		keyword	搜索农家客关键字
//		lat	经度
//		lng	维度
//		city_id	城市id
//		scenic_id	景区id
//		orderby	排序(1:距离最近2:人气最高3:点评最多4:人均最低5:人均最高)

		RequestUtils.startStringRequest(Method.GET,mQueue, RequestCommandEnum.FAMILY_LIST,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				isStart = false;
				DialogUtil.progressDialogDismiss();
				try {
					if(!TextUtils.isEmpty(response)){
						JSONObject obj = new JSONObject(response);
						if(obj.has("code") && obj.getString("code").equals("0")){
							String jsonArray = obj.getString("data");
							Gson gson = new Gson();
							ArrayList<NearBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<NearBean>>(){}.getType());
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
				isStart = false;
				DialogUtil.progressDialogDismiss();
				handler.sendEmptyMessage(GET_DATE_FAIL);
			}

		},params);

	}

	/**
	 * 监听关键字eidttextview中文本的变化，当发生变化时，请求数据。
	 */
	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (!TextUtils.isEmpty(s)) {
//				wordsListView.pullData(s.toString(), LejuApplication.NEW_HOUSE);

			} else {

			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

}
