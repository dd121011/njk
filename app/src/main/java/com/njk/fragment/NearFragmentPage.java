package com.njk.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.R;
import com.njk.activity.ShopDetailsActivity;
import com.njk.activity.ShopMapListActivity;
import com.njk.activity.SwitchCityActivity;
import com.njk.adapter.NearListAdapter;
import com.njk.bean.GetscenicBean;
import com.njk.bean.NearBean;
import com.njk.bean.ProvinceBean;
import com.njk.category.CategoryBean;
import com.njk.category.CategoryBeanUtils;
import com.njk.category.CategoryGroup;
import com.njk.category.CategoryListAdapter;
import com.njk.category.CategoryMenuAdapter;
import com.njk.category.CategoryMenuBean;
import com.njk.category.CategoryMenuLayout;
import com.njk.category.CategoryMenuLayout.OnSelectedCategoryMenuListener;
import com.njk.category.CategoryMenuUtils;
import com.njk.category.CategorySubListAdapter;
import com.njk.db.ProvinceDBUtils;
import com.njk.manager.CurrCityManager;
import com.njk.manager.CurrCityManager.OnChangerCurrCityListener;
import com.njk.net.RequestCommandEnum;
import com.njk.net.RequestUtils;
import com.njk.net.RequestUtils.ResponseHandlerInterface;
import com.njk.utils.Config;
import com.njk.utils.DialogUtil;
import com.njk.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class NearFragmentPage extends Fragment implements OnClickListener{
	private static String TAG="NearFragmentPage";
	public final static int UPDATE_DATA_LIST = 1;
	public final static int MORE_DATE_LIST = 2;
	public final static int UPATE_LIST_LAYOUT = 3;
	public final static int GET_DATE_FAIL = 100;

	private final static int GET_CITY_DATA_SUCCES = 4;
	
	private View rootView;
	private ListView listView ;
	private TextView currCity;
	private List<NearBean> nearBeanList;
	private PtrClassicFrameLayout mPtrFrame;
	private NearListAdapter mAdapter;
	private CategoryMenuAdapter menuAdapter;
	private CategoryMenuLayout categoryMenuLayout;

	private Activity activity;
	private RequestQueue mQueue;
	
	private CurrCityManager cityManger;
	
	private List<CategoryMenuBean> menuList;
	private CategoryMenuBean menuBean0,menuBean1,menuBean2;
	
	private int offset = 0;
	private int per_page = 10;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_CITY_DATA_SUCCES:

				break;
			case UPDATE_DATA_LIST:
				List<NearBean> dataList = (List<NearBean>) msg.getData().getSerializable("data");
				if(offset == 0){
					nearBeanList.clear();
				}
				offset = per_page;
				nearBeanList.addAll(dataList);
				handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
				break;
			case MORE_DATE_LIST:
				offset += per_page;
				List<NearBean> moreList = (List<NearBean>) msg.getData().getSerializable("data");
				nearBeanList.addAll(moreList);
				handler.sendEmptyMessage(UPATE_LIST_LAYOUT);
				break;
			case UPATE_LIST_LAYOUT:				 
				mPtrFrame.refreshComplete();
				mAdapter.notifyDataSetChanged();
				break;
			case GET_DATE_FAIL:
				mPtrFrame.refreshComplete();
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
		mQueue = Volley.newRequestQueue(activity);  
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(rootView == null){
			rootView = inflater.inflate(R.layout.near_fragment_layout, container, false);
			
	        listView = (ListView) rootView.findViewById(R.id.rotate_header_list_view);
	        listView.setOnItemClickListener(new NearListOnItemClickListener());
	        
	        rootView.findViewById(R.id.switch_adress_btn).setOnClickListener(this);
	        rootView.findViewById(R.id.map_btn).setOnClickListener(this);
	        
			categoryMenuLayout = (CategoryMenuLayout)rootView.findViewById(R.id.category_menu_layout);
			String[] strArr = activity.getResources().getStringArray(R.array.near_nemus);
//			menuList = CategoryMenuUtils.getTestMenuData();
			menuList = CategoryMenuUtils.getMenuData(strArr);
			menuBean0 = menuList.get(0);
			menuBean1 = menuList.get(1);
			menuBean2 = menuList.get(2);
			
			String[] strArr1 = activity.getResources().getStringArray(R.array.near_nemu1);
			List<CategoryBean> list1 = menuBean1.getCategoryGroup().getCategoryListData();
			for(int i=0;i<strArr1.length;i++){
				CategoryBean bean = new CategoryBean();
				bean.id = i+"";
				bean.name = strArr1[i];
				list1.add(bean);
			}
			
			String[] strArr2 = activity.getResources().getStringArray(R.array.near_nemu2);
			List<CategoryBean> list2 = menuBean2.getCategoryGroup().getCategoryListData();
			for(int i=0;i<strArr2.length;i++){
				CategoryBean bean = new CategoryBean();
				bean.id = i+"";
				bean.name = strArr2[i];
				list2.add(bean);
			}
						
			menuAdapter = new CategoryMenuAdapter(activity, menuList);
			categoryMenuLayout.setAdapter(menuAdapter);
			categoryMenuLayout.setOnSelectedCategoryMenuListener(new OnSelectedCategoryMenuListener() {			
				@Override
				public void onSelectedCategoryMenuListener(CategoryMenuBean item, int positon, View view) {
//					Toast.makeText(activity, "positon = "+positon + ", item "+item, Toast.LENGTH_SHORT).show();
					showCategoryPop(view, item);
				}
			});
	        
	        currCity = (TextView) rootView.findViewById(R.id.curr_city_text);
	        String city = Config.getCurrCity(activity);
	        if(!TextUtils.isEmpty(city)){
	        	currCity.setText(city);
	        	setCurProvinceCategoryGroup(city);
	        };
			
			cityManger = CurrCityManager.getInstance();
			cityManger.registerChangerCurrCityListener(currCityListener);
			
			nearBeanList = new ArrayList<NearBean>();
			mAdapter = new NearListAdapter(getActivity(), nearBeanList);
			
			listView.setAdapter(mAdapter);
			
			listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					if(firstVisibleItem == (totalItemCount - 2)){
						startGetData();
					}
					
				}
			});
			
			mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.rotate_header_list_view_frame);
			mPtrFrame.setLastUpdateTimeRelateObject(this);
			mPtrFrame.setPtrHandler(new PtrHandler() {
			   @Override
			   public void onRefreshBegin(PtrFrameLayout frame) {
				   offset = 0;
				   startGetData();
			   }
			
			   @Override
			   public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
			         return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
			   }
			});
			    // the following are default settings
			mPtrFrame.setResistance(1.7f);
			mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
			mPtrFrame.setDurationToClose(200);
			mPtrFrame.setDurationToCloseHeader(1000);
			// default is false
			mPtrFrame.setPullToRefresh(false);
			// default is true
			mPtrFrame.setKeepHeaderWhenRefresh(true);
			mPtrFrame.postDelayed(new Runnable() {
			    @Override
			    public void run() {
//			        mPtrFrame.autoRefresh();
			    }
			}, 100);
			
			startGetData();
			
			getGetscenic();
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
		
		return rootView;		
	}	
	
	private void setCurProvinceCategoryGroup(String currCity){
		ProvinceBean provinceBean = ProvinceDBUtils.getProvince(activity, currCity);
	}
	
	PopupWindow categoryGroupPop;
	boolean isShowPopup;
	ListView categoryListView,categorySubListView;
	/**
	 * 显示类目筛选项窗口
	 * showCategoryPop() 
	 * @param anchorView
	 * @param categoryGroup  
	 * @return void
	 * @author liujunbin
	 */
	@SuppressLint("NewApi")
	public void showCategoryPop(final View anchorView,final CategoryMenuBean menuItem){
		View categoryLayout = activity.getLayoutInflater().inflate(R.layout.category_window_layout, null);
		categoryListView = (ListView) categoryLayout.findViewById(R.id.category_list);
//		android.view.ViewGroup.LayoutParams layoutParams = categoryListView.getLayoutParams();
//		if(categoryGroup!=null && categoryGroup.getCategoryLevelMax()>0){；
//			layoutParams.width = Utils.getDisplayMetrics(this)[0]/2-15;
//		}else{
//			layoutParams.width = LayoutParams.MATCH_PARENT;
//		}
		final CategoryGroup categoryGroup = menuItem.getCategoryGroup();
		final CategoryListAdapter categoryListAdapter = new CategoryListAdapter(activity, categoryGroup);
		categoryListView.setAdapter(categoryListAdapter);
		
		categorySubListView  = (ListView) categoryLayout.findViewById(R.id.category_sublist);
		
		if(categoryGroup.getTmpCategory() != null && categoryGroup.getTmpCategory().subList!=null && categoryGroup.getTmpCategory().subList.size()>0){
					
			List<CategoryBean> subList = categoryGroup.getTmpCategory().subList;
			categorySubListView.setAdapter(new CategorySubListAdapter(activity,categoryGroup,subList));
			categorySubListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					CategorySubListAdapter adapter = (CategorySubListAdapter) parent.getAdapter();
					CategoryBean subItem = (CategoryBean) adapter.getItem(position);
					categoryGroup.setTmpSubCategory(subItem);	
					dismiss();
					menuItem.title = subItem.name;
					menuAdapter.notifyDataSetChanged();
				}
			});
		}else{
			categorySubListView.setVisibility(View.GONE);
		}
		
		categoryListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CategoryListAdapter adapter = (CategoryListAdapter) parent.getAdapter();
				CategoryBean item = (CategoryBean) adapter.getItem(position);
				
				List<CategoryBean> subList = item.subList;
				if(subList!=null && subList.size()>0){
					categoryGroup.setTmpCategory(item);
					categoryListAdapter.notifyDataSetChanged();
					categorySubListView.setAdapter(new CategorySubListAdapter(activity,categoryGroup,subList));
					categorySubListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							CategorySubListAdapter adapter = (CategorySubListAdapter) parent.getAdapter();
							CategoryBean subItem = (CategoryBean) adapter.getItem(position);
							categoryGroup.setTmpSubCategory(subItem);	
							dismiss();
							menuItem.title = subItem.name;
							menuAdapter.notifyDataSetChanged();
						}
					});
				}else{
					categoryGroup.setTmpCategory(item);
					categoryGroup.setTmpSubCategory(item);
					dismiss();
					menuItem.title = item.name;
					menuAdapter.notifyDataSetChanged();
				}
				
			}
		});	
		
		
		ColorDrawable drawable = new ColorDrawable(activity.getResources().getColor(R.color.pop_bg_color));
		
		categoryGroupPop = new PopupWindow(activity);
		categoryGroupPop.setWidth(LayoutParams.MATCH_PARENT);
		categoryGroupPop.setHeight(Utils.getDisplayMetrics(activity)[1]*2/3);
		categoryGroupPop.setBackgroundDrawable(drawable);

		categoryGroupPop.setContentView(categoryLayout);
		categoryGroupPop.setAnimationStyle(R.style.MorePopAnimation);
		categoryGroupPop.setFocusable(true);
		categoryGroupPop.setOutsideTouchable(true);
		
		categoryGroupPop.showAsDropDown(anchorView);

		isShowPopup = true;
		
		categoryGroupPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				//To do
				isShowPopup = false;
				
			}
		});
		
	}
	
	public void dismiss(){
		if(categoryGroupPop!=null && categoryGroupPop.isShowing()){
			categoryGroupPop.dismiss();
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}

	private boolean isStart = false;
	public void startGetData(){
		if(isStart){
			return;
		}
		DialogUtil.progressDialogShow(activity, activity.getResources().getString(R.string.is_loading));
		isStart = true;
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("offset", offset+"");
		params.put("per_page", per_page+"");
//		params.put("Token", Config.getUserToken(activity)+"");
//		params.put("keyword", "");
//		params.put("lat", Config.getCurLat(activity));
//		params.put("lng", Config.getCurLng(activity));
//		params.put("city_id", Config.getCurrCityId(activity));
//		params.put("scenic_id", "scenic_id");
//		params.put("orderby", "orderby");
//		Token	Token值
//		offset	偏移量(0,10,20)默认0
//		per_page	每页显示数(10)
//		source	1web 2 android 3 ios
//		keyword	搜索农家客关键字
//		lat	经度
//		lng	维度
//		city_id	城市id
//		scenic_id	景区id
//		orderby	排序(1:距离最近2:人气最高3:点评最多4:人均最低5:人均最高)

		RequestUtils.startStringRequest(Method.GET,mQueue, RequestCommandEnum.FAMILY_LIST,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response); 
				 isStart = false;
				 DialogUtil.progressDialogDismiss();
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code") && obj.getString("code").equals("0")){
							 String jsonArray = obj.getString("data");
							 Gson gson = new Gson();
							 ArrayList<NearBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<NearBean>>(){}.getType());
							 Bundle data = new Bundle();
							 data.putSerializable("data", dataList);
							 Message msg = null;
							 if(offset == 0){
								 msg = handler.obtainMessage(UPDATE_DATA_LIST);
							 }else{
								 msg = handler.obtainMessage(MORE_DATE_LIST);
							 }
							 msg.setData(data);
							 msg.sendToTarget();
						 }
					 }
				} catch (Exception e) {
					e.printStackTrace();
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
			
		},params);

	}
	
	class NearListOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(activity,ShopDetailsActivity.class);
