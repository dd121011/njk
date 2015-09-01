package com.njk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailWebViewActivity extends BaseActivity implements OnClickListener{
	private final String TAG ="WebViewActivity";
	private WebView webView;
	
	private Context context;
	private RequestQueue mQueue;
	@Override
	protected void onCreate(Bundle arg0) {		
		super.onCreate(arg0);
		context = this;
		mQueue = Volley.newRequestQueue(context);
		View rootView = LayoutInflater.from(context).inflate(R.layout.webview_activity_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "店铺详情", TOP_BTN_MODE.SHOWBOTH,"","");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);
		webView = (WebView) rootView.findViewById(R.id.webview);

		webView.setFocusable(false);
		webView.setFocusableInTouchMode(false);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("UTF-8");
		
		startGetData();
		
	}
	
	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		DialogUtil.progressDialogShow(this, this.getResources().getString(R.string.is_loading));
		isStart = true;
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(this)+"");
		params.put("id", "222");

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.FAMILY_INFO,new ResponseHandlerInterface(){

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
							 String detail = obj.getString("data");
//							 Gson gson = new Gson();
//							 ArrayList<NearBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<NearBean>>(){}.getType());
							 
							 
							 webView.loadDataWithBaseURL("", detail,"text/html", "utf-8", ""); 
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
			}
			
		},params);

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

}
