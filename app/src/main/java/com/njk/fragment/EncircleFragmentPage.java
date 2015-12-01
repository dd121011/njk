package com.njk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.njk.R;
import com.njk.activity.AddNjyActivity;

import java.util.ArrayList;
import java.util.List;

public class EncircleFragmentPage extends BaseFragment implements OnClickListener{

private static String TAG="EncircleFragmentPage";
	
	private View rootView;
	
	private ViewGroup switch_hot_btn,switch_local_btn;

	private Activity activity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(rootView == null){
			rootView = inflater.inflate(R.layout.encircle_fragment_layout, container, false);
			
			rootView.findViewById(R.id.share_btn).setOnClickListener(this);
			

			switch_hot_btn = (ViewGroup) rootView.findViewById(R.id.switch_hot_btn);
			switch_local_btn = (ViewGroup) rootView.findViewById(R.id.switch_local_btn);
			switch_hot_btn.setOnClickListener(new SwitchOnClickListener());
			switch_local_btn.setOnClickListener(new SwitchOnClickListener());
			
			changeFragment("Hot", EncircleHotFragmentPage.class);
			
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
		
		return rootView;		
	}	
	
	private List<Fragment> fragmentlist;
	private void changeFragment(String mTag,Class mClass){  
		FragmentManager fm = getChildFragmentManager();
		Fragment mFragment = fm.findFragmentByTag(mTag);
        FragmentTransaction ft = fm.beginTransaction();  
        if (mFragment == null) {
            mFragment = Fragment.instantiate(activity, mClass.getName(), null);
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
        
        ft.commit();
    } 
	
	
	private void handleSwitch() {
		final RadioButton hotRadio =(RadioButton) switch_hot_btn.getChildAt(0);
		final RadioButton localRadio  =(RadioButton) switch_local_btn.getChildAt(0);
		final View hotDot = switch_hot_btn.getChildAt(1);
		final View localDot = switch_local_btn.getChildAt(1);
		if(hotRadio.isChecked()){
			localRadio.setChecked(true);
			hotRadio.setChecked(false);
			localDot.setVisibility(View.VISIBLE);
			hotDot.setVisibility(View.INVISIBLE);
			changeFragment("Local", EncircleLocalFragmentPage.class);
		}else{
			localRadio.setChecked(false);
			hotRadio.setChecked(true);
			localDot.setVisibility(View.INVISIBLE);
			hotDot.setVisibility(View.VISIBLE);
			changeFragment("Hot", EncircleHotFragmentPage.class);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	class SwitchOnClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.switch_hot_btn:
				handleSwitch();
				break;
			case R.id.switch_local_btn:
				handleSwitch();
				break;
			default:
				break;
			}
			
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_btn:
			Intent intent = new Intent(activity, AddNjyActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}

}
