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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.view.ViewHolder;

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
	
	private EditText search_text;
	private View clear_btn;
	private ListView listView,listView2 ;

	private RequestQueue mQueue;
	private Activity activity;
	private List<NearBean> nearBeanList;
	private List<String> keys;
	private PtrClassicFrameLayout mPtrFrame;
	private NearListAdapter mAdapter;
	private KeyTextAdapter keysAdapter;

	private int offset = 0;
	private int per_page = 10;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
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
					if(mPtrFrame.getVisibility()==View.GONE){
						mPtrFrame.setVisibility(View.VISIBLE);
					}
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
					if(!TextUtils.isEmpty(search_text.getText().toString())){
						search();
						setKey(activity, search_text.getText().toString());
						return true;
					}
					return false;
				}
				return false;
			}
		});

		clear_btn = findViewById(R.id.clear_btn);
		clear_btn.setOnClickListener(this);

		listView = (ListView) rootView.findViewById(R.id.rotate_header_list_view);
		listView.setOnItemClickListener(new NearListOnItemClickListener());
		nearBeanList = new ArrayList<NearBean>();
		mAdapter = new NearListAdapter(activity, nearBeanList);
		listView.setAdapter(mAdapter);
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						//加载更多功能的代码
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				// 判断是否滚动到底部
				if (view.getLastVisiblePosition() == view.getCount() - 2) {
					//加载更多功能的代码
					startGetData();
				}
			}
		});


		mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.rotate_header_list_view_frame);
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				search();
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


		listView2 = (ListView) rootView.findViewById(R.id.rotate_header_list_view2);
		keys = getKeys(activity);
		if(keys == null){
			keys = new ArrayList<>();
		}
		keys.add("清除记录");
		keysAdapter = new KeyTextAdapter(activity, keys);
		listView2.setAdapter(keysAdapter);
		listView2.setOnItemClickListener(searchKeyOitemClick);

	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.clear_btn:
			if(!TextUtils.isEmpty(search_text.getText().toString())){
				search_text.setText("");
			}
			break;
		default:
			break;
		}
		
	}

	private void search(){
		offset = 0;
		startGetData();
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
		params.put("Token", Config.getUserToken(activity)+"");
		params.put("keyword", search_text.getText().toString());
		params.put("lat", Config.getCurLat(activity));
		params.put("lng", Config.getCurLng(activity));
//		params.put("city_id", Config.getCurrCityId(activity));

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
				clear_btn.setVisibility(View.VISIBLE);
//				wordsListView.pullData(s.toString(), LejuApplication.NEW_HOUSE);

			} else {
				clear_btn.setVisibility(View.GONE);
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

	private List<String> getKeys(Context context){
		String dataStr = Config.getSearchKeys(context);
		Gson gson = new Gson();
		List<String> listData = gson.fromJson(dataStr, new TypeToken<List<String>>() {
		}.getType());
		return listData;
	}

	private void setKey(Context context,String key){
		if(TextUtils.isEmpty(key)){
			return;
		}
		List<String> listData = getKeys(context);
		if(listData == null){
			listData = new ArrayList<>();
		}
		if(!listData.contains(key)){
			Gson gson = new Gson();
			listData.add(key);
			Config.setSearchKeys(context,gson.toJson(listData));
		}
	}

	private void clearKeys(Context context){
		Config.setSearchKeys(context, "");
		keys.clear();
		keysAdapter.notifyDataSetChanged();
	}


	class KeyTextAdapter extends BaseAdapter{
		List<String> listData;
		Context context;
		LayoutInflater inflater;
		public KeyTextAdapter(Context context,List<String> listData) {
			this.listData = listData;
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return listData==null?0:listData.size();
		}

		@Override
		public Object getItem(int position) {
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = inflater.inflate(R.layout.search_keys_item,null);
			}
			TextView search_key_text = ViewHolder.get(convertView, R.id.search_key_text);
			search_key_text.setText(listData.get(position));
			return convertView;
		}
	}

	AdapterView.OnItemClickListener searchKeyOitemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(parent.getAdapter().getCount()-1 == position){
				clearKeys(activity);
			}else{
				search();
			}
		}
	};

	class NearListOnItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			NearListAdapter adapter = (NearListAdapter)arg0.getAdapter();
			NearBean item = adapter.getItem(arg2);
			Intent intent = new Intent(activity,ShopDetailsActivity.class);
			intent.putExtra("id",item.id);
//			Intent intent = new Intent(activity,DetailWebViewActivity.class);
			activity.startActivity(intent);
		}

	}
}
