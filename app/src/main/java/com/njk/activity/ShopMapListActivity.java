package com.njk.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.BaseActivity;
import com.njk.Global;
import com.njk.R;
import com.njk.bean.NearMapBean;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Logger;
import com.njk.utils.Utils;
import com.njk.view.MapItemLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShopMapListActivity extends BaseActivity implements OnClickListener {
	private final String TAG = ShopMapListActivity.class.getSimpleName();
	public final static int UPDATE_DATA_LIST = 1;
	public final static int MORE_DATE_LIST = 2;
	public final static int UPATE_LIST_LAYOUT = 3;
	public final static int GET_DATE_FAIL = 100;

	// 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;
	private Marker longClickMarker;
	private OverlayOptions longClickOverlayOptions;
	private LatLng currLatLng;
	private List<Marker> markerList;
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdN = BitmapDescriptorFactory
			.fromResource(R.mipmap.icon_markd);
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.mipmap.map_tag01);
	BitmapDescriptor bdB = BitmapDescriptorFactory
			.fromResource(R.mipmap.map_tag02);
	BitmapDescriptor bdC = BitmapDescriptorFactory
			.fromResource(R.mipmap.map_tag03);
	BitmapDescriptor bdD = BitmapDescriptorFactory
			.fromResource(R.mipmap.map_tag04);
	BitmapDescriptor bdE = BitmapDescriptorFactory
			.fromResource(R.mipmap.map_tag05);
	BitmapDescriptor bdF = BitmapDescriptorFactory
			.fromResource(R.mipmap.map_tag06);
	BitmapDescriptor bdG = BitmapDescriptorFactory
			.fromResource(R.mipmap.map_tag07);

	private List<NearMapBean> nearBeanList;

	boolean isFirstLoc = true;// 是否首次定位

	private DisplayImageOptions options;
	private RequestQueue mQueue;
	private Activity context;

	private String lng ="";
	private String lat="";
	private int offset = 0;
	private int per_page = 10;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_DATA_LIST:
					List<NearMapBean> dataList = (List<NearMapBean>) msg.getData().getSerializable("data");
					if(offset == 0){
						nearBeanList.clear();
					}
					offset = per_page;
					nearBeanList.addAll(dataList);
					handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					break;
				case MORE_DATE_LIST:
					offset += per_page;
					List<NearMapBean> moreList = (List<NearMapBean>) msg.getData().getSerializable("data");
					nearBeanList.addAll(moreList);
					handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
					break;
				case UPATE_LIST_LAYOUT:
					if(longClickOverlayOptions!=null){
						resetOverlay(mMapView);
					}else{
						initOverlay2();
					}

					break;
				case GET_DATE_FAIL:

					break;
				default:
					break;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(context);
		View rootView = LayoutInflater.from(context).inflate(R.layout.map_list_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "附近", Utils.TOP_BTN_MODE.SHOWBACK, "", "");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);
		ViewGroup shareLayout = (ViewGroup) rootView.findViewById(R.id.share_btn);
		Button shareBtn = (Button) shareLayout.getChildAt(0);
		shareBtn.setText("列表");
		shareBtn.setBackgroundColor(Color.TRANSPARENT);

		findViewById(R.id.back_curlocal_btn).setOnClickListener(this);

		initView();

		initData();

		initMapView();
	}

	private void initView(){

	}

	private void initData(){
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.user_face_icon)
				.showImageForEmptyUri(R.mipmap.user_face_icon)
				.showImageOnFail(R.mipmap.user_face_icon)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new SimpleBitmapDisplayer())
				.build();

		nearBeanList = new ArrayList<NearMapBean>();

		markerList = new ArrayList<>();
	}

	private void initMapView() {
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		hideZoomView(mMapView);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

//		mCurrentMode = LocationMode.FOLLOWING; 跟随
//		mCurrentMode = LocationMode.COMPASS;  罗盘
		mCurrentMode = LocationMode.NORMAL;
		mCurrentMarker = null; //当前位置显示默认图标，// 修改为自定义markermCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));

		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		setOnMapLongCilickListener();

		setOnMarkerClickListener();


	}


	private void setOnMapLongCilickListener(){
		mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng latLng) {
				MarkerOptions markerOptions = new MarkerOptions().position(latLng).zIndex(9).draggable(true);
				markerOptions.animateType(MarkerOptions.MarkerAnimateType.none);
				BitmapDescriptor bdN = BitmapDescriptorFactory.fromResource(R.mipmap.zuobiao);
				longClickOverlayOptions= markerOptions.icon(bdN);

				addLongClickMarker();

				startGetMapData(latLng.latitude+"",latLng.longitude+"");
			}
		});
	}

	private void addLongClickMarker(){
		if(longClickMarker!=null){
			longClickMarker.remove();
		}
		if(longClickOverlayOptions != null){
			longClickMarker =(Marker)mBaiduMap.addOverlay(longClickOverlayOptions);
		}
	}

	private void removeLongClickMarker(){
		if(longClickMarker!=null){
			longClickMarker.remove();
		}

		if(longClickOverlayOptions != null){
			longClickOverlayOptions = null;
		}
	}


	private void setOnMarkerClickListener() {
		mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener(){
			@Override
			public void onTouch(MotionEvent motionEvent) {
				mBaiduMap.hideInfoWindow();
			}
		});
		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				Bundle bundle = marker.getExtraInfo();
				if (bundle != null) {
					final NearMapBean mapBean = (NearMapBean) bundle.getSerializable("bean");
					Logger.d(TAG, mapBean.toString());

					View view = new MapItemLayout(context);
					view.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							mBaiduMap.hideInfoWindow();
							Intent intent = new Intent(context, ShopDetailsActivity.class);
							intent.putExtra("id", mapBean.id);
							startActivity(intent);
						}
					});

					TextView title_text = (TextView) view.findViewById(R.id.title_text);
					title_text.setText(mapBean.title);
					TextView adress_text = (TextView) view.findViewById(R.id.adress_text);
					adress_text.setText(mapBean.address);
					TextView range_text = (TextView) view.findViewById(R.id.range_text);
					range_text.setText(mapBean.range + "km");
					TextView view_text = (TextView) view.findViewById(R.id.view_text);
					view_text.setText(mapBean.view);
					ImageView face_img = (ImageView) view.findViewById(R.id.face_img);
					ImageLoader.getInstance().displayImage(Global.base_url + mapBean.img, face_img, options);

					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(view, ll, -57);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
				return false;
			}
		});
	}


	public void initOverlay2(){
		if(longClickOverlayOptions != null){
			addLongClickMarker();
		}

		for (NearMapBean mapBean : nearBeanList){
//			LatLng llA = new LatLng(39.963175, 116.400244);
			double lat = Double.valueOf(mapBean.lat);
			double lng = Double.valueOf(mapBean.lng);
			LatLng llA = new LatLng(lat, lng);

			MarkerOptions markerOptions = new MarkerOptions().position(llA).zIndex(9).draggable(true);
			int category_id = Integer.valueOf(mapBean.category_id);
			BitmapDescriptor bd = bdN;
			switch (category_id){
				case 1:
					bd =bdA;
					break;
				case 2:
					bd = bdB;
					break;
				case 3:
					bd = bdC;
					break;
				case 4:
					bd = bdD;
					break;
				case 5:
					bd = bdE;
					break;
				case 6:
					bd = bdF;
					break;
				case 7:
					bd = bdG;
					break;
			}
			markerOptions.animateType(MarkerOptions.MarkerAnimateType.grow);
			OverlayOptions ooA = markerOptions.icon(bd);

			Marker marker =(Marker)mBaiduMap.addOverlay(ooA);
			Bundle bundle = new Bundle();
			bundle.putSerializable("bean",mapBean);
			marker.setExtraInfo(bundle);
			markerList.add(marker);
		}

		zoomToSpan();

	}

	public void zoomToSpan() {
		if(this.mBaiduMap != null) {
			if(this.markerList.size() > 0) {
				LatLngBounds.Builder var1 = new LatLngBounds.Builder();
				Iterator var2 = this.markerList.iterator();

				while(var2.hasNext()) {
					Overlay var3 = (Overlay)var2.next();
					if(var3 instanceof Marker) {
						var1.include(((Marker)var3).getPosition());
					}
				}

				this.mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(var1.build()));
			}

		}
	}

	private void backCurrLocal(){
		removeLongClickMarker();
		if(currLatLng != null){
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(currLatLng);
			mBaiduMap.animateMapStatus(u);
			startGetMapData(currLatLng.latitude + "", currLatLng.longitude + "");
		}else{
			if(mLocClient!=null){
				isFirstLoc = true;
				mLocClient.start();
			}
		}
	}

	/**
	 * 清除所有Overlay
	 *
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 *
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		initOverlay2();
	}

	/**
	 * 隐藏地图缩放控件
	 *
	 * @param mapView
	 */
	private void hideZoomView(MapView mapView) {
		// 隐藏缩放控件
		int childCount = mapView.getChildCount();
		View zoom = null;
		for (int i = 0; i < childCount; i++) {
			View child = mapView.getChildAt(i);
			if (child instanceof ZoomControls ||child instanceof ImageView) {
				zoom = child;
				break;
			}
		}
		zoom.setVisibility(View.GONE);

		//地图上比例尺
		mapView.showScaleControl(false);
		// 隐藏缩放控件
		mapView.showZoomControls(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_btn:
				this.finish();
				break;
			case R.id.share_btn:

				break;
			case R.id.back_curlocal_btn:
				backCurrLocal();
				break;
			default:
				break;
		}

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
							// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				currLatLng = new LatLng(location.getLatitude(),location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(currLatLng);
				mBaiduMap.animateMapStatus(u);
				startGetMapData(currLatLng.latitude + "", currLatLng.longitude + "");
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	public void startGetMapData(String lat, String lng){
		this.lat = lat;
		this.lng = lng;
		this.offset = 0;
		startGetData();
	}

	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		isStart = true;
		DialogUtil.progressDialogShow(context, context.getResources().getString(R.string.is_loading));
		Map<String, String> params = new HashMap<String, String>();
		params.put("Token", Config.getUserToken(context));
//		params.put("offset", offset+"");
//		params.put("per_page", per_page+"");
		params.put("latitude", lat);
		params.put("longitude", lng);
		params.put("range", 100000+"");

		RequestUtils.startStringRequest(Request.Method.GET, mQueue, RequestCommandEnum.FINDFAMILY_LISTLBS, new RequestUtils.ResponseHandlerInterface() {

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d(TAG, response);
				isStart = false;
				DialogUtil.progressDialogDismiss();
				try {
					if (!TextUtils.isEmpty(response)) {
						JSONObject obj = new JSONObject(response);
						if (obj.has("code") && obj.getString("code").equals("0")) {
							String jsonArray = obj.getString("data");
							Gson gson = new Gson();
							ArrayList<NearMapBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<NearMapBean>>() {
							}.getType());
							Bundle data = new Bundle();
							data.putSerializable("data", dataList);
							Message msg = null;
							if (offset == 0) {
								msg = handler.obtainMessage(UPDATE_DATA_LIST);
							} else {
								msg = handler.obtainMessage(MORE_DATE_LIST);
							}
							msg.setData(data);
							msg.sendToTarget();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					isStart = false;
					DialogUtil.progressDialogDismiss();
					handler.sendEmptyMessage(GET_DATE_FAIL);
				}

			}

			@Override
			public void handlerError(String error) {
				// TODO Auto-generated method stub
				Log.e(TAG, error);
				isStart = false;
				DialogUtil.progressDialogDismiss();
				handler.sendEmptyMessage(GET_DATE_FAIL);
			}

		}, params);

	}
}
