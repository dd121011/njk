package com.njk.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.BaseActivity;
import com.njk.Global;
import com.njk.R;
import com.njk.bean.FamilyDetailBean;
import com.njk.bean.NearBean;
import com.njk.bean.ReviewBean;
import com.njk.fragment.BaseFragment;
import com.njk.fragment.ShopDetailsComboFragment;
import com.njk.fragment.ShopDetailsInfoFragment;
import com.njk.fragment.ShopDetailsRemarkFragment;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;
import com.njk.view.CustomListView;
import com.njk.view.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShopDetailsActivity extends BaseActivity implements OnClickListener {
	private final String TAG = "ShopDetailsActivity";
	private  final static int UPATE_LAYOUT = 1;
	

	private View rootView,fav_do_btn,cancel_fav_btn,navigate_btn,call_btn,review_btn;
	private ImageView topImg,face_img;
	private RadioGroup swithRadio;
	private CustomListView list;
	private View shop_adress_layout;
	private TextView discount_num_text,remark_num_text,enjoy_num_text,shop_name_text,shop_adress_text;
	
	private Activity context;
	private RequestQueue mQueue;
	private FragmentManager fm;
	private List<Fragment> fragmentlist;
	private LinkedList<String> mListItems;

	private NearBean nearBean;
	private FamilyDetailBean detailBean;
	private DisplayImageOptions options;	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPATE_LAYOUT:
				updateUI();
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
		mQueue = Volley.newRequestQueue(context);  		
		fm = getSupportFragmentManager(); 
		
		rootView = LayoutInflater.from(context).inflate(R.layout.shop_details_layout, null);
		setContentView(rootView);

		Object obj = getIntent().getSerializableExtra("obj");
		if(obj!=null){
			nearBean = (NearBean)obj;
		}

		initView();
		initListener();
		initData();
	}

	private void initView() {
		Utils.showTopBtn(rootView, "店铺详情", TOP_BTN_MODE.SHOWBOTH, "", "");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);
		swithRadio = (RadioGroup) rootView.findViewById(R.id.swith_shop_details);	

		list = (CustomListView) findViewById(R.id.list_layout);
		shop_adress_layout = findViewById(R.id.shop_adress_layout);
		
		topImg = (ImageView) findViewById(R.id.shop_top_img);
		face_img = (ImageView) findViewById(R.id.face_img);

		fav_do_btn = findViewById(R.id.fav_do_btn);
		cancel_fav_btn = findViewById(R.id.cancel_fav_btn);
		navigate_btn = findViewById(R.id.navigate_btn);
		call_btn = findViewById(R.id.call_btn);
		review_btn = findViewById(R.id.review_btn);

		discount_num_text = (TextView)findViewById(R.id.discount_num_text);
		remark_num_text = (TextView)findViewById(R.id.remark_num_text);
		enjoy_num_text = (TextView)findViewById(R.id.enjoy_num_text);
		shop_name_text = (TextView)findViewById(R.id.shop_name_text);
		shop_adress_text = (TextView)findViewById(R.id.shop_adress_text);
	}
	
	private void initListener() {
		swithRadio.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
//		swithRadio.check(R.id.radio_btn1);

		shop_adress_layout.setOnClickListener(this);

		fav_do_btn.setOnClickListener(this);
		cancel_fav_btn.setOnClickListener(this);
		navigate_btn.setOnClickListener(this);
		call_btn.setOnClickListener(this);
		review_btn.setOnClickListener(this);
	}

	private void initData() { 		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.img_default_icon)
		.showImageForEmptyUri(R.mipmap.img_default_icon)
		.showImageOnFail(R.mipmap.img_default_icon)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
		
		int[] wh = Utils.getDisplayMetrics(context);
		topImg.getLayoutParams().width = wh[0];
		topImg.getLayoutParams().height = wh[0]*2/3;
		
		startGetData();
	}


	private void changeFragment(String mTag,Class mClass){  
		Fragment mFragment = fm.findFragmentByTag(mTag);
        FragmentTransaction ft = fm.beginTransaction();  
        if (mFragment == null) {
            mFragment = Fragment.instantiate(context, mClass.getName(), null);
            ft.add(R.id.fragment_content, mFragment, mTag);
            if(fragmentlist == null){
            	fragmentlist = new ArrayList<Fragment>();
            }
            fragmentlist.add(mFragment);
        } 
        
        for(Fragment f: fragmentlist){
        	if(!mFragment.equals(f)){
        		ft.hide(f);
        	}else{
        		ft.show(f); 
        	}
        }

		if(mFragment instanceof BaseFragment){
			BaseFragment m =(BaseFragment)mFragment;
			Bundle bundle = new Bundle();
			bundle.putSerializable("obj",detailBean);
			m.setArgumentsUpdateUI(bundle);
		}

		ft.commit();
    }  

	class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
