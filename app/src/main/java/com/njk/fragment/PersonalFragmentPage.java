package com.njk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.R;
import com.njk.activity.AddNjyActivity;
import com.njk.activity.FavoritesNjyActivity;
import com.njk.activity.MoreActivity;
import com.njk.activity.TravelNotesActivity;
import com.njk.bean.UserInfo;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonalFragmentPage extends Fragment implements OnClickListener{
	private static String TAG="PersonalFragmentPage";
	
	private View rootView;
	
	private Activity activity;
	private RequestQueue mQueue;
	
	private UserInfo userInfo;
		
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
			rootView = LayoutInflater.from(activity).inflate(R.layout.personal_fragment_layout, container, false);
			
			rootView.findViewById(R.id.face_layout).setOnClickListener(this);
			rootView.findViewById(R.id.nongjia_you).setOnClickListener(this);
			rootView.findViewById(R.id.favorites_nongjia).setOnClickListener(this);
			rootView.findViewById(R.id.add_nongjiayuan).setOnClickListener(this);
			rootView.findViewById(R.id.more_btn).setOnClickListener(this);
			
			getUserInfoIndex();
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
		
		return rootView;		
	}
	
	public void getUserInfoIndex(){
		DialogUtil.progressDialogShow(activity, activity.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(activity)+"");
		params.put("family_id", "222");
		params.put("user_id", "2");

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.USERINFO_INDEX,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response);
				 DialogUtil.progressDialogDismiss();
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("stacode") && obj.getString("stacode").equals("1000")){
							 String jsonArray = obj.getString("data");
							 Gson gson = new Gson();
							 userInfo = gson.fromJson(jsonArray, new TypeToken<UserInfo>(){}.getType());
							 Log.d(TAG, userInfo+""); 
//							 handler.sendEmptyMessage(UPATE_LAYOUT);
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
				DialogUtil.progressDialogDismiss();
			}
			
		},params);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.face_layout:
			
			break;
		case R.id.nongjia_you:
			intent = new Intent(activity,TravelNotesActivity.class);
			startActivity(intent);
			break;
		case R.id.favorites_nongjia:
			intent = new Intent(activity,FavoritesNjyActivity.class);
			startActivity(intent);
			break;
		case R.id.add_nongjiayuan:
			intent = new Intent(activity,AddNjyActivity.class);
			startActivity(intent);
			break;
		case R.id.more_btn:
			intent = new Intent(activity,MoreActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}	
}