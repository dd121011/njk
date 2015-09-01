package com.njk.db;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.util.Log;

public class UserDao {
	private Context context;
	private Dao<User, Integer> userDao;
	public UserDao(Context context) {
		this.context = context;
		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(context);
			userDao = helper.getDao(User.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add(User user) {
		try {
			User u1 = new User();
			u1.setName("2B青年0");
			userDao.create(u1);
			u1 = new User();
			u1.setName("2B青年1");
			userDao.create(u1);
			u1 = new User();
			u1.setName("2B青年2");
			userDao.create(u1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(User user) {
		try {
			user = new User();
			user.setName("xyz");
			user.setId(1);
			userDao.update(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try {
			userDao.deleteById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<User> queryForAll() {
		List<User> users = null;
		try {
			users = userDao.queryForAll();
			Log.e("TAG", users.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
}
