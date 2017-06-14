package com.ruanko.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ruanko.dao.RoleDao;
import com.ruanko.model.Role;
import com.ruanko.utils.AppException;
import com.ruanko.utils.DBUtil;

public class RoleDaoImpl implements RoleDao {
	public Role getById(int id) throws AppException {
		Role role = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {

			conn = DBUtil.getConnection();
			String sql = "select id,name,description,function_ids "
					+"from t_role "
					+"where id = ? and del = 0";
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, id);
			rs = psmt.executeQuery();
			 
			if (rs.next()) {
				role = new Role(); 
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setDescription(rs.getString("description"));
				role.setFuncIds(rs.getString("function_ids"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.dao.impl.RoleDaoImpl.getById");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return role;
	}
	
	public List<Role> getAll() throws AppException {
		List<Role> roleList = new ArrayList<Role>();
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select id,name,description,function_ids from t_role where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
			while (rs.next()) {
				Role role = new Role(); 
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setDescription(rs.getString("description"));
				role.setFuncIds(rs.getString("function_ids"));
				
				roleList.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.dao.impl.RoleDaoImpl.getAll");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return roleList;
	}
}
