package com.njk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.njk.activity.MainTabActivity;
import com.njk.adapter.GuideFragmentAdapter;
import com.njk.manager.UserManager;
import com.njk.utils.Config;
import com.njk.viewpager.CirclePageIndicator;
import com.njk.viewpager.PageIndicator;

public class WelcomActivity extends BaseActivity {
	ViewGroup welcom_layout, guide_layout;

	GuideFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	Button inButton;
	
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.welcom_layout);
		context = this;
		
		welcom_layout = (ViewGroup) findViewById(R.id.welcom_layout);
		guide_layout = (ViewGroup) findViewById(R.id.guide_layout);
		
		new Handler().postDelayed(new Runnable() {			
			@Override
			public void run() {
				if(Config.isHideGuided(context)){
					startNextActivity();
				}else{
					welcom_layout.setVisibility(View.GONE);
					guide_layout.setVisibility(View.VISIBLE );
				}

				
			}
		}, 2000);
		
		inButton = (Button) findViewById(R.id.come_in);
		inButton.setVisibility(View.GONE);
		
	       mAdapter = new GuideFragmentAdapter(getSupportFragmentManager());

	        mPager = (ViewPager)findViewById(R.id.pager);
	        mPager.setAdapter(mAdapter);
//
	        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
	        mIndicator.setViewPager(mPager);
	        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					if(arg0 == mPager.getAdapter().getCount()-1){
//						inButton.setVisibility(View.VISIBLE);
//						inButton.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View arg0) {
//								startNextActivity();
//							}
//				    	});
					}else{
						inButton.setVisibility(View.GONE);
					}
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}
			});

	}

	private void startNextActivity(){
		boolean isLogin = UserManager.getInstance().getUserLoginState(context);
//		if(isLogin){
			startMainTabActivity();
//		}else{
//			Intent intent = new Intent(context,LoginActivity.class);
//			intent.putExtra("ismain",true);
//			context.startActivity(intent);
//			Config.setHideGuided(context, true);
//			this.finish();
//		}
	}

	private void startMainTabActivity() {
//		Intent intent = new Intent(context,CategoryTestActivity.class);
		Intent intent = new Intent(context,MainTabActivity.class);
		context.startActivity(intent);
		Config.setHideGuided(context, true);
		this.finish();
	}
}
