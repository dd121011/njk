package com.njk.view;

import com.njk.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class MapItemLayout extends LinearLayout {

	public MapItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public MapItemLayout(Context context) {
		super(context);
		initView();
	}

	private void initView(){
		View view =LayoutInflater.from(getContext()).inflate(R.layout.map_infowindow_layout2, null);
		this.addView(view);
	}
}
