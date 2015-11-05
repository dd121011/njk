package com.njk.manager;

import android.content.Context;
import android.text.TextUtils;

import com.njk.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
	// Private constructor prevents instantiation from other classes
    private UserManager() {
    	changeUserStateListeners = new ArrayList<OnChangerUserStateListener>();
    }

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class UserManagerHolder {
            public static final UserManager INSTANCE = new UserManager();
    }

    public static UserManager getInstance() {
            return UserManagerHolder.INSTANCE;
    }
    
    
    private List<OnChangerUserStateListener> changeUserStateListeners = null;


    public void userLoginSuccees(Context context){
        for(OnChangerUserStateListener listenre : changeUserStateListeners){
            listenre.onLoginSuccees();
        }
    }

    public boolean getUserLoginState(Context context){
        boolean isLogin = false;
        if(!TextUtils.isEmpty(Config.getUserId(context)) && !TextUtils.isEmpty(Config.getUserToken(context))){
            isLogin = true;
        }
        return isLogin;
    }

    public void exitUser(Context context){
        Config.setUserId(context,null);
        Config.setUserToken(context, null);
        for(OnChangerUserStateListener listenre : changeUserStateListeners){
            listenre.onExitUser();
        }
    }

    public void unRegisterUserStateChangerListener(OnChangerUserStateListener listener){
        if(changeUserStateListeners.contains(listener)){
            changeUserStateListeners.remove(listener);
        }
    }
    public void registerUserStateChangerListener(OnChangerUserStateListener listener){
    	changeUserStateListeners.add(listener);
    }
    
    public interface OnChangerUserStateListener{
    	void onLoginSuccees();
        void onExitUser();
    }
}
