package com.ruanko.dao;
import java.util.List;
import com.ruanko.model.Role;
import com.ruanko.utils.AppException;

public interface RoleDao {
	public Role getById(int id) throws AppException;
	public List<Role> getAll() throws AppException;
}
