package com.ruanko.dao;
import java.util.List;
import com.ruanko.model.ConState;
import com.ruanko.utils.AppException;


public interface ConStateDao {
	public boolean add(ConState conState) throws AppException;
	public List<Integer> getConIdsByType(int type) throws AppException;
	public ConState getConState(int conId, int type) throws AppException;
	public boolean isExist(int con_id, int type) throws AppException;
}
