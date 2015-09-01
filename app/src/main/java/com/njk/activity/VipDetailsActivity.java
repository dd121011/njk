package com.njk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.njk.BaseActivity;
import com.njk.R;
import com.njk.adapter.VipListAdapter;
import com.njk.utils.Utils;
import com.njk.utils.Utils.TOP_BTN_MODE;

import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class VipDetailsActivity extends BaseActivity implements
		OnClickListener {

	private ListView listView;

	private Activity context;
	private LinkedList<String> mListItems;
	private PtrClassicFrameLayout mPtrFrame;
	private VipListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.vip_details_layout, null);
		setContentView(rootView);
		Utils.showTopBtn(rootView, "会员卡详情", TOP_BTN_MODE.SHOWBOTH,"","");
		rootView.findViewById(R.id.back_btn).setOnClickListener(this);
		rootView.findViewById(R.id.share_btn).setOnClickListener(this);
		
		
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
