package com.njk.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.njk.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public final class GuideFragment extends Fragment {
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

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}
