package com.ruanko.dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ruanko.dao.UserDao;
import com.ruanko.model.User;
import com.ruanko.utils.AppException;
import com.ruanko.utils.DBUtil;


public class UserDaoImpl implements UserDao {
	public boolean isExist(String name) throws AppException {
		Connection conn = null; 
		PreparedStatement psmt = null;
		ResultSet rs = null;

		boolean flag = false;
		try {
			conn = DBUtil.getConnection();
			String sql = "select id from t_user where name = ? and del = 0";

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, name);

			rs = psmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.dao.impl.UserDaoImpl.isExist");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	public boolean add(User user) throws AppException {
		Connection conn = null; 
		PreparedStatement psmt = null;
		
		boolean flag = false;
		int result = -1;
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into t_user (name,password)"
					+ " values (?,?)";
			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, user.getName());
			psmt.setString(2, user.getPassword());

			result = psmt.executeUpdate();
			if (result > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.dao.impl.UserDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	public int login(String name, String password) throws AppException {
		int userId = -1; 
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			conn = DBUtil.getConnection();
			String sql = "select id from t_user where name = ? and password = ? and del = 0";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, name);
			psmt.setString(2, password);
			rs = psmt.executeQuery();
			if (rs.next()) {
				userId = rs.getInt("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.dao.impl.UserDaoImpl.login");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return userId;
	}
	
	public User getById(int id) throws AppException {
		User user = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select id,name,password "
					+"from t_user "
					+"where id = ? and del = 0";
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, id);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				user = new User(); 
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.dao.impl.UserDaoImpl.getById");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return user;
	}
	
	public List<Integer> getIds() throws AppException {
		List<Integer> ids = new ArrayList<Integer>();
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select id from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.dao.impl.UserDaoImpl.getIds");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return ids;
	}

}
