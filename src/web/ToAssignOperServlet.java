package com.ruanko.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.Contract;
import com.ruanko.model.User;
import com.ruanko.service.ContractService;
import com.ruanko.service.UserService;
import com.ruanko.utils.AppException;

@SuppressWarnings("serial")
public class ToAssignOperServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = null;
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		if (userId == null) {
			response.sendRedirect("toLogin");
		}else {
			int conId = Integer.parseInt(request.getParameter("conId"));
			
			try {
			ContractService contractService = new ContractService();
			Contract contract = contractService.getContract(conId);
			
			UserService userService = new UserService();
			int roleId = 2;
			List<User> userList = new ArrayList<User>();
			userList = userService.getUserListByRoleId(roleId);
			request.setAttribute("contract", contract);
			request.setAttribute("userList", userList);
			request.getRequestDispatcher("/assignOperator.jsp").forward(request,
					response);
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
