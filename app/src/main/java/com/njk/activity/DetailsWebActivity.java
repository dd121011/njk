package com.njk.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.bean.DetailInfoBean;
import com.njk.bean.FamilyDetailBean;
import com.njk.fragment.ShopDetailsWebFragment;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

public class DetailsWebActivity extends BaseActivity implements View.OnClickListener {
	private final String  TAG = "DetailsWebActivity";
	public static FragmentManager fm;
	private RequestQueue mQueue; 
	private Context context;

	private ShopDetailsWebFragment fragment;
	private DetailInfoBean infoBean;
	private FamilyDetailBean detailBean;
	private String content = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(this);
		View rootView = LayoutInflater.from(this).inflate(R.layout.details_web_layout, null);
		setContentView(rootView);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.share_btn).setOnClickListener(this);

		fm = getSupportFragmentManager();

		Object obj = getIntent().getSerializableExtra("obj");

		Object obj2 = getIntent().getSerializableExtra("detail");
		if(obj != null){

			infoBean = (DetailInfoBean)obj;
			if(obj2!=null){
				detailBean = (FamilyDetailBean)obj2;

				switch (infoBean.getType()){
					case INTRO:
						content = detailBean.getIntro();
						break;
					case FEATURE:
						content = detailBean.getFeature();
						break;
					case STAY:
						content = detailBean.getStay();
						break;
					case RECREATION:
						content = detailBean.getRecreation();
						break;
					case ROUTE:
						content = detailBean.getRoute();
						break;
					case SPECIAL:
						content = detailBean.getSpecial();
						break;
				}
			}


			fragment = new ShopDetailsWebFragment();
			Bundle bundle = new Bundle();
			bundle.putString(ShopDetailsWebFragment.ARGUMENTS,content);
			fragment.setArguments(bundle);

			// 只當容器，主要內容已Fragment呈現
			initFragment(fragment);
		}

		Utils.showTopBtn(rootView, infoBean.getTitle(), TOP_BTN_MODE.SHOWRIGHTTEXT, "", "");
	}

	// 初始化Fragment(FragmentActivity中呼叫)
	public static void initFragment(Fragment f){
		changeFragment(f, true);
	}
	private static void changeFragment(Fragment f, boolean init){
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fragment_container, f);
		if(!init)
			ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_btn:
				this.finish();
				break;

			default:
				break;
		}

	}
}
