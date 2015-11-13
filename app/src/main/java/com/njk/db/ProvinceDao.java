package com.njk.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.njk.bean.CityBean;
import com.njk.bean.ProvinceBean;
import com.nostra13.universalimageloader.utils.L;

import java.sql.SQLException;
import java.util.List;

public class ProvinceDao {
	private Context context;
	private Dao<ProvinceBean, Integer> provinceDao;
	public ProvinceDao(Context context) {
		this.context = context;
		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(context);
			provinceDao = helper.getDao(ProvinceBean.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add(ProvinceBean bean) {
		try {
			provinceDao.create(bean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(ProvinceBean bean) {
		try {
			provinceDao.update(bean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try {
			provinceDao.deleteById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ProvinceBean queryById(int id){
		ProvinceBean provinceBean = null;
		try {
			provinceBean = provinceDao.queryForId(id);
			if (provinceBean.getCitys() != null)
				for (CityBean bean : provinceBean.getCitys())
				{
					Log.e("TAG",bean.toString());
				}
			
			return provinceDao.queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<ProvinceBean> queryForAll() {
		List<ProvinceBean> beans = null;
		try {
			beans = provinceDao.queryForAll();
			Log.e("TAG", beans.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}

	public ProvinceBean queryByName(String name) {
		// TODO Auto-generated method stub
		ProvinceBean provinceBean= null;
		try {
			List<ProvinceBean> provinceBeans = provinceDao.queryBuilder().where().eq("name", name).query();
			if(provinceBeans!=null && provinceBeans.size()>0){
				provinceBean = provinceBeans.get(0);
				if (provinceBean.getCitys() != null)
					for (CityBean bean : provinceBean.getCitys())
					{
						L.e(bean.toString());
					}
				
				return provinceBean;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
