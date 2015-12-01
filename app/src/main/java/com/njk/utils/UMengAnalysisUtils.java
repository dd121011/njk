package com.njk.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by a on 2015/12/1.
 */
public class UMengAnalysisUtils {

    /**
     * 是否打开Activity自动统计
     * @param isOpen
     */
    public static void openActivityDurationTrack(boolean isOpen){
        MobclickAgent.openActivityDurationTrack(isOpen);
    }

    /**
     * 统计activity页面开始
     * @param context
     * @param name
     */
    public static void onResume(Context context,String name){
        MobclickAgent.onPageStart(name);
        MobclickAgent.onResume(context);
    }

    /**
     * 统计activity页面结束
     * @param context
     * @param name
     */
    public static void onPause(Context context,String name){
        MobclickAgent.onPageEnd(name);
        MobclickAgent.onPause(context);
    }

    /**
     * 统计fragment开始
     * @param name
     */
    public static void onPageStart(String name){
        MobclickAgent.onPageStart(name);
    }

    /**
     * 统计fragment结束
     * @param name
     */
    public static void onPageEnd(String name){
        MobclickAgent.onPageEnd(name);
    }
}
