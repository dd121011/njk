package com.njk.category;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njk.R;
import com.njk.view.ViewHolder;

import java.util.List;

public class CategoryListAdapter extends BaseAdapter {
	Activity context;
	CategoryGroup categoryGroup;
	List<CategoryBean> list;
	private LayoutInflater inflater;

	public CategoryListAdapter(Activity context,CategoryGroup categoryGroup) {
		this.context = context;
		this.categoryGroup = categoryGroup;
		this.list = categoryGroup.getCategoryListData();
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size()-1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position+1);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position+1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = inflater.inflate(R.layout.category_list_item, null);  
		}

		CategoryBean item = list.get(position+1);
		
		ImageView icon = ViewHolder.get(convertView, R.id.icon);
		TextView title_text = ViewHolder.get(convertView, R.id.title_text);
		TextView hint_text = ViewHolder.get(convertView, R.id.hint_text);

		title_text.setText(item.name);
		
		if(item.equals(categoryGroup.getTmpCategory())){
			convertView.setBackgroundColor(Color.TRANSPARENT);
			title_text.setSelected(true);
			icon.setVisibility(View.VISIBLE);
			icon.setSelected(true);
		}else{
			convertView.setBackgroundColor(Color.WHITE);
			title_text.setSelected(false);
			icon.setVisibility(View.INVISIBLE);
			icon.setSelected(false);
		}
		
		return convertView;
	}

}
