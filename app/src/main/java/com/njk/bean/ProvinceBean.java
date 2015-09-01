package com.njk.bean;

import java.io.Serializable;
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_province")
public class ProvinceBean implements Serializable {
	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	public int id;
	@DatabaseField(columnName = "name")
	public String name;
	@ForeignCollectionField
	private Collection<CityBean> citys;
	
	
	public ProvinceBean() {}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<CityBean> getCitys() {
		return citys;
	}
	public void setCitys(Collection<CityBean> citys) {
		this.citys = citys;
	}
	@Override
	public String toString() {
		return "ProvinceBean [id=" + id + ", name=" + name + ", citys=" + citys + "]";
	}
}
