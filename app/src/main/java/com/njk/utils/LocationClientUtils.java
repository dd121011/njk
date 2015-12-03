package com.njk.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;

import java.util.ArrayList;
import java.util.List;

public class LocationClientUtils {
	public Context context;
	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	public Vibrator mVibrator;

	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	
	private List<LocatonListener> locatonListeners = new ArrayList<LocatonListener>();	
	
	private static LocationClientUtils instance;
	private LocationClientUtils() {}
	public static LocationClientUtils getInstance(){
		if(instance == null){
			instance =new LocationClientUtils();
		}
		return instance;
	} 

	public void init(Context context){
		this.context = context;
		mLocationClient = new LocationClient(this.context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mVibrator =(Vibrator)this.context.getSystemService(Service.VIBRATOR_SERVICE);


		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
		int span=1000;
		try {
			span = 1000;
		} catch (Exception e) {
			// TODO: handle exception
		}
		option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);//可选，默认false,设置是否使用gps
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mLocationClient.setLocOption(option);

	}
	
	public void start(){
		if(mLocationClient.isStarted()){
			mLocationClient.stop();
		}
		mLocationClient.start();
	}
	
	public void stop(){
		mLocationClient.stop();
	}
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			Log.i("MApplication", sb.toString());

			stop();

			String cityName = location.getCity();
			if(!TextUtils.isEmpty(cityName)){
				if (cityName.contains("市")){
					cityName = cityName.substring(0,cityName.length()-1);
				}
			}
			Config.setLocationCity(context, cityName);
			Config.setCurLat(context, location.getLatitude() + "");
			Config.setCurLng(context,location.getLongitude()+"");
			Config.setCurAddr(context,location.getLocationDescribe());
			for(LocatonListener listener : locatonListeners){
				listener.onReceiveLocation(location);
			}

		}

	}
	
	public void addListenter(LocatonListener listener){
		if(listener!=null){
			locatonListeners.add(listener);
		}
	}
	
	public void removeListener(LocatonListener listener){
		if(listener!=null && locatonListeners.contains(listener)){
			locatonListeners.remove(listener);
		}
	}
	
	public interface LocatonListener{
		void onReceiveLocation(BDLocation location);
	}
}
