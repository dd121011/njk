package com.njk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.R;
import com.njk.activity.MessgeListActivity;
import com.njk.activity.MyClientActivity;

public class PersonalFragmentBarberPage extends Fragment{
	private static String TAG="NearFragmentPage";
	
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
			rootView = LayoutInflater.from(activity).inflate(R.layout.personal_fragment_barber_layout, container, false);
			
			rootView.findViewById(R.id.my_client_btn).setOnClickListener(clickListener);
			rootView.findViewById(R.id.my_works_btn).setOnClickListener(clickListener);
			rootView.findViewById(R.id.chat_record_btn).setOnClickListener(clickListener);
			
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
		
		return rootView;		
	}	
	
	private OnClickListener clickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.my_client_btn:
				intent = new Intent (activity, MyClientActivity.class);
				activity.startActivity(intent);
				break;
			case R.id.my_works_btn:
				intent = new Intent (activity, MessgeListActivity.class);
				activity.startActivity(intent);
				break;
			case R.id.chat_record_btn:
				intent = new Intent (activity, MessgeListActivity.class);
				activity.startActivity(intent);
				break;
			default:
				break;
			}
			
		}};
}