package com.njk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.njk.R;
import com.njk.activity.MainTabActivity;
import com.njk.activity.SwitchCityActivity;
import com.njk.utils.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public final class GuideFragment extends BaseFragment {
    private static final String KEY_CONTENT = "GuideFragment:Content";
    private static final String KEY_IMAGEID = "GuideFragment:imageId";

    public static GuideFragment newInstance(String content,int imageId) {
        GuideFragment fragment = new GuideFragment();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();

        fragment.imageId = imageId;
        
        
        return fragment;
    }

    private String mContent = "???";
    private int imageId = R.mipmap.ic_launcher;
    private DisplayImageOptions options;	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
            imageId = savedInstanceState.getInt(KEY_IMAGEID);
        }
        
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
//        TextView text = new TextView(getActivity());
//        text.setGravity(Gravity.CENTER);
//        text.setText(mContent);
//        text.setTextSize(20 * getResources().getDisplayMetrics().density);
//        text.setPadding(20, 20, 20, 20);
    	
    	ImageView imageView = new ImageView(getActivity());
    	imageView.setBackgroundResource(imageId);
    	imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    	LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	
//    	ImageLoader.getInstance().displayImage("drawable://" + R.drawable.face_test1, imageView, options);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(imageView,params);

        if(this.imageId == R.mipmap.guide_03){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNextActivity();
                }
            });
        }

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    private void startNextActivity(){
        String currCity = Config.getCurrCity(getActivity());
        if(!TextUtils.isEmpty(currCity)){
            startMainTabActivity();
        }else{
            Intent intent = new Intent(getActivity(),SwitchCityActivity.class);
            this.startActivityForResult(intent, 1000);
        }

    }


    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            startNextActivity();
        }
    }


    private void startNextActivity2(){
        boolean isLogin = false;
//        if(isLogin){
            startMainTabActivity();
//        }else{
//            Intent intent = new Intent(getActivity(),LoginActivity.class);
////			Intent intent = new Intent(context,CategoryTestActivity.class);
//            intent.putExtra("ismain",true);
//            getActivity().startActivity(intent);
//            Config.setHideGuided(getActivity(), true);
//            getActivity().finish();
//        }
    }


    private void startMainTabActivity() {
//		Intent intent = new Intent(context,CategoryTestActivity.class);
        Intent intent = new Intent(getActivity(),MainTabActivity.class);
        getActivity().startActivity(intent);
        Config.setHideGuided(getActivity(), true);
        getActivity().finish();
    }
}