//			Toast.makeText(context, "checkedId = "+checkedId, 1000).show();
			switch (checkedId) {
			case R.id.radio_btn1:
				changeFragment(checkedId+"",ShopDetailsComboFragment.class);
				break;
			case R.id.radio_btn2:
				changeFragment(checkedId+"",ShopDetailsRemarkFragment.class);
				break;
			case R.id.radio_btn3:
				changeFragment(checkedId+"",ShopDetailsInfoFragment.class);
				break;
			default:
				break;
			}
		}
		
	}
	
	private void updateUI(){
		if(detailBean!=null){
			swithRadio.check(R.id.radio_btn1);
			ImageLoader.getInstance().displayImage(Global.base_url+detailBean.getImg(), topImg, options);
			face_img.setImageResource(R.mipmap.face_test1);
			discount_num_text.setText(detailBean.getPer_capita());
			remark_num_text.setText(detailBean.getComment());
			enjoy_num_text.setText(detailBean.getView());
			shop_name_text.setText(detailBean.getTitle());
			shop_adress_text.setText(detailBean.getAddress());
			String[] textArra = {};
			if(!TextUtils.isEmpty(detailBean.getTag())){
				textArra = detailBean.getTag().split("、");
			}
			list.setAdapter(new TextAdapter(context, textArra));
		}
	}
	
	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		DialogUtil.progressDialogShow(context, context.getResources().getString(R.string.is_loading));
		isStart = true;
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(context)+"");
		params.put("family_id", nearBean.id);
		params.put("user_id", "");

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.FAMILY_DETAIL,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
//				 Log.d(TAG, response); 
				 isStart = false;
				 DialogUtil.progressDialogDismiss();
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code") && obj.getString("code").equals("0")){
							 String jsonArray = obj.getString("data");
							 Gson gson = new Gson();
							 detailBean = gson.fromJson(jsonArray, new TypeToken<FamilyDetailBean>(){}.getType());
							 Log.d(TAG, detailBean+""); 
							 handler.sendEmptyMessage(UPATE_LAYOUT);
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
			
		},params);

	}
	
	public void familyFavDo(){
		DialogUtil.progressDialogShow(context, context.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(context)+"");
		params.put("family_id", detailBean.getId());
		params.put("user_id", Config.getUserId(context));

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.FAMILY_FAV_DO,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response);
				 DialogUtil.progressDialogDismiss();
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code") && obj.getString("code").equals("0")){
							 String jsonArray = obj.getString("data");
							 Gson gson = new Gson();
							 runOnUiThread(new Runnable() {
								 @Override
								 public void run() {
									 Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
									 cancel_fav_btn.setVisibility(View.VISIBLE);
									 fav_do_btn.setVisibility(View.GONE);
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
			
		},params);

	}
	
	public void familyCancelFav(){
		DialogUtil.progressDialogShow(context, context.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(context)+"");
		params.put("family_id", detailBean.getId());
		params.put("user_id", Config.getUserId(context));

		RequestUtils.startStringRequest(Method.GET,mQueue, RequestCommandEnum.FAMILY_CANCEL_FAV,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response);
				 DialogUtil.progressDialogDismiss();
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code") && obj.getString("code").equals("0")){
							 String jsonArray = obj.getString("data");
							 Gson gson = new Gson();
							 runOnUiThread(new Runnable() {
								 @Override
								 public void run() {
									 Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show();
									 fav_do_btn.setVisibility(View.VISIBLE);
									 cancel_fav_btn.setVisibility(View.GONE);
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
			
		},params);

	}
	
	public void familyReviewDo(){
		DialogUtil.progressDialogShow(context, context.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(context)+"");
		params.put("family_id", "222");
		params.put("content", "点评点评");

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.FAMILY_REVIEW_DO,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response);
				 DialogUtil.progressDialogDismiss();
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code") && obj.getString("code").equals("0")){
							 String jsonArray = obj.getString("data");
							 Gson gson = new Gson();
//							 detailBean = gson.fromJson(jsonArray, new TypeToken<FamilyDetailBean>(){}.getType());
//							 Log.d(TAG, detailBean+""); 
//							 handler.sendEmptyMessage(UPATE_LAYOUT);
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
			
		},params);

	}
	public void familyReviewList(){
		DialogUtil.progressDialogShow(context, context.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(context)+"");
		params.put("family_id", "222");
		params.put("user_id", "");

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.FAMILY_REVIEW_LIST,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response);
				 DialogUtil.progressDialogDismiss();
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code") && obj.getString("code").equals("0")){
							 String jsonArray = obj.getString("data");
							 Gson gson = new Gson();
							 List<ReviewBean> reviewBeans = gson.fromJson(jsonArray, new TypeToken<List<ReviewBean>>(){}.getType());
							 Log.d(TAG, reviewBeans+""); 
