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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.bean.NearMapBean;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.LocationClientUtils;
import com.njk.utils.Logger;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;
import com.njk.view.MapItemLayout;

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

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;
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

	private List<OverlayOptions> overlayOptionses;
	private List<NearMapBean> nearBeanList;

	private RequestQueue mQueue;
	private Activity context;

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
					initOverlay2();

					break;
				case GET_DATE_FAIL:

					break;
				default:
					break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(context);
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.map_list_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "附近", TOP_BTN_MODE.SHOWBACK,"","");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);
		ViewGroup shareLayout = (ViewGroup) rootView
				.findViewById(R.id.share_btn);
		Button shareBtn = (Button) shareLayout.getChildAt(0);
		shareBtn.setText("列表");
		shareBtn.setBackgroundColor(Color.TRANSPARENT);

		nearBeanList = new ArrayList<NearMapBean>();

		initData();

		initItemView();

		initMap();
	}

	private void initData(){
		String lat = Config.getCurLat(context);
		String lng = Config.getCurLng(context);
		if(!TextUtils.isEmpty(lat) || !TextUtils.isEmpty(lng)){
			startGetData();
		}else{
			LocationClientUtils.getInstance().addListenter(locationListener);
			LocationClientUtils.getInstance().start();
		}
	}

	private void initItemView(){
		ViewGroup container = (ViewGroup) findViewById(R.id.infowindow_container);
	}

	private void initMap() {
		markerList = new ArrayList<>();
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				Bundle bundle = marker.getExtraInfo();
				if(bundle!=null){
					final NearMapBean mapBean = (NearMapBean)bundle.getSerializable("bean");
					Logger.d(TAG, mapBean.toString());

					View view = new MapItemLayout(context);
					view.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
//							marker.setIcon(bdC);
							mBaiduMap.hideInfoWindow();

							Intent intent = new Intent(context,ShopDetailsActivity.class);
							intent.putExtra("id",mapBean.id);
							startActivity(intent);
						}
					});

					TextView title_text = (TextView)view.findViewById(R.id.title_text);
					title_text.setText(mapBean.title);
					TextView adress_text = (TextView)view.findViewById(R.id.adress_text);
					adress_text.setText(mapBean.address);
					TextView range_text = (TextView)view.findViewById(R.id.range_text);
					range_text.setText(mapBean.range+"km");
					TextView view_text = (TextView)view.findViewById(R.id.view_text);
					view_text.setText(mapBean.view);

					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(view, ll, -57);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
				return false;
			}
		});
	}

	public void initOverlay2(){
		if(overlayOptionses==null){
			overlayOptionses = new ArrayList<>();
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

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		bdA.recycle();
		bdB.recycle();
		bdC.recycle();
		bdD.recycle();
		bdE.recycle();
		bdG.recycle();

		LocationClientUtils.getInstance().removeListener(locationListener);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.share_btn:

			break;
		default:
			break;
		}

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
		params.put("latitude", Config.getCurLat(context));
		params.put("longitude", Config.getCurLng(context));
		params.put("range", 50000+"");

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

	private LocationClientUtils.LocatonListener locationListener = new LocationClientUtils.LocatonListener() {

		@Override
		public void onReceiveLocation(final BDLocation location) {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (location != null) {
						startGetData();
					}
				}
			});
		}
	};

}
