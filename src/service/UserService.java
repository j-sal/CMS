package com.ruanko.service;


import com.ruanko.model.User;
import com.ruanko.utils.AppException;
import com.ruanko.dao.UserDao;
import com.ruanko.dao.impl.UserDaoImpl;
import com.ruanko.dao.RightDao;
import com.ruanko.dao.RoleDao;
import com.ruanko.dao.impl.RightDaoImpl;
import com.ruanko.dao.impl.RoleDaoImpl;
import com.ruanko.model.Right;
import com.ruanko.model.Role;
import com.ruanko.model.PermissionBusiModel;

import java.util.ArrayList;
import java.util.List;

public class UserService {
	private UserDao userDao = null;
	private RoleDao roleDao = null;
	private RightDao rightDao = null;
	
	public UserService() {
		userDao = new UserDaoImpl();
		roleDao = new RoleDaoImpl();
		rightDao = new RightDaoImpl();
	}
	
	public boolean register(User user) throws AppException {
		boolean flag = false;
		
		try {
			if (!userDao.isExist(user.getName())) {
				flag = userDao.add(user);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.UserService.register");
		}
		return flag;
	}
	public int login(String name, String password) throws AppException {
		int userId = -1; 
		try {
			userId = userDao.login(name, password); 
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.UserService.login");
		}
		return userId;
	}
	
	public Role getUserRole(int userId) throws AppException {	
		Role role = null;
		int roleId = -1; 
		try {
			roleId = rightDao.getRoleIdByUserId(userId);
			if(roleId > 0){
				role = roleDao.getById(roleId); 
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.UserService.getUserRole");
		}
		return role;
	}
	public List<User> getUserListByRoleId(int roleId) throws AppException {
		List<User> userList = new ArrayList<User>();
		List<Integer> userIds = null; 
		
		try {
			userIds = rightDao.getUserIdsByRoleId(roleId);

			for (int userId : userIds) {
				User user = userDao.getById(userId);
				if (user != null) {
					userList.add(user); 
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.UserService.getUserList");
		}	
		return userList;
	}
	
	public List<PermissionBusiModel> getYhqxList() throws AppException {
		// Initialize permissionList
		List<PermissionBusiModel> permissionList = new ArrayList<PermissionBusiModel>();
		// Declare userIds
		List<Integer> userIds = null; 
		
		try {
			userIds = userDao.getIds();

			for (int userId : userIds) {

				PermissionBusiModel permission = new PermissionBusiModel();
				
				User user = userDao.getById(userId); 
				int roleId = -1;
				roleId = rightDao.getRoleIdByUserId(userId); 
				
				if (roleId > 0) {
					Role role = roleDao.getById(roleId);
					permission.setRoleId(roleId);
					permission.setRoleName(role.getName());
				}
				
				permission.setUserId(userId);
				permission.setUserName(user.getName());
				
				permissionList.add(permission);
			}
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.UserService.getYhqxList");
		}	
		return permissionList;
	}
	
	public List<Role> getRoleList() throws AppException {	
		List<Role> roleList = new ArrayList<Role>();
		
		try {
			roleList = roleDao.getAll();
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.UserService.getRoleList");
		}
		return roleList;
	}
	
	public boolean assignPermission(Right right) throws AppException {
		boolean flag = false;
		
		try {
			int roleId = -1; 
			roleId = rightDao.getRoleIdByUserId(right.getUserId());
			Role role = null;
			if (roleId > 0) {
				role = roleDao.getById(roleId);
			}

			if (role != null) {
				int rightId = rightDao.getIdByUserId(right.getUserId());
				right.setId(rightId);
				right.setDescription("update");
				flag = rightDao.updateById(right);
			} else {
				flag = rightDao.add(right);
			}
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.UserService.assignPermission");
		}
		return flag;
	}
}
