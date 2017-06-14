package com.ruanko.dao;
import java.util.List;
import com.ruanko.model.ConProcess;
import com.ruanko.utils.AppException;

public interface ConProcessDao {
	public boolean isExist(int conId) throws AppException;
	public boolean add(ConProcess conProcess) throws AppException;
	public List<Integer> getConIds(ConProcess conProcess) throws AppException;
	public boolean update(ConProcess conProcess) throws AppException;
	public int getTotalCount(ConProcess conProcess) throws AppException;
	public List<Integer> getIds(int conId, int type, int state) throws AppException;
	public ConProcess getById(int id) throws AppException;
}
