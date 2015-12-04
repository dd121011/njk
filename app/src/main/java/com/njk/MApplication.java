package com.njk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.njk.utils.LocationClientUtils;
import com.njk.utils.UMengAnalysisUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

public class MApplication extends Application {

	
	public List<Activity> userLoginList;
	
	@Override
	public void onCreate() {

		super.onCreate();
		
		//百度地图 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		
		LocationClientUtils.getInstance().init(this);
		
		initImageLoader(getApplicationContext());

		UMengAnalysisUtils.openActivityDurationTrack(false);
	}


	
	
	public void addLoginAcitivity(Activity activity){
		if(userLoginList==null){
			userLoginList = new ArrayList<Activity>();
		}
		userLoginList.add(activity);
	}
	
	public void finishLoginActivity(){
		if(userLoginList == null){
			return;
		}
		for(Activity activity : userLoginList){
			if(activity!=null){
				activity.finish();				
			}
		}
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

}
