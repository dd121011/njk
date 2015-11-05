package com.njk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.njk.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public final class ShopDetailsNearFragment extends BaseFragment {
    private String TAG = "ShopDetailsNearFragment";
	public static final String ARGUMENTS = "arguments";
	private View rootView;
	private WebView detail_web_content;

	private Activity context;
	private DisplayImageOptions options;

	private String  detailText = "";

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
			rootView = inflater.inflate(R.layout.shop_details_web, container, false);
			detail_web_content = (WebView)rootView.findViewById(R.id.detail_web_content);
			Bundle bundle = getArguments();
			if(bundle!=null){
				detailText = bundle.getString(ARGUMENTS);
				updateUI();
			}
		}

		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null)
		{
			parent.removeView(rootView);
		}
		return rootView;
	}
	private void updateUI(){
		detailText ="<html> \n"
				+"<head> \n"
				+"<style type=\"text/css\"> \n"
				+"body {font-style:normal; " +
				"font-family: \""+"DroidSansFallback"+"\"; " +
				"font-size: 14; " +
				"line-height: 1.5; " +
				"text=\"#00000000\"}\n"
				+"a {color:#8FB554; text-decoration:none;}\n"
				+"</style> \n"
				+"</head> \n"
				+"<body>"+detailText+"</body>\n"
				+"</html>";

		detail_web_content.setFocusable(false);
		detail_web_content.setFocusableInTouchMode(false);
		detail_web_content.getSettings().setLoadWithOverviewMode(true);
		detail_web_content.getSettings().setJavaScriptEnabled(true);
		detail_web_content.getSettings().setDefaultTextEncodingName("UTF-8");
		detail_web_content.loadDataWithBaseURL("", detailText,"text/html", "utf-8", "");
	}
}
