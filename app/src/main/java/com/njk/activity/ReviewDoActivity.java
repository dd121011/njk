package com.njk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.bean.FamilyDetailBean;
import com.njk.bean.ObserverManager;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReviewDoActivity extends BaseActivity implements OnClickListener{
	private final String  TAG = "ReviewDoActivity";
	
	private RequestQueue mQueue; 
	private Context context;
	
	private EditText content_text;
	private FamilyDetailBean detailBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(this);
		View rootView = LayoutInflater.from(this).inflate(R.layout.review_do_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "发表评论", TOP_BTN_MODE.SHOWRIGHTTEXT, "", "发表");
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.share_btn).setOnClickListener(this);

		content_text = (EditText)findViewById(R.id.content_text);

		Object obj = getIntent().getSerializableExtra("obj");
		if(obj != null){
			detailBean = (FamilyDetailBean)obj;
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.share_btn:
			if(detailBean!=null){
				startGetData();
			}
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
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("family_id", detailBean.getId());
		params.put("content",content_text.getText().toString() );
		
		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.FAMILY_REVIEW_DO,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response);
				 isStart = false;
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code")){
							final String code = obj.getString("code");
							 if("0".equals(code)){
								 ObserverManager.getInstance().notifyReviewOchange();
								 runOnUiThread(new Runnable() {
									 @Override
									 public void run() {
										 ReviewDoActivity.this.finish();
									 }
								 });
							 }
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
			}

		},params);

	}
}
