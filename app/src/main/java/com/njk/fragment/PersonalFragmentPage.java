package com.njk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.R;
import com.njk.activity.FavoritesNjyActivity;
import com.njk.activity.LoginActivity;
import com.njk.activity.MoreActivity;
import com.njk.activity.MyCheapActivity;
import com.njk.bean.UserInfo;
import com.njk.manager.UserManager;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonalFragmentPage extends BaseFragment implements OnClickListener{
	private static String TAG="PersonalFragmentPage";
	
	private View rootView,info_layout;
	private ImageView face_img;
	private TextView personal_name;
	
	private Activity activity;
	private RequestQueue mQueue;
	
	private UserInfo userInfo;
	FeedbackAgent fb;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
		mQueue = Volley.newRequestQueue(activity);
		this.seTAnalysis(true);
		setUpUmengFeedback();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		if(rootView == null){
			rootView = LayoutInflater.from(activity).inflate(R.layout.personal_fragment_layout, container, false);
			
			rootView.findViewById(R.id.face_layout).setOnClickListener(this);
			rootView.findViewById(R.id.my_cheap_layout).setOnClickListener(this);
			rootView.findViewById(R.id.favorites_nongjia).setOnClickListener(this);
			rootView.findViewById(R.id.add_nongjiayuan).setOnClickListener(this);
			rootView.findViewById(R.id.more_btn).setOnClickListener(this);

			info_layout = rootView.findViewById(R.id.info_layout);
			info_layout.setOnClickListener(this);

			face_img = (ImageView)rootView.findViewById(R.id.face_img);
			personal_name = (TextView)rootView.findViewById(R.id.personal_name);

			initListener();

			getUserInfoIndex();

			updateUI();
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
		
		return rootView;		
	}

	private void updateUI(){
		if(userInfo!=null){

			if(TextUtils.isEmpty(userInfo.getNickname())){
				personal_name.setText(userInfo.getMobile());
			}else{
				personal_name.setText(userInfo.getNickname());
			}
		}else{
			personal_name.setText("登录/注册");
		}
	}

	private void initListener(){
		UserManager.getInstance().registerUserStateChangerListener(listener);
	}
	
	public void getUserInfoIndex(){
		DialogUtil.progressDialogShow(activity, activity.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(activity)+"");
		params.put("user_id", Config.getUserId(activity)+"");

		RequestUtils.startStringRequest(Method.POST, mQueue, RequestCommandEnum.USERINFO_INDEX, new ResponseHandlerInterface() {

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				DialogUtil.progressDialogDismiss();
				try {
					if (!TextUtils.isEmpty(response)) {
						JSONObject obj = new JSONObject(response);
						if (obj.has("stacode") && obj.getString("stacode").equals("1000")) {
							String jsonArray = obj.getString("data");
							Gson gson = new Gson();
							userInfo = gson.fromJson(jsonArray, new TypeToken<UserInfo>() {
							}.getType());
							Log.d(TAG, userInfo + "");
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									updateUI();
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
				DialogUtil.progressDialogDismiss();
			}

		}, params);
	}

	private void setUpUmengFeedback() {
		fb = new FeedbackAgent(activity);
		// check if the app developer has replied to the feedback or not.
		fb.sync();
		fb.openAudioFeedback();
		fb.openFeedbackPush();
		PushAgent.getInstance(activity).setDebugMode(true);
		PushAgent.getInstance(activity).enable();

		//fb.setWelcomeInfo();
		//  fb.setWelcomeInfo("Welcome to use umeng feedback app");
//        FeedbackPush.getInstance(this).init(true);
//        PushAgent.getInstance(this).setPushIntentServiceClass(MyPushIntentService.class);


		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = fb.updateUserInfo();
			}
		}).start();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.info_layout:
			if(!UserManager.getInstance().getUserLoginState(activity)){
				intent = new Intent(activity,LoginActivity.class);
				startActivityForResult(intent, Activity.RESULT_FIRST_USER);
			}
			break;
		case R.id.face_layout:
			
			break;
		case R.id.my_cheap_layout:
//			intent = new Intent(activity,TravelNotesActivity.class);
			intent = new Intent(activity,MyCheapActivity.class);
			startActivity(intent);
			break;
		case R.id.favorites_nongjia:
			intent = new Intent(activity,FavoritesNjyActivity.class);
			startActivity(intent);
			break;
		case R.id.add_nongjiayuan:
//			intent = new Intent(activity,AddNjyActivity.class);
//			startActivity(intent);
			fb.startFeedbackActivity();
			break;
		case R.id.more_btn:
			intent = new Intent(activity,MoreActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		UserManager.getInstance().unRegisterUserStateChangerListener(listener);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case LoginActivity.LOGIN_SUCCESS:
				getUserInfoIndex();
				break;
		}
	}

	UserManager.OnChangerUserStateListener listener = new UserManager.OnChangerUserStateListener(){
		@Override
		public void onLoginSuccees() {
			getUserInfoIndex();
		}

		@Override
		public void onExitUser() {
			userInfo = null;
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateUI();
				}
			});
		}
	};
}