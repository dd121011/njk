package com.njk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.njk.BaseActivity;
import com.njk.R;
import com.njk.manager.CurrCityManager;
import com.njk.pinnedheaderlistView.BladeView;
import com.njk.pinnedheaderlistView.BladeView.OnItemClickBladeListener;
import com.njk.pinnedheaderlistView.City;
import com.njk.pinnedheaderlistView.CityDao;
import com.njk.pinnedheaderlistView.CityListAdapter;
import com.njk.pinnedheaderlistView.DBHelper;
import com.njk.pinnedheaderlistView.MySectionIndexer;
import com.njk.pinnedheaderlistView.PinnedHeaderListView;
import com.njk.utils.Config;
import com.njk.utils.LocationClientUtils;
import com.njk.utils.LocationClientUtils.LocatonListener;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SwitchCityActivity extends BaseActivity implements OnClickListener {
	private String TAG = "SwitchCityActivity";

	private Activity context;

	private CurrCityManager cityManger;

	private final static int UPDATE_CITY_LIST = 2;

	private static final int COPY_DB_SUCCESS = 10;
	private static final int COPY_DB_FAILED = 11;
	protected static final int QUERY_CITY_FINISH = 12;
	private MySectionIndexer mIndexer;

	private List<City> cityList = new ArrayList<City>();
	private DBHelper helper;

	private CityListAdapter mAdapter;
	private static final String ALL_CHARACTER = "定热#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private String[] sections = {"定位城市","热门城市", "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private int[] counts;
	private PinnedHeaderListView mListView;
	private RequestQueue mQueue;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case QUERY_CITY_FINISH:

				if (mAdapter == null) {

					mIndexer = new MySectionIndexer(sections, counts);

					mAdapter = new CityListAdapter(cityList, mIndexer, getApplicationContext());
					mListView.setAdapter(mAdapter);

					mListView.setOnScrollListener(mAdapter);

					// 設置頂部固定頭部
					mListView.setPinnedHeaderView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_group_item, mListView, false));

					mListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							cityManger.setCurrCity(context, cityList.get(arg2).getName());
							context.finish();							
						}
					});
					
				} else if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}

				break;

			case COPY_DB_SUCCESS:
				requestData();
				break;
			case UPDATE_CITY_LIST:
				String city = Config.getLocationCity(context);
				if(city!=null && cityList!=null && cityList.size()>0){
					City item = cityList.get(0);
					item.setName(city);
				}
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
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

		View rootView = LayoutInflater.from(context).inflate(R.layout.switch_city_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "城市切换", TOP_BTN_MODE.SHOWBACK, "", "");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);

		helper = new DBHelper();

		copyDBFile();
		findView();
		
		cityManger = CurrCityManager.getInstance();
		
		LocationClientUtils.getInstance().addListenter(locationListener);
        LocationClientUtils.getInstance().start();
	}

	private void copyDBFile() {

		File file = new File(CityDao.APP_DIR + "/city.db");
		if (file.exists()) {
			requestData();

		} else { // 拷贝文件
			Runnable task = new Runnable() {

				@Override
				public void run() {

					copyAssetsFile2SDCard("city.db");
				}
			};

			new Thread(task).start();
		}
	}

	/**
	 * 拷贝资产目录下的文件到 手机
	 */
	private void copyAssetsFile2SDCard(String fileName) {

		File desDir = new File(CityDao.APP_DIR);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}

		// 拷贝文件
		File file = new File(CityDao.APP_DIR + fileName);
		if (file.exists()) {
			file.delete();
		}

		try {
			InputStream in = getAssets().open(fileName);

			FileOutputStream fos = new FileOutputStream(file);

			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf)) > 0) {
				fos.write(buf, 0, len);
			}

			fos.flush();
			fos.close();

			handler.sendEmptyMessage(COPY_DB_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(COPY_DB_FAILED);
		}
	}

	private void requestData() {

		Runnable task = new Runnable() {

			@Override
			public void run() {
				CityDao dao = new CityDao(helper);

				City localCity = new City();
				localCity.setId("");
				localCity.setName("定位城市");
				localCity.setPys("定位城市");
				
				List<City> hot = dao.getHotCities(); // 热门城市
				
				if(hot!=null){
					for(City item: hot){
						item.setPys("热门城市");
					}		
				}

				
				List<City> all = dao.getAllCities(); // 全部城市

				if (all != null) {

					Collections.sort(all, new MyComparator()); // 排序
					cityList.add(localCity);
					cityList.addAll(hot);
					cityList.addAll(all);

					// 初始化每个字母有多少个item
					counts = new int[sections.length];

					counts[0] = 1; 
					counts[1] = hot.size();// 热门城市 个数

					for (City city : all) { // 计算全部城市

						String firstCharacter = city.getSortKey();
						int index = ALL_CHARACTER.indexOf(firstCharacter);
						counts[index]++;
					}

					handler.sendEmptyMessage(QUERY_CITY_FINISH);
				}
			}
		};

		new Thread(task).start();
	}

	public class MyComparator implements Comparator<City> {

		@Override
		public int compare(City c1, City c2) {

			return c1.getSortKey().compareTo(c2.getSortKey());
		}

	}

	private void findView() {

		mListView = (PinnedHeaderListView) findViewById(R.id.mListView);
		BladeView mLetterListView = (BladeView) findViewById(R.id.mLetterListView);

		mLetterListView.setOnItemClickListener(new OnItemClickBladeListener() {

			@Override
			public void onItemClick(String s) {
				if (s != null) {

					int section = ALL_CHARACTER.indexOf(s);

					int position = mIndexer.getPositionForSection(section);

					Log.i(TAG, "s:" + s + ",section:" + section + ",position:" + position);

					if (position != -1) {
						mListView.setSelection(position);
					} else {

					}
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		LocationClientUtils.getInstance().removeListener(locationListener);
		super.onDestroy();
	}
	
	private LocatonListener locationListener = new LocatonListener() {

		@Override
		public void onReceiveLocation(final BDLocation location) {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (location != null) {
//						cityManger.setCurrCity(context, arg0.getCity());
						Config.setLocationCity(context, location.getCity());
						handler.sendEmptyMessage(UPDATE_CITY_LIST);
					}
				}
			});
		}
	};

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
