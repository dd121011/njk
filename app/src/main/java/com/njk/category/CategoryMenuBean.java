package com.njk.category;

public class CategoryMenuBean {
	public int index;
	public boolean isSelected;
	public String title;
	private CategoryGroup categoryGroup;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public CategoryGroup getCategoryGroup() {
		return categoryGroup;
	}
	public void setCategoryGroup(CategoryGroup categoryGroup) {
		this.categoryGroup = categoryGroup;
	}
	@Override
	public String toString() {
		return "CategoryMenuBean [index=" + index + ", isSelected=" + isSelected + ", title=" + title + "]";
	}	
	
}
