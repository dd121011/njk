package com.njk.category;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.njk.utils.Logger;

import java.util.List;

public class CategoryMenuLayout extends LinearLayout {
	private String TAG = "CategoryMenuLayout";
	private Context context;
	private CategoryMenuAdapter adapter;
	
	private List<CategoryMenuBean> menuList;
	
	private OnSelectedCategoryMenuListener selectedCategoryMenuListener;
	
	public CategoryMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public CategoryMenuLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context){
		this.context = context;

		menuList = CategoryMenuUtils.getTestMenuData();
		adapter = new CategoryMenuAdapter(context, menuList);
	}
	
	
	public void setAdapter(CategoryMenuAdapter adapter){
		if(adapter == null){
			return;
		}
		
		adapter.registerDataSetObserver(dataSetObserver);
		this.adapter = adapter;
		this.menuList = adapter.getListData();
		
		initChildViews();
	}
	
	
	private void initChildViews() {
		if(this.getChildCount()>0){
			this.removeAllViews();
		}

		for(int i=0;i<adapter.getCount();i++){
			View view = adapter.getView(i, null, this);
			this.addView(view);
			final int index = i;
			view.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(final View view) {
					handleSelectMenu(index,menuList);
					if(selectedCategoryMenuListener!=null){
						view.post(new Runnable() {							
							@Override
							public void run() {
								selectedCategoryMenuListener.onSelectedCategoryMenuListener(menuList.get(index), index, view);

								adapter.notifyDataSetChanged();
							}
						});
					}
					Logger.d("onClick", view.isSelected()+" = isSelected");
				}
			});
		}
	}
	
	private DataSetObserver dataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			// TODO Auto-generated method stub
			super.onChanged();
			initChildViews();
		}
	};
	
	public void setOnSelectedCategoryMenuListener(OnSelectedCategoryMenuListener listener){
		this.selectedCategoryMenuListener = listener;
	}
	
	/**
	 * 处理选中数据集合
	 * @param position
	 * @param listData
	 */
	public void handleSelectMenu(int position, List<CategoryMenuBean> listData){
		if(listData!= null){
			for(int i=0;i<listData.size();i++){
				CategoryMenuBean item = listData.get(i);
				if(i == position){
					item.setSelected(true);
				}else{
					item.setSelected(false);
				}
			}
		}
	}
	
	public interface OnSelectedCategoryMenuListener{
		void onSelectedCategoryMenuListener(CategoryMenuBean item, int positon, View view);
	}
}



