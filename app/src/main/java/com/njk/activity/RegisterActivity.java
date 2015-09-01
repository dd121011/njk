package com.njk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.BaseActivity;
import com.njk.MApplication;
import com.njk.R;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "RegisterActivity";
	private static final int GET_VERIFY_CODE = 1;

	private TextView getVerifyCodeBtn;
	private EditText phone_text,verify_code_text;

	private int timeMax = 6;
	private int offsetTime = timeMax;

	Context context;
	RequestQueue mQueue;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_VERIFY_CODE:
				if (offsetTime > 0) {
					getVerifyCodeBtn.setText((offsetTime--) + "s");
					handler.sendEmptyMessageDelayed(GET_VERIFY_CODE, 1000);
				} else {
					getVerifyCodeBtn.setText("获取验证码");
					offsetTime = timeMax;
				}
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

		View rootView = LayoutInflater.from(this).inflate(R.layout.register_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "注册", TOP_BTN_MODE.SHOWBACK, "", "");
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.next_btn).setOnClickListener(this);

		phone_text = (EditText) findViewById(R.id.phone_text);
		verify_code_text = (EditText) findViewById(R.id.verify_code_text);

		getVerifyCodeBtn = (TextView) findViewById(R.id.get_verify_code);
		getVerifyCodeBtn.setOnClickListener(this);
		;
		mQueue = Volley.newRequestQueue(context);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.next_btn:
			String verify_code = verify_code_text.getText().toString();
			String phone = phone_text.getText().toString();
			if(!TextUtils.isEmpty(verify_code) && !TextUtils.isEmpty(phone)){
				intent = new Intent(context, SetPasswordActivity.class);
				intent.putExtra("mobile", phone);
				intent.putExtra("verify_code", verify_code);
				startActivity(intent);
				MApplication app = (MApplication) getApplication();
				app.addLoginAcitivity(this);
			}			
			break;
		case R.id.get_verify_code:
			String str = phone_text.getText().toString();
			if(!TextUtils.isEmpty(str)){
				if(offsetTime == timeMax ){
//					handler.sendEmptyMessageDelayed(GET_VERIFY_CODE, 1000);
					handler.sendEmptyMessage(GET_VERIFY_CODE);
					getVerifyCode(str);
				}
			}

			break;
		default:
			break;
		}
		
	}

	public void getVerifyCode(String num) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", num);

		RequestUtils.startStringRequest(Method.POST, mQueue, RequestCommandEnum.SEND_CODE_DO, new ResponseHandlerInterface() {

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				try {
					JSONObject obj = new JSONObject(response);
					if(obj.has("code")){
						String codeText = obj.getString("code");
						if("0".equals(codeText)){
							
						}else if("1003".equals(codeText)){
							final String msgText = obj.getString("message");
							runOnUiThread(new Runnable() {								
								@Override
								public void run() {
									Toast.makeText(context, msgText+"", Toast.LENGTH_SHORT).show();
								}
							});
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
			}

		}, params);

	}
}
