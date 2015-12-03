package com.njk.category;

import java.io.Serializable;
import java.util.List;

public class CategoryGroup implements Serializable {

	private List<CategoryBean> categoryListData;
	private CategoryBean tmpCategory;
	private CategoryBean tmpSubCategory;
	private CategoryBean currCategory;

	public CategoryGroup(List<CategoryBean> listData) {
		this.categoryListData = listData;
	}

	public List<CategoryBean> getCategoryListData() {
		return categoryListData;
	}

	public void setCategoryListData(List<CategoryBean> categoryListData) {
		this.categoryListData = categoryListData;
	}

	public CategoryBean getTmpCategory() {
		return tmpCategory;
	}

	public void setTmpCategory(CategoryBean tmpCategory) {
		this.tmpCategory = tmpCategory;
	}

	public CategoryBean getTmpSubCategory() {
		return tmpSubCategory;
	}

	public void setTmpSubCategory(CategoryBean tmpSubCategory) {
		this.tmpSubCategory = tmpSubCategory;
	}
	public CategoryBean getCurrCategory() {
		return currCategory;
	}

	public void setCurrCategory(CategoryBean currCategory) {
		this.currCategory = currCategory;
	}
	
	public void clear(){

		if(categoryListData.size()>0){
			CategoryBean b = new CategoryBean();
			CategoryBean b2 = categoryListData.get(0);
			b.subList = b2.subList;
			b.name = b2.name;
			b.id = b2.id;
			b.leve = b2.leve;
			categoryListData.clear();

			categoryListData.add(b);
			tmpCategory = b;
			tmpSubCategory = b;

		}
	}
}
