package com.njk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.BaseActivity;
import com.njk.Global;
import com.njk.R;
import com.njk.bean.CouponDetailBean;
import com.njk.manager.UserManager;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CouponDetailActivity extends BaseActivity implements View.OnClickListener {
	private final String  TAG = CouponDetailActivity.class.getSimpleName();
	public final static int UPDATE_DATA_LIST = 1;
	public final static int UPATE_LIST_LAYOUT = 3;
	public final static int GET_DATE_FAIL = 100;
	public final static int GET_COUPON_SUCCEES = 101;
	public final static int GET_COUPON_FAIL = 102;
	public final static int GET_DATA_FINISH = 1001;
	public final static int GET_DATA_START = 1000;
	private final static int getcoupon_do_btn_index = 9999;
	private DisplayImageOptions options;
	private RequestQueue mQueue; 
	private Activity activity;

	private TextView family_title_text,valid_date_text,content_text,money_text;
	private ImageView item_img;
	private Button get_coupon_layout_btn;

	private CouponDetailBean couponDetailBean;
	private String couponId="";
	private String user_id ="";

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_DATA_LIST:
					couponDetailBean = (CouponDetailBean) msg.getData().getSerializable("data");
					handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					break;
				case UPATE_LIST_LAYOUT:
					updateUI();
					break;
				case GET_DATE_FAIL:

					break;
				case GET_DATA_FINISH:
					isStart = false;
					DialogUtil.progressDialogDismiss();
					break;
				case GET_DATA_START:
					if(isStart){
						return;
					}
					DialogUtil.progressDialogShow(activity, activity.getResources().getString(R.string.is_loading));
					isStart = true;
					break;
				case GET_COUPON_SUCCEES:
//					CouponDetailActivity.this.finish();
					Toast.makeText(activity,"领取成功",Toast.LENGTH_SHORT).show();
					break;
				case GET_COUPON_FAIL:
					String msgStr = msg.getData().getString("data");
					Toast.makeText(activity,msgStr,Toast.LENGTH_SHORT).show();
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
		activity = this;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.img_default_icon)
				.showImageForEmptyUri(R.mipmap.img_default_icon)
				.showImageOnFail(R.mipmap.img_default_icon)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new SimpleBitmapDisplayer())
				.build();
		mQueue = Volley.newRequestQueue(this);
		View rootView = LayoutInflater.from(this).inflate(R.layout.coupon_detail_layout, null);
		setContentView(rootView);
		findViewById(R.id.back_btn).setOnClickListener(this);

		money_text = (TextView)findViewById(R.id.money_text);
		content_text = (TextView)findViewById(R.id.content_text);
		valid_date_text = (TextView)findViewById(R.id.valid_date_text);
		family_title_text = (TextView)findViewById(R.id.family_title_text);
		item_img = (ImageView)findViewById(R.id.item_img);
		get_coupon_layout_btn = (Button)findViewById(R.id.get_coupon_layout_btn);
		get_coupon_layout_btn.setOnClickListener(this);
		findViewById(R.id.next_detail_btn).setOnClickListener(this);

		couponId = getIntent().getStringExtra("obj");
		user_id = getIntent().getStringExtra("user_id");
		if(!TextUtils.isEmpty(user_id)){
			user_id = Config.getUserId(activity)+"";
		}else{
			user_id = "";
		}
		if(!TextUtils.isEmpty(couponId)){
			startGetData();
		}

		Utils.showTopBtn(rootView, "优惠券领取", TOP_BTN_MODE.SHOWBACK, "", "");
	}

	private void updateUI(){
		if(couponDetailBean != null){
			family_title_text.setText(couponDetailBean.family_title);
			valid_date_text.setText("有效期："+couponDetailBean.starttime+"至"+couponDetailBean.endtime);
			content_text.setText(couponDetailBean.content);
			money_text.setText(couponDetailBean.money+"元抵用券");
			ImageLoader.getInstance().displayImage(Global.base_url + couponDetailBean.family_img, item_img, options);

			if("1".equals(couponDetailBean.is_fav)){
				get_coupon_layout_btn.setClickable(false);
				get_coupon_layout_btn.setText("此券已领");
			}
		}
	}

	private boolean isStart = false;
	public void startGetData(){
//		if(isStart){
//			return;
//		}
		handler.sendEmptyMessage(GET_DATA_START);
		Map<String, String> params = new HashMap<String, String>();
		params.put("Token", Config.getUserToken(activity)+"");
		params.put("user_id",user_id);
		params.put("coupon_id", couponId+"");

		RequestUtils.startStringRequest(Request.Method.GET, mQueue, RequestCommandEnum.COUPON_DETAILS, new RequestUtils.ResponseHandlerInterface() {

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				handler.sendEmptyMessage(GET_DATA_FINISH);
				try {
					if (!TextUtils.isEmpty(response)) {
						JSONObject obj = new JSONObject(response);
						if (obj.has("stacode") && obj.getString("stacode").equals("1000")) {
							JSONObject jsonArray = obj.getJSONObject("data");
							Gson gson = new Gson();
							CouponDetailBean couponDetailBean = gson.fromJson(jsonArray.toString(), new TypeToken<CouponDetailBean>() {
							}.getType());
							Bundle data = new Bundle();
							data.putSerializable("data", couponDetailBean);
							Message msg = handler.obtainMessage(UPDATE_DATA_LIST);
							msg.setData(data);
							msg.sendToTarget();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(GET_DATA_FINISH);
				}
			}

			@Override
			public void handlerError(String error) {
				// TODO Auto-generated method stub
				Log.e(TAG, error);
				handler.sendEmptyMessage(GET_DATA_FINISH);
			}

		}, params);

	}

	public void getCoupon(){
		if(isStart){
			return;
		}
		DialogUtil.progressDialogShow(activity, activity.getResources().getString(R.string.is_loading));
		isStart = true;
		Map<String, String> params = new HashMap<String, String>();
		params.put("Token", Config.getUserToken(activity)+"");
		params.put("user_id", Config.getUserId(activity)+"");
		params.put("id", couponId+"");

		RequestUtils.startStringRequest(Request.Method.GET, mQueue, RequestCommandEnum.COUPONN_ADD, new RequestUtils.ResponseHandlerInterface() {

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				isStart = false;
				DialogUtil.progressDialogDismiss();
				try {
					if (!TextUtils.isEmpty(response)) {
						JSONObject obj = new JSONObject(response);
//						if (obj.has("stacode") && obj.getString("stacode").equals("1000")) {
//							JSONObject jsonArray = obj.getJSONObject("data");
//							Gson gson = new Gson();
//							CouponDetailBean couponDetailBean = gson.fromJson(jsonArray.toString(), new TypeToken<CouponDetailBean>() {
//							}.getType());
//							Bundle data = new Bundle();
//							data.putSerializable("data", couponDetailBean);
//							Message msg = handler.obtainMessage(UPDATE_DATA_LIST);
//							msg.setData(data);
//							msg.sendToTarget();
//						}

						String msgStr = obj.getString("message");
						Bundle data = new Bundle();
						Message msg = new Message();

						if(obj.has("code") && obj.getString("code").equals("0")){
							msg.what = GET_COUPON_SUCCEES;
						}else{
							msg.what = GET_COUPON_FAIL;
							data.putString("data",msgStr);
						}
						msg.setData(data);
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					isStart = false;
					DialogUtil.progressDialogDismiss();
					handler.sendEmptyMessage(GET_COUPON_FAIL);
				}
			}

			@Override
			public void handlerError(String error) {
				// TODO Auto-generated method stub
				Log.e(TAG, error);
			}

		}, params);

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case LoginActivity.LOGIN_SUCCESS:
				if(requestCode == getcoupon_do_btn_index){
					getCoupon();
				}
				break;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_btn:
				this.finish();
				break;
			case R.id.get_coupon_layout_btn:
				if(!UserManager.getInstance().getUserLoginState(activity)){
					Intent intent = new Intent(activity,LoginActivity.class);
					startActivityForResult(intent, getcoupon_do_btn_index);
				}else{
					getCoupon();
				}
				break;
			case R.id.next_detail_btn:
				if(couponDetailBean!=null){
					Intent intent = new Intent(activity,ShopDetailsActivity.class);
					intent.putExtra("id",couponDetailBean.family_id);
					activity.startActivity(intent);
				}
				break;
			default:
				break;
		}

	}
}
