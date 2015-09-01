package com.njk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**  
 * @author Peng.Lin
 * @E-mail:peng.lin@group-net.cn
 * @version 创建时间：2013-4-7 下午04:59:09
 */
public class MyScrollView extends ScrollView {
	
	private MyScrollViewListener scrollViewListener = null; 

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context) {
		super(context);
	}
	
	public void setMyScrollViewListener(MyScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	} 
	
	@Override     
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if(scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	} 
}
