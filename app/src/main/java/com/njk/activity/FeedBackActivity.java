package com.njk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends BaseActivity implements OnClickListener{
	private final String  TAG = "FeedBackActivity";

	private RequestQueue mQueue;
	private Activity context;

	private EditText content_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(this);
		View rootView = LayoutInflater.from(this).inflate(R.layout.feed_back_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "意见反馈", TOP_BTN_MODE.SHOWRIGHTTEXT, "", "发表");
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.share_btn).setOnClickListener(this);

		content_text = (EditText)findViewById(R.id.content_text);

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.back_btn:
				this.finish();
				break;
			case R.id.share_btn:
				if(!TextUtils.isEmpty(content_text.getText())){
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
		DialogUtil.progressDialogShow(context,"正在发送...");
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", Config.getUserId(context));
		params.put("content",content_text.getText().toString() );

		RequestUtils.startStringRequest(Request.Method.POST, mQueue, RequestCommandEnum.USERINFO_FEEDBACK, new RequestUtils.ResponseHandlerInterface() {

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
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										FeedBackActivity.this.finish();
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
				DialogUtil.progressDialogDismiss();
			}

		}, params);

	}
}
