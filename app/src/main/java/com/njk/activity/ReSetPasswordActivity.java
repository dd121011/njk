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
import com.njk.MApplication;
import com.njk.R;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Password;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReSetPasswordActivity extends BaseActivity implements OnClickListener{
	private final String TAG = "SetPasswordActivity";
	private EditText phone_text,affirm_new_psd;
	private Context context;
	private RequestQueue mQueue; 
	private String verify_code,user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(this);
		View rootView = LayoutInflater.from(this).inflate(R.layout.set_password_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "设置密码", TOP_BTN_MODE.SHOWBACK,"","");
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.finish_btn).setOnClickListener(this);
		
		phone_text = (EditText)findViewById(R.id.phone_text);
		affirm_new_psd = (EditText)findViewById(R.id.affirm_new_psd);
		
		Intent intent = getIntent();
		verify_code = intent.getStringExtra("verify_code");
		phone_text.setText(intent.getStringExtra("mobile"));

		user_id = intent.getStringExtra("user_id");
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.finish_btn:
			if(!TextUtils.isEmpty(phone_text.getText().toString()) && !TextUtils.isEmpty(affirm_new_psd.getText().toString()) && !TextUtils.isEmpty(verify_code)){
				startGetData();
			}
			break;	
		default:
			break;
		}
		
	}
	
	private void toLoginActivity(){
//		Intent intent = new Intent(this, SelectUserTypeActivity.class);
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		MApplication app = (MApplication) getApplication();
		app.addLoginAcitivity(this);
	}

	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		isStart = true;
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("mobile", phone_text.getText().toString());
		params.put("code", verify_code+"");
		params.put("password", Password.createPassword(affirm_new_psd.getText().toString()));
		params.put("user_id", user_id+"");

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.ACCOUNT_UPDATE_PASSWORD,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response); 
				 isStart = false;
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("stacode")){
							String code = obj.getString("stacode");
							 if("1000".equals(code)){
								 runOnUiThread(new Runnable() {
									 public void run() {
										 toLoginActivity();
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
