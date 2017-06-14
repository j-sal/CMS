package com.ruanko.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.ConBusiModel;
import com.ruanko.service.ContractService;
import com.ruanko.utils.AppException;

@SuppressWarnings("serial")
public class ToDqdhtListServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = null;
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		if (userId == null) {
			response.sendRedirect("toLogin");
		}else {
			
			try {
				ContractService contractService = new ContractService();
				List<ConBusiModel> contractList = new ArrayList<ConBusiModel>();
				contractList = contractService.getDqdhtList(userId);
				request.setAttribute("contractList", contractList);
				request.getRequestDispatcher("/dqdhtList.jsp").forward(request, response);
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
