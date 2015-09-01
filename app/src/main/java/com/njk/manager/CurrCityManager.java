package com.njk.manager;

import android.content.Context;

import com.njk.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class CurrCityManager {
	// Private constructor prevents instantiation from other classes
    private CurrCityManager() { 
    	changeCurrCityListeners = new ArrayList<OnChangerCurrCityListener>();
    }

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class CurrCityHolder { 
            public static final CurrCityManager INSTANCE = new CurrCityManager();
    }

    public static CurrCityManager getInstance() {
            return CurrCityHolder.INSTANCE;
    }
    
    
    private List<OnChangerCurrCityListener> changeCurrCityListeners = null;
    
    public void setCurrCity(Context context, String currCity){
    	Config.setCurrCity(context,currCity);
    	for(OnChangerCurrCityListener listenre : changeCurrCityListeners){
    		listenre.onChangeCurrCity(currCity);
    	}
    }
    
    public void registerChangerCurrCityListener(OnChangerCurrCityListener listener){
    	changeCurrCityListeners.add(listener);
    }
    
    public interface OnChangerCurrCityListener{
    	void onChangeCurrCity(String currCity);
    }
}
