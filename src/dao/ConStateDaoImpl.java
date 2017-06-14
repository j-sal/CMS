package com.ruanko.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ruanko.dao.ConStateDao;
import com.ruanko.model.ConState;
import com.ruanko.utils.AppException;
import com.ruanko.utils.DBUtil;

public class ConStateDaoImpl implements ConStateDao {
	
	public boolean add(ConState conState) throws AppException{	
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into t_contract_state(con_id,type) values(?,?)";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conState.getConId());
			psmt.setInt(2, conState.getType());
		
			int result = psmt.executeUpdate();
			
			if(result > 0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"com.ruanko.dao.impl.ContStateDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	public List<Integer> getConIdsByType(int type) throws AppException {
		List<Integer> conIds = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select con_id from t_contract_state where type=? and del=0";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, type);
			
			rs = psmt.executeQuery();

			while (rs.next()) {
				conIds.add(rs.getInt("con_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"com.ruanko.dao.impl.ConStateDaoImpl.getConIdsByType");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return conIds;
	}

	public ConState getConState(int conId, int type) throws AppException {
		ConState conState = null;

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select id,con_id,type,time "
					+"from t_contract_state "
					+"where con_id = ? and type = ? and del = 0";
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conId); 
			psmt.setInt(2, type);

			rs = psmt.executeQuery();
			if(rs.next()) {
				conState = new ConState();
				conState.setId(rs.getInt("id"));
				conState.setConId(rs.getInt("con_id"));
				conState.setType(rs.getInt("type"));
				conState.setTime(rs.getDate("time"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.dao.impl.ConStateDaoImpl.getByConId");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return conState;
	}
	
	public boolean isExist(int con_id, int type) throws AppException {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(id) as n from t_contract_state "
				 +"where con_id = ? and type = ? and del = 0";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, con_id);
			psmt.setInt(2, type);
			rs = psmt.executeQuery();
			rs.next();
			int n = rs.getInt("n"); 
			if (n > 0) {
				flag = true;  
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"com.ruanko.dao.impl.ConStateDaoImpl.isExist");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
}