//			Intent intent = new Intent(activity,DetailWebViewActivity.class);
			activity.startActivity(intent);
		}
		
	}
	
	/**
	 * 获取景区列表数据
	 */
	public void getGetscenic(){
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("Token", Config.getUserToken(activity)+"");
		params.put("city_id", Config.getCurrCityId(activity));

		RequestUtils.startStringRequest(Method.POST,mQueue, RequestCommandEnum.FAMILY_GETSCENIC,new ResponseHandlerInterface(){

			@Override
			public void handlerSuccess(String response) {
				// TODO Auto-generated method stub
				 Log.d(TAG, response); 
				 try {
					if(!TextUtils.isEmpty(response)){
						 JSONObject obj = new JSONObject(response);
						 if(obj.has("code") && obj.getString("code").equals("0")){
							 String jsonArray = obj.getString("data");
							 Gson gson = new Gson();
							 ArrayList<GetscenicBean> dataList = gson.fromJson(jsonArray, new TypeToken<List<GetscenicBean>>(){}.getType());
							 CategoryGroup group = menuBean0.getCategoryGroup();
							 if(group!=null){
								 List<CategoryBean> categoryListData = group.getCategoryListData();
								 categoryListData.addAll(CategoryBeanUtils.getscenicToCategory(dataList));
							 }
						 }
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void handlerError(String error) {
				// TODO Auto-generated method stub
				Log.e(TAG, error);  
			}
			
		},params);

	}


	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.switch_adress_btn:
			intent = new Intent(activity,SwitchCityActivity.class);
			activity.startActivity(intent);
			break;
		case R.id.map_btn:
			intent = new Intent(activity,ShopMapListActivity.class);
			activity.startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	
	private OnChangerCurrCityListener currCityListener = new OnChangerCurrCityListener(){
		@Override
		public void onChangeCurrCity(final String city) {	
			activity.runOnUiThread(new Runnable() {
				public void run() {
					if(!TextUtils.isEmpty(city)){
			        	currCity.setText(city);
			        	setCurProvinceCategoryGroup(city);
			        };
				}
			});			
		}	
	};
	
	
}
