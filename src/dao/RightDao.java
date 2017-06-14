package com.ruanko.dao;
import java.util.List;
import com.ruanko.utils.AppException;
import com.ruanko.model.Right;


public interface RightDao {
	public int getRoleIdByUserId(int userId) throws AppException;
	public List<Integer> getUserIdsByRoleId(int roleId) throws AppException;
	public int getIdByUserId(int userId) throws AppException;
	public boolean updateById(Right right) throws AppException;
	public boolean add(Right right) throws AppException;
}
