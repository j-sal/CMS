package com.ruanko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.ConProcess;
import com.ruanko.service.ContractService;
import com.ruanko.utils.AppException;
import com.ruanko.utils.Constant;

@SuppressWarnings("serial")
public class AddSHPOpinionServlet extends HttpServlet {
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		HttpSession session = null;
		session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect("toLogin");
		} else {

			int conId = Integer.parseInt(request.getParameter("conId"));
			String content = request.getParameter("content");
			String approve = request.getParameter("approve");

			ConProcess conProcess = new ConProcess();
			conProcess.setConId(conId);
			conProcess.setUserId(userId);
			conProcess.setContent(content);
			
			if (approve.equals("true")) { 
				conProcess.setState(Constant.DONE);
			} else { 
				conProcess.setState(Constant.VETOED);
			}
			try {
				ContractService contractService = new ContractService();
				contractService.approve(conProcess);
				response.sendRedirect("toDshphtList");
			} catch (AppException e) {
				e.printStackTrace();
				response.sendRedirect("toError");
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
}
