package com.njk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.njk.utils.UMengAnalysisUtils;
import com.umeng.message.PushAgent;

public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(isAnalysis){
			UMengAnalysisUtils.onResume(this, this.getClass().getSimpleName());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(isAnalysis){
			UMengAnalysisUtils.onPause(this, this.getClass().getSimpleName());
		}
	}

	protected boolean isAnalysis = true;//为true则进行统计，false则不进行统计
	protected void seTAnalysis(boolean isAnalysis){
		this.isAnalysis = isAnalysis;
	}
}
