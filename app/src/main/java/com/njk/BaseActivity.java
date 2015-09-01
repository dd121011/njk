package com.njk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.njk.R;

public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		overridePendingTransition(R.anim.push_right_in,R.anim.push_left_out);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
	}

	
}
