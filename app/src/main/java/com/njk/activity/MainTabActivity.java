package com.njk.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.BaseActivity;
import com.njk.MApplication;
import com.njk.R;
import com.njk.bean.ProvinceBean;
import com.njk.db.ProvinceDBUtils;
import com.njk.fragment.EncircleFragmentPage;
import com.njk.fragment.NearFragmentPage;
import com.njk.fragment.PersonalFragmentPage;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.photo.util.Res;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.LocalDisplay;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author junbin
 *	功能描述：自定义TabHost
 */
public class MainTabActivity extends BaseActivity{	
	private static String TAG="MainTabActivity";
	
	//定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	
	//定义一个布局
	private LayoutInflater layoutInflater;
		
//	//定义数组来存放Fragment界面
//	private Class[] fragmentArray = {NearFragmentPage.class,DiscoverFragmentPage.class,EncircleFragmentPage.class,SubscribeFragmentPage.class,PersonalFragmentBarberPage.class};
//	//定义数组来存放按钮图片
//	private int[] mImageViewArray = {R.drawable.tab_near_btn,R.drawable.tab_discover_btn,R.drawable.tab_encircle_btn,
//									 R.drawable.tab_subscribe_btn,R.drawable.tab_user_btn};
//	//Tab选项卡的文字
//	private String[] mTextviewArray = {"附近", "发现", "炫美圈", "预约单", "我的"};
//	private Class[] fragmentArray = {NearFragmentPage.class,DiscoverFragmentPage.class,EncircleFragmentPage.class,PersonalFragmentPage.class};
	private Class[] fragmentArray = {NearFragmentPage.class,EncircleFragmentPage.class,PersonalFragmentPage.class};
//	private int[] mImageViewArray = {R.drawable.tab_near_btn,R.drawable.tab_discover_btn,R.drawable.tab_encircle_btn,R.drawable.tab_user_btn};
	private int[] mImageViewArray = {R.drawable.tab_near_btn,R.drawable.tab_encircle_btn,R.drawable.tab_user_btn};
//	private String[] mTextviewArray = {"附近", "发现", "晒农家", "我的"};
	private String[] mTextviewArray = {"附近", "优惠券", "我的"};
	private Activity activity;
	private RequestQueue mQueue;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);
        
        MApplication app = (MApplication) getApplication();
        app.finishLoginActivity();
               
        activity = this;
		mQueue = Volley.newRequestQueue(activity); 
        
        initData();
        
        initView();
    }
	 
	private void initData() {

		Res.init(this);       
		LocalDisplay.init(this);
		
		updateProvinceData();
	}

	/**
	 * 初始化组件
	 */
	private void initView(){
		//实例化布局对象
		layoutInflater = LayoutInflater.from(this);
				
		//实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//得到fragment的个数
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
//			//设置Tab按钮的背景
//			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_background);
		}
		
		
				
	}
				
	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
		
		return view;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				DialogUtil.appExitDialog(this);			
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	
	public void updateProvinceData() {
		Map<String, String> params = new HashMap<String, String>();
//		params.put("Token", "");
//		params.put("version", "");
		RequestUtils.startStringRequest(Method.POST, mQueue, RequestCommandEnum.APPINFOS_AREAS, new ResponseHandlerInterface() {

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				try {
					if (!TextUtils.isEmpty(response)) {
						JSONObject obj = new JSONObject(response);
						if (obj.has("code") && obj.getString("code").equals("0")) {
							JSONObject dataObj = obj.getJSONObject("data");
							String updateTime = dataObj.getString("datetime");
							if(TextUtils.isEmpty(updateTime)){
								//"2015-04-22"
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
								Date newTime = df.parse(updateTime);
								
								String oldStr = Config.getUpdateProvinceTime(activity);
								Date oldTime = df.parse(oldStr);
								
								if(newTime.getTime()>oldTime.getTime()){
									Config.setUpdateProvinceTime(activity, updateTime);
									
									String jsonArray = dataObj.getString("province");
									Gson gson = new Gson();
									ArrayList<ProvinceBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<ProvinceBean>>() {
									}.getType());									
									ProvinceDBUtils.saveProvinceToDb(activity,dataList);
									
//									ProvinceDBUtils.getProvince(activity,1);
//									CityDao dao = new CityDao(activity);
//									Logger.d(TAG, dao.listByUserId(1)+"");
//									User u = new User();
//									u.setName("张鸿洋");
//									new UserDao(activity).add(u);
//									new UserDao(activity).queryForAll();
									
//									Bundle data = new Bundle();
//									data.putSerializable("data", dataList);
//									Message msg = null;
//									msg = handler.obtainMessage(GET_CITY_DATA_SUCCES);
//									msg.setData(data);
//									msg.sendToTarget();
								}
								
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
			}

		}, params);

	}
}
