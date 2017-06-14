package com.ruanko.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ruanko.dao.RightDao;
import com.ruanko.model.Right;
import com.ruanko.utils.AppException;
import com.ruanko.utils.DBUtil;

public class RightDaoImpl implements RightDao {
	public int getRoleIdByUserId(int userId) throws AppException {
		int roleId = -1; 
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select role_id "
					+"from t_right "
					+"where user_id = ? and del = 0";
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, userId);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				roleId = rs.getInt("role_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.dao.impl.RightDaoImpl.getRoleIdByUserId");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return roleId;
	}
	
	public List<Integer> getUserIdsByRoleId(int roleId) throws AppException  {
		List<Integer> userIds = new ArrayList<Integer>();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select user_id from t_right where role_id = ? and del = 0";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, roleId);
			
			rs = psmt.executeQuery();
			while (rs.next()) {
				userIds.add(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.dao.impl.RightDaoImpl.getUserIdsByRoleId");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return userIds;
	}
	public int getIdByUserId(int userId) throws AppException {
		int id = -1; 
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select id "
					+"from t_right "
					+"where user_id = ? and del = 0";
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, userId);
			rs = psmt.executeQuery();

			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.dao.impl.RightDaoImpl.getIdByUserId");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return id;
	}
	
	public boolean updateById(Right right) throws AppException {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "update t_right set user_id = ?, role_id = ?, description = ? " 
					+"where id = ? and del = 0";

			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, right.getUserId());
			psmt.setInt(2, right.getRoleId());
			psmt.setString(3, right.getDescription());
			psmt.setInt(4, right.getId());
			
			int count = psmt.executeUpdate();
			
			if (count > 0) {
				flag = true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.dao.impl.RightDaoImpl.updateById");
		} finally {
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}

	public boolean add(Right right) throws AppException {
		boolean flag = false;
		Connection conn = null; 
		PreparedStatement psmt = null;
		
		int result = -1;
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into t_right (user_id,role_id,description)"
					+ " values (?,?,?)";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, right.getUserId());
			psmt.setInt(2, right.getRoleId());
			psmt.setString(3, right.getDescription());

			result = psmt.executeUpdate();
			if (result > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.dao.impl.RightDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
}
