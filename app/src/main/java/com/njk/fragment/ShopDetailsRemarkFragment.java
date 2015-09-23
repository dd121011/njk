package com.njk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.R;
import com.njk.adapter.RemarkListAdapter;
import com.njk.bean.FamilyDetailBean;
import com.njk.bean.ObserverManager;
import com.njk.bean.ObserverManager.ReviewListener;
import com.njk.bean.ReviewBean;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.utils.Config;
import com.njk.utils.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ShopDetailsRemarkFragment extends BaseFragment {
    private String TAG = "ShopDetailsRemarkFragment";
	public final static int UPDATE_DATA_LIST = 1;
	public final static int MORE_DATE_LIST = 2;
	public final static int UPATE_LIST_LAYOUT = 3;
	public final static int GET_DATE_FAIL = 100;
	private final static int GET_CITY_DATA_SUCCES = 4;
	private final static int EMPTY_DATA_LIST = 5;

    private View rootView,empty_layout;
    private ListView listView;
	private List<ReviewBean> reviewBeanList;
	private FamilyDetailBean detailBean;;
    
    private Activity context;
	private RequestQueue mQueue;
    private DisplayImageOptions options;	
    private RemarkListAdapter mAdapter;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_DATA_LIST:
					List<ReviewBean> dataList = (List<ReviewBean>) msg.getData().getSerializable("data");
					if(dataList!=null && dataList.size()>0){
						reviewBeanList.clear();
						reviewBeanList.addAll(dataList);
						handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					}else{
						handler.sendEmptyMessage(EMPTY_DATA_LIST);
					}
					break;
				case MORE_DATE_LIST:

					handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					break;
				case UPATE_LIST_LAYOUT:

					mAdapter.notifyDataSetChanged();
					break;
				case GET_DATE_FAIL:

					break;
				case EMPTY_DATA_LIST:
					empty_layout.setVisibility(View.VISIBLE);
					break;
				default:
					break;
			}
		}

	};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getActivity();
		mQueue = Volley.newRequestQueue(context);

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.ic_stub)
		.showImageForEmptyUri(R.mipmap.ic_empty)
		.showImageOnFail(R.mipmap.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(rootView == null){

			rootView = inflater.inflate(R.layout.shop_details_remark, container, false);

			empty_layout = rootView.findViewById(R.id.empty_layout);
			listView = (ListView) rootView.findViewById(R.id.list_layout);
			reviewBeanList = new ArrayList<ReviewBean>();
			mAdapter = new RemarkListAdapter(getActivity(), reviewBeanList);
			listView.setAdapter(mAdapter);
			initObsever();
			load();
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
        return rootView;
    }

	private void initObsever(){
		ObserverManager.getInstance().addReviewListener(reviewListener);
	}

	private void removeObjserver(){
		ObserverManager.getInstance().removeReviewListener(reviewListener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		removeObjserver();
	}

	private void load(){
		if(detailBean==null){
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("Token", Config.getUserToken(context)+"");
		params.put("family_id", detailBean.getId());
		params.put("user_id","");
		RequestUtils.startStringRequest(Request.Method.GET,mQueue, RequestCommandEnum.FAMILY_REVIEW_LIST,new RequestUtils.ResponseHandlerInterface(){
			@Override
			public void handlerSuccess(String response) {
				try {
					if(!TextUtils.isEmpty(response)){
						JSONObject obj = new JSONObject(response);
						if(obj.has("code") && obj.getString("code").equals("0")){
							String total = obj.getString("total");
							Logger.d(TAG,"total = "+total);
							String jsonArray = obj.getString("data");
							Gson gson = new Gson();
							ArrayList<ReviewBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<ReviewBean>>(){}.getType());
							Bundle data = new Bundle();
							data.putSerializable("data", dataList);
							Message msg = msg = handler.obtainMessage(UPDATE_DATA_LIST);
							msg.setData(data);
							msg.sendToTarget();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(GET_DATE_FAIL);
				}
			}

			@Override
			public void handlerError(String error) {

			}
		},params);
	}

	@Override
	public void setArgumentsUpdateUI(Bundle bundle) {
		if(bundle!=null && bundle.getSerializable("obj")!=null){
			detailBean = (FamilyDetailBean)bundle.getSerializable("obj");
		}
	}

	ReviewListener reviewListener = new ReviewListener(){
		@Override
		public void notifyUpdateReview() {
			load();
		}
	};
}
