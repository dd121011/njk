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
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.BaseActivity;
import com.njk.MApplication;
import com.njk.R;
import com.njk.manager.UserManager;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Password;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements OnClickListener{
	private final String  TAG = "LoginActivity";
	public static final int LOGIN_SUCCESS = 1;
	private RequestQueue mQueue; 
	private Context context;
	
	private EditText phone_text,password_text;

	boolean ismain = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(this);
		View rootView = LayoutInflater.from(this).inflate(R.layout.login_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "登录", TOP_BTN_MODE.SHOWRIGHTTEXT, "", "注册");
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.share_btn).setOnClickListener(this);
		findViewById(R.id.login_btn).setOnClickListener(this);
		
		findViewById(R.id.forget_password).setOnClickListener(this);
		
		password_text = (EditText)findViewById(R.id.password_text);
		phone_text = (EditText)findViewById(R.id.phone_text);

		ismain = getIntent().getBooleanExtra("ismain",false);
	        
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.share_btn:
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			MApplication app = (MApplication) getApplication();
			app.addLoginAcitivity(this);
			break;
		case R.id.forget_password:
	
			break;
		case R.id.login_btn:
			if(!TextUtils.isEmpty(phone_text.getText()) && !TextUtils.isEmpty(password_text.getText())){
				startGetData();
			}
			break;
		default:
			break;
		}
		
	}

	private void toMainAcitivity(){
		Intent intent = new Intent(this, MainTabActivity.class);
		startActivity(intent);
		MApplication app2 = (MApplication) getApplication();
		app2.addLoginAcitivity(this);
	}

	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		isStart = true;
		DialogUtil.progressDialogShow(this, this.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("mobile", phone_text.getText().toString());
		params.put("password", Password.createPassword(password_text.getText().toString()));
		
		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.LOGIN,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response);
				 isStart = false;
				DialogUtil.progressDialogDismiss();
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code")){
							final String code = obj.getString("code");
							 if("0".equals(code)){
								 JSONObject json2 = obj.getJSONObject("data");
								 Config.setUserId(context, json2.getString("user_id"));
								 Config.setUserToken(context, json2.getString("token"));
								 Config.setUserMobile(context,phone_text.getText().toString());
								 UserManager.getInstance().userLoginSuccees(context);
								 runOnUiThread(new Runnable() {
									public void run() {
										if(ismain){
											toMainAcitivity();
											Toast.makeText(context, code+" = 0", Toast.LENGTH_SHORT).show();
										}else{
											setResult(LOGIN_SUCCESS);
										}
										LoginActivity.this.finish();
									}
								 });
							 }else{
								 final String msg = obj.getString("message");
								 runOnUiThread(new Runnable() {
									 public void run() {
										 Toast.makeText(context, msg+"", Toast.LENGTH_SHORT).show();
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
				DialogUtil.progressDialogDismiss();
				isStart = false;
			}

		},params);

	}
}
