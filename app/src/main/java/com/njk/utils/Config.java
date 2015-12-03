package com.njk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.njk.Global;


public class Config {
	/**
	 * 保存搜索关键字记录
	 * @param context
	 * @param value
	 */
	public static void setSearchKeys(Context context, String value){
		setPreferences(context, Global.search_key, value);
	}

	/**
	 * 获取搜索关键字记录
	 * @param context
	 * @param context
	 */
	public static String getSearchKeys(Context context){
		return getStringPreferences(context,Global.search_key);
	}

	/**
	 * 保存用户当前纬度
	 * @param context
	 * @param value
	 */
	public static void setCurLng(Context context, String value){
		setPreferences(context, Global.cur_lng, value);
	}
	
	/**
	 * 获取用户当前纬度
	 * @param context
	 * @param context
	 */
	public static String getCurLng(Context context){
		return getStringPreferences(context,Global.cur_lng);
	}

	/**
	 * 保存用户当前纬度
	 * @param context
	 * @param value
	 */
	public static void setCurAddr(Context context, String value){
		setPreferences(context, Global.cur_addr, value);
	}

	/**
	 * 获取用户当前纬度
	 * @param context
	 * @param context
	 */
	public static String getCurAddr(Context context){
		return getStringPreferences(context,Global.cur_addr);
	}
	
	/**
	 * 保存用户当前经度
	 * @param context
	 * @param value
	 */
	public static void setCurLat(Context context, String value){
		setPreferences(context, Global.cur_lat, value);
	}
	
	/**
	 * 获取用户当前经度
	 * @param context
	 * @param context
	 */
	public static String getCurLat(Context context){
		return getStringPreferences(context,Global.cur_lat);
	}

	/**
	 * 保存用户mobile
	 * @param context
	 * @param value
	 */
	public static void setUserMobile(Context context, String value){
		setPreferences(context, Global.mobile, value);
	}

	/**
	 * 获取用户mobile
	 * @param context
	 * @param context
	 */
	public static String getUserMobile(Context context){
		return getStringPreferences(context,Global.mobile);
	}

	/**
	 * 保存用户token
	 * @param context
	 * @param value
	 */
	public static void setUserToken(Context context, String value){
		setPreferences(context, Global.token, value);
	}
	
	/**
	 * 获取用户token
	 * @param context
	 * @param context
	 */
	public static String getUserToken(Context context){
		return getStringPreferences(context,Global.token);
	}
	
	/**
	 * 保存用户id
	 * @param context
	 * @param value
	 */
	public static void setUserId(Context context, String value){
		setPreferences(context, Global.user_id, value);
	}
	
	/**
	 * 获取用户id
	 * @param context
	 * @param context
	 */
	public static String getUserId(Context context){
		return getStringPreferences(context,Global.user_id);
	}
	
	/**
	 * 设置更新城区列表的时间
	 * @param context
	 * @param context
	 */
	public static void setAreasData(Context context, String value){
		setPreferences(context, Global.areas_data, value);
	}
	
	/**
	 * 获取更新城区列表的时间
	 * @param context
	 * @param context
	 */
	public static String getAreasData(Context context){
		return getStringPreferences(context,Global.areas_data);
	}

	/**
	 * 设置更新城区列表的时间
	 * @param context
	 * @param context
	 */
	public static void setUpdateProvinceTime(Context context, String value){
		setPreferences(context, Global.update_province_time, value);
	}

	/**
	 * 获取更新城区列表的时间
	 * @param context
	 * @param context
	 */
	public static String getUpdateProvinceTime(Context context){
		return getStringPreferences(context,Global.update_province_time);
	}
	
	/**
	 * 设置定位城市
	 * @param context
	 * @param value
	 */
	public static void setLocationCity(Context context, String value){
		setPreferences(context, Global.location_city, value);
	}
	/**
	 * 获取定位城市
	 * @param context
	 * @return
	 */
	public static String getLocationCity(Context context){
		return getStringPreferences(context,Global.location_city);
	}
	
