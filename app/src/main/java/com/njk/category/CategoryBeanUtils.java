package com.njk.category;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njk.bean.AreasBean;
import com.njk.bean.AreasBean.ScenciImp;
import com.njk.bean.AreasBean.City;
import com.njk.bean.GetscenicBean;
import com.njk.utils.Config;
import com.njk.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CategoryBeanUtils {
   public static List<CategoryBean> getTestDada(){
	   List<CategoryBean> list = new ArrayList<CategoryBean>();
	   for(int i=0;i<20;i++){
		   CategoryBean item = new CategoryBean();
		   item.id = i+"";
		   item.name = i+"北京";
		   item.leve = 1;
		   
		   List<CategoryBean> subList = new ArrayList<CategoryBean>();
		   for(int j=0;j<5;j++){
			   CategoryBean subItem = new CategoryBean();
			   subItem.id = j+"";
			   subItem.name = j+"北京sub";
			   subItem.leve = 1;
			   
			   subList.add(subItem);
		   }
		   item.subList = subList;
		   list.add(item);
	   }
	   return list;
   }
   
   public static List<CategoryBean> getListDada(){
	   List<CategoryBean> list = new ArrayList<CategoryBean>();
	   for(int i=0;i<20;i++){
		   CategoryBean item = new CategoryBean();
		   item.id = i+"";
		   item.name = i+"北京";
		   item.leve = 1;
		   
		   List<CategoryBean> subList = new ArrayList<CategoryBean>();
		   for(int j=0;j<5;j++){
			   CategoryBean subItem = new CategoryBean();
			   subItem.id = j+"";
			   subItem.name = j+"北京sub";
			   subItem.leve = 1;
			   
			   subList.add(subItem);
		   }
		   item.subList = subList;
		   list.add(item);
	   }
	   return list;
   }
   
   public static List<CategoryBean> getscenicToCategory(ArrayList<GetscenicBean> dataList){
	   
	   List<CategoryBean> list = null;
	   if(dataList!=null){
		   list = new ArrayList<CategoryBean>();
		   for(GetscenicBean item : dataList){
			   CategoryBean categoryBean = new CategoryBean();
			   categoryBean.id = item.id;
			   categoryBean.name = item.title;
			   list.add(categoryBean);
		   }
	   }
	   
	   return list;
   }

	public static List<CategoryBean> getCategorysFromAreas(Context context,String  currCity){
		List<CategoryBean> list = new ArrayList<>();
		String dataObj = Config.getAreasData(context);
		Gson gson = new Gson();
		AreasBean areasBean = gson.fromJson(dataObj.toString(), new TypeToken<AreasBean>() {
		}.getType());
		TreeMap<String,AreasBean.Items> province = areasBean.province;

		for (Map.Entry<String,AreasBean.Items> entry : province.entrySet()) {
			Logger.e("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			String key = entry.getKey();
			AreasBean.Items items = entry.getValue();
			for (AreasBean.Item item: items.items){
				if(currCity.equals(item.name) && item.citys!=null){
					for (City city: item.citys){
						CategoryBean categoryBean = new CategoryBean();
						categoryBean.id = city.id;
						categoryBean.name = city.name;
						if(city.scenic!=null && city.scenic.size()>0){

							List<CategoryBean> subList = new ArrayList<>();
							for (ScenciImp scenic : city.scenic){
								CategoryBean subBean = new CategoryBean();
								subBean.name = scenic.title;
								subBean.id = scenic.id;
								subList.add(subBean);
							}

							categoryBean.subList = subList;

							list.add(categoryBean);
						}

					}
				}
			}
		}

		return list;
	}
}
