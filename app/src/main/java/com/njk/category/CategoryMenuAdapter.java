package com.njk.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.njk.R;
import com.njk.view.ViewHolder;

import java.util.List;

public class CategoryMenuAdapter extends BaseAdapter{
	private List<CategoryMenuBean> menuList;
	private Context context;
	private LayoutInflater inflater;
	public CategoryMenuAdapter(Context context , List<CategoryMenuBean> menuList) {
		this.menuList = menuList;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuList!=null?menuList.size():0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return menuList!=null?menuList.get(arg0):null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		if(view==null){
			view = inflater.inflate(R.layout.category_menu_item, null);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
		}
		
		CategoryMenuBean item = menuList.get(arg0);
		ImageView icon = ViewHolder.get(view, R.id.icon);
		TextView title_text = ViewHolder.get(view, R.id.title_text);
		TextView hint_text = ViewHolder.get(view, R.id.hint_text);
		
		title_text.setText(item.getTitle());
		
		if(item.isSelected){
			view.setSelected(true);
			icon.setSelected(true);
			title_text.setSelected(true);
			hint_text.setSelected(true);
		}else{
			view.setSelected(false);
			icon.setSelected(false);
			title_text.setSelected(false);
			hint_text.setSelected(false);
		}
		
		return view;
	}
	
	public List<CategoryMenuBean> getListData(){
		return menuList;
	}
}