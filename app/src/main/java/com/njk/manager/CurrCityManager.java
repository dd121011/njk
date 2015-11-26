package com.njk.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.R;
import com.njk.bean.AreasBean;
import com.njk.bean.AreasBean.Item;
import com.njk.bean.AreasBean.Items;
import com.njk.pinnedheaderlistView.City;
import com.njk.utils.Config;
import com.njk.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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


    public List<City> getHotCityList(Context context){
        return hotCityList;
    }

    public List<City> getCityList(Context context){
        return cityList;
    }

    private  List<City> cityList = new ArrayList<>();
    private  List<City> hotCityList = new ArrayList<>();
    public void initCityData(Context context){
        String[] hotCityArr = context.getResources().getStringArray(R.array.hot_city);
        List<String> hotlist = Arrays.asList(hotCityArr);
        String dataObj = Config.getAreasData(context);
        Gson gson = new Gson();
        AreasBean areasBean = gson.fromJson(dataObj.toString(), new TypeToken<AreasBean>() {
        }.getType());
        TreeMap<String,Items> province = areasBean.province;

        for (Map.Entry<String,Items> entry : province.entrySet()) {
            Logger.e("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            String key = entry.getKey();
            Items items = entry.getValue();
            for (Item item: items.items){
                City city = new City();
                city.setId(item.id);
                city.setName(item.name);
                city.setPyf(key);
                city.setPys(key);
                cityList.add(city);
                if(hotlist.contains(item.name)){
                    hotCityList.add(clone(city));
                }
            }
        }
    }

    public City clone(City city){

        City c = new City();
        c.setPys(city.getPys());
        c.setPyf(city.getPyf());
        c.setName(city.getName());
        c.setId(city.getId());
        return c;
    }

    public void setCurrCity(Context context, City city){
        Config.setCurrCity(context, city.getName());
        Config.setCurrCityId(context, city.getId());
        for(OnChangerCurrCityListener listenre : changeCurrCityListeners){
            listenre.onChangeCurrCity(city.getName());
        }
    }

    public void setCurrCity(Context context, String currCity){
    	Config.setCurrCity(context, currCity);
    	for(OnChangerCurrCityListener listenre : changeCurrCityListeners){
    		listenre.onChangeCurrCity(currCity);
    	}
    }

    public City findCity(Context context, String name){
        City city = null;
        if(!TextUtils.isEmpty(name)){
            for (City c : cityList){
                if(name.equals(c.getName())){
                    city = c;
                }
            }
            if(city == null && cityList.size()>0){
                city = cityList.get(0);
            }
        }
        return city;
    }

    public City getCurrCity(Context context){
        City city = null;
        String cityName = Config.getCurrCity(context);
        String cityId = Config.getCurrCityId(context);
        if(!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(cityId)){
            city = new City();
            city.setName(cityName);
            city.setId(cityId);
        }
        return city;
    }
    
    public void registerChangerCurrCityListener(OnChangerCurrCityListener listener){
    	changeCurrCityListeners.add(listener);
    }
    
    public interface OnChangerCurrCityListener{
    	void onChangeCurrCity(String currCity);
    }
}
