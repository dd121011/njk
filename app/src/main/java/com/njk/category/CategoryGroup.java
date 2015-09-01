package com.njk.category;

import java.io.Serializable;
import java.util.List;

public class CategoryGroup implements Serializable {

	private List<CategoryBean> categoryListData;
	private CategoryBean tmpCategory;
	private CategoryBean tmpSubCategory;
	
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
	
	
}
