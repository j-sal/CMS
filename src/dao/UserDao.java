package com.ruanko.dao;
import java.util.List;
import com.ruanko.model.User;
import com.ruanko.utils.AppException;


public interface UserDao {
	public boolean isExist(String name) throws AppException;
	public boolean add(User user) throws AppException;
	public int login(String name,String password) throws AppException;
	public User getById(int id) throws AppException;
	public List<Integer> getIds() throws AppException;
}
