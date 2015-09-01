package com.njk.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.njk.bean.CityBean;

import java.sql.SQLException;
import java.util.List;

public class CityDao {
	private Context context;
	private Dao<CityBean, Integer> beanDao;
	public CityDao(Context context) {
		this.context = context;
		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(context);
			beanDao = helper.getDao(CityBean.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add(CityBean bean) {
		try {
			int i = beanDao.create(bean);
			L.d("add index ====="+i);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(CityBean bean) {
		try {
			beanDao.update(bean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try {
			beanDao.deleteById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<CityBean> queryForAll() {
		List<CityBean> beans = null;
		try {
			beans = beanDao.queryForAll();
			Log.e("TAG", beans.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	public List<CityBean> listByUserId(int province_id)
	{
		try
		{
			return beanDao.queryBuilder().where().eq("province_id", province_id)
					.query();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
