package com.njk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.njk.R;
import com.njk.activity.DetailsWebActivity;
import com.njk.adapter.DetailInfoListAdapter;
import com.njk.bean.DetailInfoBean;
import com.njk.bean.FamilyDetailBean;
import com.njk.view.MyFullListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public final class ShopDetailsComboFragment extends BaseFragment {
    private String TAG = "ShopDetailsInfoFragment";

    private View rootView;
    private MyFullListView listView;

    private Activity context;
    private DisplayImageOptions options;	
    private DetailInfoListAdapter mAdapter;

	private List<DetailInfoBean> mListItems;

	private FamilyDetailBean detailBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getActivity();
        
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.ic_stub)
		.showImageForEmptyUri(R.mipmap.ic_empty)
		.showImageOnFail(R.mipmap.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(rootView == null){
			rootView = inflater.inflate(R.layout.shop_details_combo, container, false);

			listView = (MyFullListView) rootView.findViewById(R.id.list_layout);

			Bundle bundle = getArguments();
			if(bundle!=null && bundle.getSerializable("obj")!=null){
				detailBean = (FamilyDetailBean)bundle.getSerializable("obj");
			}

			mListItems = new ArrayList<DetailInfoBean>();
			DetailInfoBean item1 = new DetailInfoBean();
			item1.setIconId(R.mipmap.info_icon1);
			item1.setTitle("农家简介");
			item1.setContent("放松感受农家宁静，乡土之情。。");
			item1.setType(DetailInfoBean.InfoType.INTRO);
			mListItems.add(item1);

			item1 = new DetailInfoBean();
			item1.setIconId(R.mipmap.info_icon2);
			item1.setTitle("饮食特色");
			item1.setContent("吃野味、常特产，只要新鲜。。");
			item1.setType(DetailInfoBean.InfoType.FEATURE);
			mListItems.add(item1);

			item1 = new DetailInfoBean();
			item1.setIconId(R.mipmap.info_icon3);
			item1.setTitle("住宿环境");
			item1.setContent("躺土炕、睡竹楼，亲近自然美。。");
			item1.setType(DetailInfoBean.InfoType.STAY);
			mListItems.add(item1);

			item1 = new DetailInfoBean();
			item1.setIconId(R.mipmap.info_icon4);
			item1.setTitle("娱乐游玩");
			item1.setContent("嬉戏乡间美景，找儿时感觉。。");
			item1.setType(DetailInfoBean.InfoType.RECREATION);
			mListItems.add(item1);

			item1 = new DetailInfoBean();
			item1.setIconId(R.mipmap.info_icon5);
			item1.setTitle("行程路线");
			item1.setContent("万事俱备，只待出发，启程。。");
			item1.setType(DetailInfoBean.InfoType.ROUTE);
			mListItems.add(item1);

			item1 = new DetailInfoBean();
			item1.setIconId(R.mipmap.info_icon6);
			item1.setTitle("费用说明");
			item1.setContent("农家优惠券，有么有。。");
			item1.setType(DetailInfoBean.InfoType.SPECIAL);
			mListItems.add(item1);
//			INTRO,FEATURE,STAY,RECREATION,ROUTE,SPECIAL
			mAdapter = new DetailInfoListAdapter(getActivity(), mListItems);
			listView.setAdapter(mAdapter);

			listView.setOnItemClickListener(itemClickListener);
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null)
	    {
	      parent.removeView(rootView);
	    }
        return rootView;
    }

	@Override
	public void setArgumentsUpdateUI(Bundle bundle) {
		super.setArgumentsUpdateUI(bundle);
		if(bundle!=null && bundle.getSerializable("obj")!=null){
			detailBean = (FamilyDetailBean)bundle.getSerializable("obj");
		}
	}

	AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			DetailInfoBean infoBean = (DetailInfoBean)parent.getAdapter().getItem(position);
			Intent intent = new Intent(context,DetailsWebActivity.class);
			intent.putExtra("obj",infoBean);
			intent.putExtra("detail",detailBean);
			context.startActivity(intent);
		}
	};
}
