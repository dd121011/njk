package com.njk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.njk.Global;
import com.njk.R;
import com.njk.adapter.BarberDetailsFansAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.Arrays;
import java.util.LinkedList;

public class BarberFansFragment extends BaseFragment implements OnClickListener{
private static String TAG="BarberFansFragment";
	
	private View rootView;
	private GridView gridView;
	
	private Activity activity;
	private RequestQueue mQueue;	
    private LinkedList mListItems;    
    private DisplayImageOptions options;
    private BarberDetailsFansAdapter mAdapter;

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
		mQueue = Volley.newRequestQueue(activity);  
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.ic_stub)
		.showImageForEmptyUri(R.mipmap.ic_empty)
		.showImageOnFail(R.mipmap.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
				
		if(rootView == null){
			rootView = inflater.inflate(R.layout.barber_details_fans_layout, container, false);
			
			gridView = (GridView) rootView.findViewById(R.id.barber_fans_layout);
			mListItems = new LinkedList<String>();
			mListItems.addAll(Arrays.asList(Global.mStrings));
			mAdapter = new BarberDetailsFansAdapter(getActivity(), mListItems);
			gridView.setAdapter(mAdapter);
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
		switch (view.getId()) {
		case R.id.back_btn:
			
			break;

		default:
			break;
		}
		
	}	
	
}