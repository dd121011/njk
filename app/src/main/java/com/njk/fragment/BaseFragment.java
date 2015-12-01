package com.njk.fragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import com.njk.utils.UMengAnalysisUtils;

public class BaseFragment extends Fragment {

    protected boolean isAnalysis = false;//为true则进行统计，false则不进行统计
    protected void seTAnalysis(boolean isAnalysis){
        this.isAnalysis = isAnalysis;
    }
    public void setArgumentsUpdateUI(Bundle bundle){

    }

    @Override
    public void onResume() {
        super.onResume();
        if(isAnalysis){
            UMengAnalysisUtils.onPageStart(this.getClass().getSimpleName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isAnalysis){
            UMengAnalysisUtils.onPageEnd(this.getClass().getSimpleName());
        }
    }
}
