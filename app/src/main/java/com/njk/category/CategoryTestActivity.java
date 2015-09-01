package com.njk.category;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.njk.R;
import com.njk.BaseActivity;
import com.njk.category.CategoryMenuLayout.OnSelectedCategoryMenuListener;
import com.njk.utils.Logger;
import com.njk.utils.Utils;

public class CategoryTestActivity extends BaseActivity {
	private String TAG = "CategoryTestActivity";
	private CategoryMenuLayout categoryMenuLayout;
	private Activity context;
	
	CategoryMenuAdapter menuAdapter;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		context = this;
		setContentView(R.layout.category_test_layout);
		
		categoryMenuLayout = (CategoryMenuLayout)findViewById(R.id.custom_listview);
		List<CategoryMenuBean> menuList = CategoryMenuUtils.getTestMenuData();
		menuAdapter = new CategoryMenuAdapter(context, menuList);
		categoryMenuLayout.setAdapter(menuAdapter);
		categoryMenuLayout.setOnSelectedCategoryMenuListener(new OnSelectedCategoryMenuListener() {			
			@Override
			public void onSelectedCategoryMenuListener(CategoryMenuBean item, int positon, View view) {
				Toast.makeText(context, "positon = "+positon + ", item "+item, Toast.LENGTH_SHORT).show();
				showCategoryPop(view, item);
			}
		});
			
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
		View categoryLayout = getLayoutInflater().inflate(R.layout.category_window_layout, null);
		categoryListView = (ListView) categoryLayout.findViewById(R.id.category_list);
//		android.view.ViewGroup.LayoutParams layoutParams = categoryListView.getLayoutParams();
//		if(categoryGroup!=null && categoryGroup.getCategoryLevelMax()>0){
//			layoutParams.width = Utils.getDisplayMetrics(this)[0]/2-15;
//		}else{
//			layoutParams.width = LayoutParams.MATCH_PARENT;
//		}
		final CategoryGroup categoryGroup = menuItem.getCategoryGroup();
		final CategoryListAdapter categoryListAdapter = new CategoryListAdapter(context, categoryGroup);
		categoryListView.setAdapter(categoryListAdapter);
		
		categorySubListView  = (ListView) categoryLayout.findViewById(R.id.category_sublist);
		
		if(categoryGroup.getTmpCategory() != null && categoryGroup.getTmpCategory().subList!=null && categoryGroup.getTmpCategory().subList.size()>0){
					
			List<CategoryBean> subList = categoryGroup.getTmpCategory().subList;
			categorySubListView.setAdapter(new CategorySubListAdapter(context,categoryGroup,subList));
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
					categorySubListView.setAdapter(new CategorySubListAdapter(context,categoryGroup,subList));
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
		
		
		ColorDrawable drawable = new ColorDrawable(context.getResources().getColor(R.color.pop_bg_color));
		
		categoryGroupPop = new PopupWindow(this);
		categoryGroupPop.setWidth(LayoutParams.MATCH_PARENT);
		categoryGroupPop.setHeight(Utils.getDisplayMetrics(this)[1]*2/3);
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
}
