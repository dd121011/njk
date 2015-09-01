package com.njk.activity;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;

import com.njk.BaseActivity;
import com.njk.Global;
import com.njk.R;
import com.njk.adapter.BarberDetailsFaceAdapter;
import com.njk.fragment.BarberFansFragment;
import com.njk.fragment.BarberWorksFragment;
import com.njk.fragment.ShopDetailsRemarkFragment;
import com.njk.utils.LocalDisplay;
import com.njk.utils.Logger;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;
import com.njk.view.MyScrollView;
import com.njk.view.MyScrollViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BarberDetailsActivity extends BaseActivity implements OnClickListener{
	
	private ImageView topImg;
	private RadioGroup swithRadio;
	private GridView barberFaceLayout;
	private MyScrollView scroll_layout;
	private View title1,title2;
	private ImageView face_img;
	
	private Activity context;
	private FragmentManager fm;
	private List<Fragment> fragmentlist;
	private LinkedList<String> mListItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		View rootView = LayoutInflater.from(context).inflate(R.layout.baber_details_layout, null);
		setContentView(rootView);	
		Utils.showTopBtn(rootView, "", TOP_BTN_MODE.SHOWBOTH,"","");
		scroll_layout = (MyScrollView) rootView.findViewById(R.id.scroll_layout);
		scroll_layout.setMyScrollViewListener(myScrollViewListener);
		scroll_layout.setOnTouchListener(myOnTouchListener);
		
		face_img = (ImageView) findViewById(R.id.face_img_tmp);
		
		initTitle(rootView);
		
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.share_btn).setOnClickListener(this);		
		
		fm = getSupportFragmentManager();  
		
		
		swithRadio = (RadioGroup) rootView.findViewById(R.id.swith_shop_details);		
		swithRadio.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		swithRadio.check(R.id.radio_btn2);
		
		
		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(Global.mStrings3));
		barberFaceLayout = (GridView) rootView.findViewById(R.id.barber_face_layout);
		BarberDetailsFaceAdapter adapter = new BarberDetailsFaceAdapter(context, mListItems);
		barberFaceLayout.setAdapter(adapter);

		
	}

	private void initTitle(View rootView) {
//		View title1 = rootView.findViewById(R.id.title_layout1);
//		View title2 = rootView.findViewById(R.id.title_layout2);
//		
//		Utils.showTopBtn(title1, "", TOP_BTN_MODE.SHOWBOTH,"","");
//		Utils.showTopBtn(title2, "", TOP_BTN_MODE.SHOWBOTH,"","");
//		
//		title1.findViewById(R.id.back_btn).setOnClickListener(this);
//		title2.findViewById(R.id.back_btn).setOnClickListener(this);
//		
//		title1.findViewById(R.id.share_btn).setOnClickListener(this);
//		title2.findViewById(R.id.share_btn).setOnClickListener(this);
	}
	
	private void changeFragment(String mTag,Class mClass){  
		Fragment mFragment = fm.findFragmentByTag(mTag);
        FragmentTransaction ft = fm.beginTransaction();  
        if (mFragment == null) {
            mFragment = Fragment.instantiate(context, mClass.getName(), null);
            ft.add(R.id.fragment_content, mFragment, mTag);
            if(fragmentlist == null){
            	fragmentlist = new ArrayList<Fragment>();
            }
            fragmentlist.add(mFragment);
        } 
        
        for(Fragment f: fragmentlist){
        	if(!mFragment.equals(f)){
        		ft.hide(f);
        	}else{
        		ft.show(f); 
        	}
        }
        
        ft.commit();
    }  

	class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
//			Toast.makeText(context, "checkedId = "+checkedId, 1000).show();
			switch (checkedId) {
			case R.id.radio_btn1:
				changeFragment(checkedId+"",BarberWorksFragment.class);
				break;
			case R.id.radio_btn2:
				changeFragment(checkedId+"",ShopDetailsRemarkFragment.class);
				break;
			case R.id.radio_btn3:
				changeFragment(checkedId+"",BarberFansFragment.class);
				break;
			default:
				break;
			}
		}
		
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
	
	LayoutParams  preParams =  new LayoutParams((int) (LocalDisplay.dp2px(60)),(int) (LocalDisplay.dp2px(60)));
	LayoutParams  targeParams  =  new LayoutParams((int) (LocalDisplay.dp2px(30)),(int) (LocalDisplay.dp2px(30)));

	int up = 1;
	int down = 2;
	private void changerFaceImg(int cur, int offser){
		LayoutParams params = (LayoutParams) face_img.getLayoutParams();		
		switch (cur) {
		case 1:
			   		
    		if(params.width > targeParams.width && offser <= targeParams.width){
    			params.width = (int) (params.width - 8);
        		params.height = (int) (params.height - 8);
    		}
    		
    		
			break;
		case 2:
    		if(params.width < preParams.width && offser <= targeParams.width){
    			params.width = (int) (params.width + 8);
        		params.height = (int) (params.height + 8);
    		}
			break;
		default:
			break;
		}

    	
    	face_img.setLayoutParams(params);
	}
	
	private MyScrollViewListener myScrollViewListener = new MyScrollViewListener(){

		@Override
		public void onScrollChanged(MyScrollView scrollView, int x, int y,
				int oldx, int oldy) {
			Logger.d("x = "+x+",y = "+y+", oldx = "+oldx+", oldy = "+oldy);

		}
		
	};
	
	
	private OnTouchListener myOnTouchListener = new OnTouchListener(){
		float y = 0;
		@Override
		public boolean onTouch(View v, MotionEvent ev) {
			int action = ev.getAction();  
			switch(action) {  
			case MotionEvent.ACTION_DOWN:  
				y = ev.getY();// 获取点击y坐标  
			break;  
			case MotionEvent.ACTION_UP:  
			break;  
			case MotionEvent.ACTION_MOVE:  
				final float preY = y;  
				float nowY = ev.getY();  
				int deltaY = (int) (preY - nowY);// 获取滑动距离  
				y = nowY;  
				
		        if (Math.abs(deltaY) > 5) {
		        	if(deltaY>0){
		        		changerFaceImg(up,Math.abs(deltaY));
		        	}else{
		        		changerFaceImg(down,Math.abs(deltaY));
		        	}
		        }
			
			break;  
			default:  
			break;  

			}
			return false;  
		}
		
	};
}
