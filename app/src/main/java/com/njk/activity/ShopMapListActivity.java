package com.njk.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;
import com.njk.view.MapItemLayout;

import java.util.ArrayList;

public class ShopMapListActivity extends BaseActivity implements OnClickListener {

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private Marker mMarkerB;
	private Marker mMarkerC;
	private Marker mMarkerD;
	private InfoWindow mInfoWindow;
	
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.mipmap.shop_map_icon);
	BitmapDescriptor bdB = BitmapDescriptorFactory
			.fromResource(R.mipmap.user_map_icon);
	BitmapDescriptor bdC = BitmapDescriptorFactory
			.fromResource(R.mipmap.icon_markc);
	BitmapDescriptor bdD = BitmapDescriptorFactory
			.fromResource(R.mipmap.icon_markd);
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.mipmap.icon_gcoding);
	BitmapDescriptor bdGround = BitmapDescriptorFactory
			.fromResource(R.mipmap.ground_overlay);
	

	private Activity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
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
		
		initItemView();

		initMap();
	}
	
	private void initItemView(){
		ViewGroup container = (ViewGroup) findViewById(R.id.infowindow_container);
	}

	private void initMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
//				View view =LayoutInflater.from(context).inflate(R.layout.map_infowindow_layout2, null);
//				view.setLayoutParams(new LayoutParams(10, 10));
				View view = new MapItemLayout(context);

				OnInfoWindowClickListener listener = null;
				if (marker == mMarkerA || marker == mMarkerD) {
//					listener = new OnInfoWindowClickListener() {
//						public void onInfoWindowClick() {
//							LatLng ll = marker.getPosition();
//							LatLng llNew = new LatLng(ll.latitude + 0.005,
//									ll.longitude + 0.005);
//							marker.setPosition(llNew);
//							mBaiduMap.hideInfoWindow();
//						}
//					};
					view.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							LatLng ll = marker.getPosition();
							LatLng llNew = new LatLng(ll.latitude + 0.005,
									ll.longitude + 0.005);
							marker.setPosition(llNew);
							mBaiduMap.hideInfoWindow();
						}
					});
					LatLng ll = marker.getPosition();
//					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -47, listener);
					mInfoWindow = new InfoWindow(view, ll, -57);
					mBaiduMap.showInfoWindow(mInfoWindow);
				} else if (marker == mMarkerB) {
					view.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							marker.setIcon(bd);
							mBaiduMap.hideInfoWindow();
						}
					});
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(view, ll, -57);
					mBaiduMap.showInfoWindow(mInfoWindow);
				} else if (marker == mMarkerC) {
					view.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							marker.remove();
							mBaiduMap.hideInfoWindow();
						}
					});
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(view, ll, -57);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
				return true;
			}
		});
	}

	public void initOverlay() {
		// add marker overlay
		LatLng llA = new LatLng(39.963175, 116.400244);
		LatLng llB = new LatLng(39.942821, 116.369199);
		LatLng llC = new LatLng(39.939723, 116.425541);
		LatLng llD = new LatLng(39.906965, 116.401394);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdB)
				.zIndex(5);
		mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
		OverlayOptions ooC = new MarkerOptions().position(llC).icon(bdC)
				.perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
		mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(bdA);
		giflist.add(bdB);
		giflist.add(bdC);
		OverlayOptions ooD = new MarkerOptions().position(llD).icons(giflist)
				.zIndex(0).period(10);	
		mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

//		// add ground overlay
//		LatLng southwest = new LatLng(39.92235, 116.380338);
//		LatLng northeast = new LatLng(39.947246, 116.414977);
//		LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
//				.include(southwest).build();
//
//		OverlayOptions ooGround = new GroundOverlayOptions()
//				.positionFromBounds(bounds).image(bdGround).transparency(0.8f);
//		mBaiduMap.addOverlay(ooGround);
//
//		MapStatusUpdate u = MapStatusUpdateFactory
//				.newLatLng(bounds.getCenter());
//		mBaiduMap.setMapStatus(u);

		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				Toast.makeText(
						context,
						"拖拽结束，新位置：" + marker.getPosition().latitude + ", "
								+ marker.getPosition().longitude,
						Toast.LENGTH_LONG).show();
			}

			public void onMarkerDragStart(Marker marker) {
			}
		});
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
		initOverlay();
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
		bd.recycle();
		bdGround.recycle();
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

}
