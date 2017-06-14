package com.ruanko.dao;

import java.util.List;
import com.ruanko.model.Contract;
import com.ruanko.utils.AppException;

public interface ContractDao {
	public boolean add(Contract contract) throws AppException;
	public Contract getById(int id) throws AppException;
	public List<Integer> getIdsByUserId(int userId) throws AppException;
	public boolean updateById(Contract contract) throws AppException;
}
