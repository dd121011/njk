package com.njk.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.manager.UserManager;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MoreActivity extends BaseActivity implements OnClickListener{
	private String TAG = MoreActivity.class.getSimpleName();
	private RequestQueue mQueue;
	private Context context;
	private Button user_state_btn;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		context = this;
		mQueue = Volley.newRequestQueue(this);
		View rootView = LayoutInflater.from(this).inflate(R.layout.more_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "更多", TOP_BTN_MODE.SHOWBACK, "", "");

		findViewById(R.id.back_btn).setOnClickListener(this);
		user_state_btn = (Button)findViewById(R.id.user_state_btn);
		user_state_btn.setOnClickListener(this);

		UserManager.getInstance().registerUserStateChangerListener(listener);

		updateUI();
	}

	private void updateUI(){
		if(UserManager.getInstance().getUserLoginState(context)){
			user_state_btn.setVisibility(View.VISIBLE);
			user_state_btn.setText("退出登录");
		}else{
			user_state_btn.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UserManager.getInstance().unRegisterUserStateChangerListener(listener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.user_state_btn:
			startGetData();
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
		DialogUtil.progressDialogShow(this, this.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", Config.getUserId(context));

		RequestUtils.startStringRequest(Request.Method.POST, mQueue, RequestCommandEnum.ACCOUNT_LOGOUT, new RequestUtils.ResponseHandlerInterface() {

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				isStart = false;
				DialogUtil.progressDialogDismiss();
				try {
					if (!TextUtils.isEmpty(response)) {
						JSONObject obj = new JSONObject(response);
						if (obj.has("stacode")) {
							final String code = obj.getString("stacode");
							if ("1000".equals(code)) {
								String json2 = obj.getString("data");
								runOnUiThread(new Runnable() {
									public void run() {

										UserManager.getInstance().exitUser(context);
										Toast.makeText(context, code + " = 0", Toast.LENGTH_SHORT).show();
									}
								});
							} else {
								final String msg = obj.getString("message");
								runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
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

		}, params);

	}
	UserManager.OnChangerUserStateListener listener = new UserManager.OnChangerUserStateListener(){
		@Override
		public void onLoginSuccees() {

		}

		@Override
		public void onExitUser() {
			MoreActivity.this.finish();
		}
	};
}
