package com.njk.category;

import com.njk.bean.GetscenicBean;

import java.util.ArrayList;
import java.util.List;

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
}
