package com.ruanko.web;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.Contract;
import com.ruanko.service.ContractService;
import com.ruanko.utils.AppException;

@SuppressWarnings("serial")
public class DraftServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = null;
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		 
		if (userId == null) {
			response.sendRedirect("toLogin");
		}else {
			String name = request.getParameter("name");
			String customer = request.getParameter("customer");
			String content = request.getParameter("content");
			String beginTime = request.getParameter("beginTime");
			String endTime = request.getParameter("endTime");
			
			Date begin = new Date();
			Date end = new Date();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
			String message = "";
			
			try {
				begin = dateFormat.parse(beginTime);
				end = dateFormat.parse(endTime);
				
				Contract contract = new Contract();
				contract.setName(name);
				contract.setCustomer(customer);
				contract.setBeginTime(begin);
				contract.setEndTime(end);
				contract.setContent(content);
				contract.setUserId(userId);
				
				ContractService contractService = new ContractService();
				
				if (contractService.draft(contract)) {
					message = "Drafting succeeded!";
					request.setAttribute("contract", contract);
				} else {
					message = "Drafting failure!";
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
				message = "Contract data is required. Incorrect date format";
			} catch (AppException e) {
				e.printStackTrace();
				response.sendRedirect("toError");
				return;
			}
			request.setAttribute("message", message);
			request.getRequestDispatcher("/addContract.jsp").forward(request, response);
		}
	}
	
	/**
	 * Process the GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
}
