package com.njk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.R;
import com.njk.activity.EventListActivity;
import com.njk.activity.VipCardListActivity;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

public class DiscoverFragmentPage extends BaseFragment implements OnClickListener{
	private static String TAG="DiscoverFragmentPage";
	
	private View rootView;
	
	private Activity activity;
	private RequestQueue mQueue;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
		mQueue = Volley.newRequestQueue(activity);  
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
				
		if(rootView == null){
			rootView = inflater.inflate(R.layout.discover_fragment_layout, container, false);
			Utils.showTopBtn(rootView, "发现", TOP_BTN_MODE.SHOWTEXT,"","");
			
			rootView.findViewById(R.id.event_btn).setOnClickListener(this);
			rootView.findViewById(R.id.cheap_btn).setOnClickListener(this);
			rootView.findViewById(R.id.vip_btn).setOnClickListener(this);
			rootView.findViewById(R.id.health_btn).setOnClickListener(this);
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
		
		return rootView;		
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.back_btn:
			
			break;
		case R.id.event_btn:
			intent = new Intent(activity,EventListActivity.class);
			break;
		case R.id.cheap_btn:
			intent = new Intent(activity,EventListActivity.class);			
			break;
		case R.id.vip_btn:
			intent = new Intent(activity,VipCardListActivity.class);	
			break;
		case R.id.health_btn:
			intent = new Intent(activity,EventListActivity.class);	
			break;
		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
		}
		
	}	
}