	/**
	 * 设置当前城市
	 * setLocationCity() 
	 * @param context
	 * @param value  
	 * @return void
	 * @author liujunbin
	 */
	public static void setCurrCity(Context context, String value){
		setPreferences(context, Global.curr_city, value);
	}
	/**
	 * 获取本地保存当前城市
	 * getLocationCity() 
	 * @param context  
	 * @return void
	 * @author liujunbin
	 */
	public static String getCurrCity(Context context){
		return getStringPreferences(context,Global.curr_city);
	}
	
	/**
	 * 设置当前城市id
	 * setLocationCity() 
	 * @param context
	 * @param value  
	 * @return void
	 * @author liujunbin
	 */
	public static void setCurrCityId(Context context, String value){
		setPreferences(context, Global.curr_city_id, value);
	}
	/**
	 * 获取本地保存当前城市id
	 * getLocationCity() 
	 * @param context  
	 * @return void
	 * @author liujunbin
	 */
	public static String getCurrCityId(Context context){
		return getStringPreferences(context,Global.curr_city_id);
	}
	
	/**
	 * 设置是否显示引导页面
	 * setHideGuided() 
	 * @param context
	 * @param value  
	 * @return void
	 * @author liujunbin
	 */
	public static void setHideGuided(Context context, boolean value){
		setPreferences(context, Global.hide_guide, value);
	}
	/**
	 * 是否显示引导页面
	 * isHideGuided() 
	 * @param context  
	 * @return void
	 * @author liujunbin
	 */
	public static boolean isHideGuided(Context context){
		return getBooleanPreferences(context,Global.hide_guide);
	}
	
	
	/************************************************************************************/

	/**
	 * 保存设置（String）
	 * @param context
	 * @param key
	 * @param value
	 */
    public static void setPreferences(Context context, String key, String value){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	Editor editor = sp.edit();
    	editor.putString(key, value);
    	editor.commit();
    }
    
    /**
     * 读取设置（String）
     * @param context
     * @param key
     * @return
     */
    public static String getStringPreferences(Context context, String key){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	return sp.getString(key, "");
    }
    
    /**
	 * 保存设置（Boolean）
	 * @param context
	 * @param key
	 * @param value
	 */
    public static void setPreferences(Context context, String key, boolean value){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	Editor editor = sp.edit();    	
    	editor.putBoolean(key, value);
    	editor.commit();
    }
    
    /**
     * 读取设置（Boolean）
     * @param context
     * @param key
     * @return
     */
    public static boolean getBooleanPreferences(Context context, String key){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);    	
    	return sp.getBoolean(key, false);
    }
    
    /**
	 * 保存设置（Integer）
	 * @param context
	 * @param key
	 * @param value
	 */
    public static void setPreferences(Context context, String key, int value){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	Editor editor = sp.edit();
    	editor.putInt(key, value);
    	editor.commit();
    }
    
    /**
     * 读取设置（Integer）
     * @param context
     * @param key
     * @return
     */
    public static int getIntPreferences(Context context, String key){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	return sp.getInt(key, -1);
    }
    
    /**
	 * 保存设置（Long）
	 * @param context
	 * @param key
	 * @param value
	 */
    public static void setPreferences(Context context, String key, long value){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	Editor editor = sp.edit();
    	editor.putLong(key, value);
    	editor.commit();
    }
    
    /**
     * 读取设置（Long）
     * @param context
     * @param key
     * @return
     */
    public static long getLongPreferences(Context context, String key){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	return sp.getLong(key, -1);
    }
    
    /**
	 * 保存设置（Float）
	 * @param context
	 * @param key
	 * @param value
	 */
    public static void setPreferences(Context context, String key, float value){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	Editor editor = sp.edit();
    	editor.putFloat(key, value);
    	editor.commit();
    }
    
    /**
     * 读取设置（Float）
     * @param context
     * @param key
     * @return
     */
    public static float getFloatPreferences(Context context, String key){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	return sp.getFloat(key, -1);
    }
}
