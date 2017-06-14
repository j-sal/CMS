package com.ruanko.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.ruanko.dao.ConStateDao;
import com.ruanko.dao.ContractDao;
import com.ruanko.dao.ConProcessDao;
import com.ruanko.dao.UserDao;
import com.ruanko.dao.impl.UserDaoImpl;
import com.ruanko.dao.impl.ConStateDaoImpl;
import com.ruanko.dao.impl.ContractDaoImpl;
import com.ruanko.dao.impl.ConProcessDaoImpl;
import com.ruanko.model.CSignatureOpinion;
import com.ruanko.model.ConState;
import com.ruanko.model.Contract;
import com.ruanko.model.ConBusiModel;
import com.ruanko.model.ConDetailBusiModel;
import com.ruanko.model.ConProcess;
import com.ruanko.model.User;
import com.ruanko.utils.AppException;
import com.ruanko.utils.Constant;

public class ContractService {
	private ContractDao contractDao = null;
	private ConStateDao conStateDao = null;
	private ConProcessDao conProcessDao = null;
	private UserDao userDao = null;

	public ContractService() {
		contractDao = new ContractDaoImpl();
		conStateDao = new ConStateDaoImpl();
		conProcessDao = new ConProcessDaoImpl();
		userDao = new UserDaoImpl();
	}
	

