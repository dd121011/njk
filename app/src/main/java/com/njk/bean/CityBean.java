package com.njk.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_city")
public class CityBean implements Serializable {
//	id: "186",
//	name: "丰台区"
	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	public int id;
	@DatabaseField(columnName = "name")
	public String name;
	@DatabaseField(canBeNull = true, foreign = true, columnName = "province_id", foreignAutoRefresh = true)
	public ProvinceBean province;
	
	
	public CityBean() {}
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
	public ProvinceBean getProvince() {
		return province;
	}
	public void setProvince(ProvinceBean province) {
		this.province = province;
	}
	@Override
	public String toString() {
		return "CityBean [id=" + id + ", name=" + name + ", province=" + province + "]";
	}
	
}
