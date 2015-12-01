package com.njk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.njk.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public final class ShopDetailsInfoFragment extends BaseFragment {
    private String TAG = "ShopDetailsInfoFragment";

    private View rootView;
    
    private Activity context;
    private DisplayImageOptions options;	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getActivity();
        
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(rootView == null){
			rootView = inflater.inflate(R.layout.shop_details_info, container, false);

		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
        return rootView;
    }

}
