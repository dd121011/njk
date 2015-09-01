package com.njk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.njk.BaseActivity;
import com.njk.MApplication;
import com.njk.R;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

public class SelectUserTypeActivity extends BaseActivity implements OnClickListener{
	
	private static final int GET_VERIFY_CODE = 1;
	
	private TextView getVerifyCodeBtn;
	
	private int timeMax = 6;
	private int offsetTime = timeMax;
	
	Context context;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_VERIFY_CODE:
				if(offsetTime>0){
					getVerifyCodeBtn.setText((offsetTime--)+"s");
					handler.sendEmptyMessageDelayed(GET_VERIFY_CODE, 1000);
				}else{
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
		View rootView = LayoutInflater.from(this).inflate(R.layout.select_user_type_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "云美客", TOP_BTN_MODE.SHOWBACK, "", "");
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.select_baber_user).setOnClickListener(this);
		findViewById(R.id.select_normal_user).setOnClickListener(this);
		     
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.select_baber_user:
			intent = new Intent(context, MainTabActivity.class);
			startActivity(intent);
			MApplication app = (MApplication) getApplication();
			app.addLoginAcitivity(this);
			break;
		case R.id.select_normal_user:
			intent = new Intent(context, MainTabActivity.class);
			startActivity(intent);
			MApplication app2 = (MApplication) getApplication();
			app2.addLoginAcitivity(this);
			break;
		default:
			break;
		}
		
	}

}
