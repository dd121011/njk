package com.njk.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.njk.R;

/**
 * Created by a on 2015/9/18.
 */
public class LoadingDialog  extends Dialog {
    private ImageView  mLoadingImageView;
    private TextView mLoadingTextView;
    private Animation mLoadingAnimation;
    private String msg;
    public LoadingDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View loadingView= LayoutInflater.from(getContext()).inflate(R.layout.loading_layout, null);
        mLoadingImageView=(ImageView) loadingView.findViewById(R.id.loadingImageView);
        mLoadingTextView = (TextView)loadingView.findViewById(R.id.loadingTextView);
        setContentView(loadingView);
    }

    @Override
    public void show() {
        super.show();
        mLoadingTextView.setText(this.msg);
        mLoadingAnimation= AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
        LinearInterpolator lir = new LinearInterpolator();
        mLoadingAnimation.setInterpolator(lir);
        mLoadingImageView.startAnimation(mLoadingAnimation);
    }
    @Override
    public void dismiss() {
        mLoadingAnimation.cancel();
        mLoadingImageView.clearAnimation();
        super.dismiss();
    }

    public void setLoadingText(String text){
        this.msg = text;
    }

}