//							 handler.sendEmptyMessage(UPATE_LAYOUT);
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
			
		},params);

	}
	
	String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi" };

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.shop_adress_layout:
			intent = new Intent(context,ShopRouteSearchActivity.class);
			context.startActivity(intent);
			break;
		case R.id.fav_do_btn:
			familyFavDo();
			break;
		case R.id.cancel_fav_btn:
			familyCancelFav();
			break;
		case R.id.navigate_btn:
			intent = new Intent(context,ShopRouteSearchActivity.class);
			context.startActivity(intent);
			break;
		case R.id.call_btn:
			if(detailBean == null){
				return;
			}
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + detailBean.getPhone()));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
//			familyReviewDo();
			break;
		case R.id.review_btn:
			if(detailBean == null){
				return;
			}
			intent = new Intent(context,ReviewDoActivity.class);
			intent.putExtra("obj",detailBean);
			context.startActivity(intent);
//			familyReviewList();
			break;
		default:
			break;
		}
		
	}

	class TextAdapter extends BaseAdapter{
		String[] texts = null;
		Activity context = null;
		public TextAdapter(Activity context, String[] textArra) {
			texts = textArra;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return texts.length;
		}

		@Override
		public Object getItem(int position) {

			return texts[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.near_item_text_layout, null);
			}
			TextView t = ViewHolder.get(convertView, R.id.item_text);
			t.setText(texts[position]);
			
			switch (position) {
			case 0:
				t.setTextColor(context.getResources().getColor(R.color.angling_text_color));
				t.setBackgroundResource(R.color.angling_text_bg_color);
				break;
			case 1:
				t.setTextColor(context.getResources().getColor(R.color.pick_text_color));
				t.setBackgroundResource(R.color.pick_text_bg_color);		
				break;
			case 2:
				t.setTextColor(context.getResources().getColor(R.color.angling2_text_color));
				t.setBackgroundResource(R.color.angling2_text_bg_color);
				break;
			case 3:
				t.setTextColor(context.getResources().getColor(R.color.angling_text_color));
				t.setBackgroundResource(R.color.angling_text_bg_color);
				break;

			default:
				break;
			}
			
			return convertView;
		}
		
	}
}