	public boolean draft(Contract contract) throws AppException {
		boolean flag = false;

		contract.setNum(generateConNum());
		
		try {
			if (contractDao.add(contract)) {
				ConState conState = new ConState();
				conState.setConId(contract.getId());  
				conState.setType(Constant.STATE_DRAFTED);
				flag = conStateDao.add(conState);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.draft");
		}
		return flag;
	}
	
	public List<ConBusiModel> getDfphtList() throws AppException {
		List<ConBusiModel> contractList = new ArrayList<ConBusiModel>();
	
		try {

			List<Integer> conIds = conStateDao.getConIdsByType(Constant.STATE_DRAFTED);
			for (int conId : conIds) {
				if (!conProcessDao.isExist(conId)) {
					Contract contract = contractDao.getById(conId);
					ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
					ConBusiModel conBusiModel = new ConBusiModel();
					if (contract != null) {
						conBusiModel.setConId(contract.getId());
						conBusiModel.setConName(contract.getName());
					}
					if (conState != null) {
						conBusiModel.setDrafTime(conState.getTime()); 
					}
					contractList.add(conBusiModel); // Add conBusiModel to contractList
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.ContractService.getDfphtList");
		}
		return contractList;
	}
	
	
	public Contract getContract(int id) throws AppException {
		Contract contract = null;
		
		try {
			contract = contractDao.getById(id);
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.getContract");
		}
		return contract;
	}
	
	public boolean distribute(int conId, int userId, int type)
			throws AppException {
		boolean flag = false;
		try {
			ConProcess conProcess = new ConProcess();
			conProcess.setConId(conId);
			conProcess.setType(type);
			conProcess.setState(Constant.UNDONE);
			conProcess.setUserId(userId);
			flag = conProcessDao.add(conProcess);
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.distribute");
		}
		return flag;
	}
	
	public List<ConBusiModel> getDhqhtList(int userId) throws AppException {
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		ConProcess conProcess = new ConProcess();
		conProcess.setUserId(userId);
		conProcess.setType(Constant.PROCESS_CSIGN);
		conProcess.setState(Constant.UNDONE);
		try {
			List<Integer> conIds = conProcessDao.getConIds(conProcess);
			for (int conId : conIds) {
				Contract contract = contractDao.getById(conId);
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				if (conState != null) {
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.ContractService.getDhqhtList");
		}
		return conList;
	}

	public boolean counterSign(ConProcess conProcess) throws AppException {
		boolean flag = false;
		conProcess.setType(Constant.PROCESS_CSIGN);
		conProcess.setState(Constant.DONE);
		
		try {
			if (conProcessDao.update(conProcess)) { 
				conProcess.setState(Constant.UNDONE);
				int totalCount = conProcessDao.getTotalCount(conProcess);				
				if (totalCount == 0) {
					ConState conState = new ConState();
					conState.setConId(conProcess.getConId());
					conState.setType(Constant.STATE_CSIGNED);
					flag = conStateDao.add(conState);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.counterSign");
		}
		return flag;
	}
	
	public ConDetailBusiModel getContractDetail(int id) throws AppException {
		ConDetailBusiModel conDetailBusiModel = null;
		try {
			Contract contract = contractDao.getById(id);
			User user = userDao.getById(contract.getUserId());

			conDetailBusiModel = new ConDetailBusiModel();
			conDetailBusiModel.setId(contract.getId());
			conDetailBusiModel.setNum(contract.getNum());
			conDetailBusiModel.setName(contract.getName());
			conDetailBusiModel.setCustomer(contract.getCustomer());
			conDetailBusiModel.setBeginTime(contract.getBeginTime());
			conDetailBusiModel.setEndTime(contract.getEndTime());
			conDetailBusiModel.setContent(contract.getContent());
			conDetailBusiModel.setDraftsman(user.getName());
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.getContractDetail");
		}
		return conDetailBusiModel;
	}
	
	public List<ConBusiModel> getDdghtList(int userId) throws AppException {
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		List<Integer> conIds = new ArrayList<Integer>();
		
		try {

			List<Integer> drafConIds = contractDao.getIdsByUserId(userId);

			for (int dConId : drafConIds) {
				if (conStateDao.isExist(dConId, Constant.STATE_CSIGNED)
						&& !conStateDao.isExist(dConId,Constant.STATE_FINALIZED)) {
					conIds.add(dConId);
				}
			}
			
			for (int conId : conIds) {
				Contract contract = contractDao.getById(conId);
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				if (conState != null) {
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.ContractService.getDdghtList");
		}
		return conList;
	}

	public boolean finalize(Contract contract) throws AppException {
		boolean flag = false; 

		try {
			if (contractDao.updateById(contract)) {
				ConState conState = new ConState();

				conState.setConId(contract.getId());
				conState.setType(Constant.STATE_FINALIZED);

				flag = conStateDao.add(conState);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.finalize");
		}
		return flag;
	}
	
	public List<CSignatureOpinion> showHQOpinion(int conId) throws AppException {
		List<CSignatureOpinion> csOpinionList = new ArrayList<CSignatureOpinion>();
		
		try {
			List<Integer> conProcessIds = conProcessDao.getIds(conId, Constant.PROCESS_CSIGN, Constant.DONE);
			for (int id : conProcessIds) {
				ConProcess conProcess = conProcessDao.getById(id);
				User user = userDao.getById(conProcess.getUserId());
				CSignatureOpinion csOpinion = new CSignatureOpinion(); 
				csOpinion.setConId(conId);
				if (conProcess != null) {
					csOpinion.setOpinion(conProcess.getContent());
				}
				if (user != null) {
					csOpinion.setCsOperator(user.getName());
				}
				csOpinionList.add(csOpinion);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.showHQOpinion");
		}
		return csOpinionList;
		
	}
	
	public List<ConBusiModel> getDshphtList(int userId) throws AppException {
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		List<Integer> conIds = new ArrayList<Integer>();
		
		ConProcess conProcess = new ConProcess();
		conProcess.setUserId(userId);
		conProcess.setType(Constant.PROCESS_APPROVE);
		conProcess.setState(Constant.UNDONE);
		
		try {

			List<Integer> myConIds = conProcessDao.getConIds(conProcess);
			for (int conId : myConIds) {
				if (conStateDao.isExist(conId, Constant.STATE_FINALIZED)) {
					conIds.add(conId);
				}
			}

			for (int conId : conIds) {
				Contract contract = contractDao.getById(conId);
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				if (conState != null) {
					conBusiModel.setDrafTime(conState.getTime());
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.getDshphtList");
		}
		return conList;
	}
	
	public boolean approve(ConProcess conProcess) throws AppException {
		boolean flag = false;
		conProcess.setType(Constant.PROCESS_APPROVE);
		
		try {
			if (conProcessDao.update(conProcess)) {
				conProcess.setState(Constant.UNDONE);
				int tbApprovedCount = conProcessDao.getTotalCount(conProcess);
				conProcess.setState(Constant.VETOED);
				int refusedCount = conProcessDao.getTotalCount(conProcess);

				if (tbApprovedCount == 0 && refusedCount == 0) {
					ConState conState = new ConState();
					conState.setConId(conProcess.getConId());
					conState.setType(Constant.STATE_APPROVED);
					flag = conStateDao.add(conState);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.approve");
		}
		return flag;
	}
	
	public List<ConBusiModel> getDqdhtList(int userId) throws AppException {
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		List<Integer> conIds = new ArrayList<Integer>();
		
		ConProcess conProcess = new ConProcess();
		conProcess.setUserId(userId);
		conProcess.setType(Constant.PROCESS_SIGN);
		conProcess.setState(Constant.UNDONE);
		
		try {

			List<Integer> myConIds = conProcessDao.getConIds(conProcess);

			for (int conId : myConIds) {
				if (conStateDao.isExist(conId, Constant.STATE_APPROVED)) {
					conIds.add(conId);
				}
			}

			for (int conId : conIds) {
				Contract contract = contractDao.getById(conId);
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				if (conState != null) {
					conBusiModel.setDrafTime(conState.getTime());
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.getDqdhtList");
		}

		return conList;
	}
	
	public boolean sign(ConProcess conProcess) throws AppException {
		boolean flag = false;
		
		conProcess.setType(Constant.PROCESS_SIGN);
		conProcess.setState(Constant.DONE);
		
		try {
			if (conProcessDao.update(conProcess)) {
				ConState conState = new ConState();
				conState.setConId(conProcess.getConId());
				conState.setType(Constant.STATE_SIGNED);
				flag = conStateDao.add(conState);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"com.ruanko.service.ContractService.sign");
		}
		return flag;
	}
	
	private String generateConNum() {
		Date date = new Date();
		SimpleDateFormat sft = new SimpleDateFormat("yyyyMMddhhmmss");
		
		int rd = new Random().nextInt(99999);
		String rand = "00000" + rd;
		rand = rand.substring(rand.length() - 5);
		
		String contractNum = sft.format(date) + rand;
		return contractNum;
	}
	
	
}
