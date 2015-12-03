package com.njk.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.njk.activity.BNDemoGuideActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2015/12/3.
 */
public class NaviUtils {
    private String TAG = "NaviUtils";

    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "BNSDKDemo";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private Activity context;
    private CoordinateType naviType = CoordinateType.WGS84;
    private MyRoutePlanListener myRoutePlanListener = new MyRoutePlanListener() {
        @Override
        public void onJumpToNavigator() {

        }

        @Override
        public void onRoutePlanFailed() {

        }
    };
    private MyNaviInitListener listener = new MyNaviInitListener() {
        @Override
        public void onAuthResult(int status, String msg) {

        }

        @Override
        public void initSuccess() {

        }

        @Override
        public void initStart() {

        }

        @Override
        public void initFailed() {

        }
    };


    private static NaviUtils instance;
    private NaviUtils() {}
    public static NaviUtils getInstance(){
        if(instance == null){
            instance =new NaviUtils();
        }
        return instance;
    }

    public void init(Activity context,MyNaviInitListener listener){
        this.context = context;
        if ( initDirs() ) {
            initNavi(listener);
        }
    }

    public void startNavi(String eLng,String eLat,String endName,MyRoutePlanListener myRoutePlanListener){

        if(myRoutePlanListener!=null){
            this.myRoutePlanListener = myRoutePlanListener;
        }

        if (!BaiduNaviManager.isNaviInited()){
            this.myRoutePlanListener.onRoutePlanFailed();
            return;
        }

        naviType = CoordinateType.WGS84;//国际经纬度坐标
//        naviType = CoordinateType.GCJ02;//国测局坐标
//        naviType = CoordinateType.BD09_MC;//百度墨卡托坐标

        String startLng = Config.getCurLng(context);
        String startLat = Config.getCurLat(context);
        Double lng = 0d;
        Double lat = 0d;
        if(!TextUtils.isEmpty(startLng)){
            lng =Double.valueOf(startLng);
        }else{
            this.myRoutePlanListener.onRoutePlanFailed();
            return;
        }
        if(!TextUtils.isEmpty(startLat)){
            lat =Double.valueOf(startLat);
        }else{
            this.myRoutePlanListener.onRoutePlanFailed();
            return;
        }

        Double endLng = 0d;
        Double endLat = 0d;
        if(!TextUtils.isEmpty(eLng)){
            endLng =Double.valueOf(eLng);
        }else{
            this.myRoutePlanListener.onRoutePlanFailed();
            return;
        }
        if(!TextUtils.isEmpty(eLat)){
            endLat =Double.valueOf(eLat);
        }else{
            this.myRoutePlanListener.onRoutePlanFailed();
            return;
        }


        BNRoutePlanNode sNode = new BNRoutePlanNode(lng, lat,
                Config.getCurAddr(context), null, naviType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(endLng, endLat,
                endName, null, naviType);

        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(context, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }


    String authinfo = "";
    private void initNavi(MyNaviInitListener listener) {
        BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath + "/BaiduNaviSDK_SO");

        if(listener!=null){
            this.listener = listener;
        }

        BaiduNaviManager.getInstance().init(context, mSDCardPath, APP_FOLDER_NAME,
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(final int status,final String msg) {
                        if (0 == status) {
                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        context.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Logger.d(TAG,authinfo);
                                NaviUtils.this.listener.onAuthResult(status,msg);
                            }
                        });

                    }
                    public void initSuccess() {
                        NaviUtils.this.listener.initSuccess();
                        Logger.d(TAG, "百度导航引擎初始化成功");
//                        Toast.makeText(context, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                    }

                    public void initStart() {
                        NaviUtils.this.listener.initStart();
                        Logger.d(TAG, "百度导航引擎初始化开始");
//                        Toast.makeText(context, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    public void initFailed() {
                        NaviUtils.this.listener.initFailed();
                        Logger.d(TAG, "百度导航引擎初始化失败");
//                        Toast.makeText(context, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                }, null /*mTTSCallback*/);
    }


    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if ( mSDCardPath == null ) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if ( !f.exists() ) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;
        public DemoRoutePlanListener(BNRoutePlanNode node){
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            Intent intent = new Intent(context, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            context.startActivity(intent);
            myRoutePlanListener.onJumpToNavigator();
        }
        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            myRoutePlanListener.onRoutePlanFailed();
        }
    }

    public interface  MyRoutePlanListener{

        void onJumpToNavigator();
        void onRoutePlanFailed();
    }

    public interface MyNaviInitListener{
        void onAuthResult(int status, String msg);
        void initSuccess();
        void initStart();
        void initFailed();
    }
}
