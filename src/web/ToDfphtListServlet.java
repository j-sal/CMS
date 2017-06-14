package com.ruanko.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.ConBusiModel;
import com.ruanko.service.ContractService;
import com.ruanko.utils.AppException;
import com.ruanko.model.Role;
import com.ruanko.service.UserService;

@SuppressWarnings("serial")
public class ToDfphtListServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		// Set output content's type
		response.setContentType("text/html");
		// Set the response's character encoding
		response.setCharacterEncoding("UTF-8");
		// Set the request's character encoding
		request.setCharacterEncoding("UTF-8");
		
		// Declare session
		HttpSession session = null;
		// Get session by using request
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// If user is not login, jump to login page
		if (userId == null) {
			response.sendRedirect("toLogin");
		}else {
			try {
				UserService userService = new UserService();
				Role role = userService.getUserRole(userId);
				
				if ( role == null) {
					response.sendRedirect("toNewUser");
				}else {
					String funcIds = role.getFuncIds();

					boolean flag1 = false;
					boolean flag2 = false;
					boolean flag3 = false;
					
					for (String id : funcIds.split(",")) {
						if (id.equals("008")) {
							flag1 = true;
						}
						if (id.equals("009")) {
							flag2 = true;
						}
						if (id.equals("010")) {
							flag3 = true;
						}
					}
					
					if (flag1 && flag2 && flag3) {
						ContractService contractService = new ContractService();
						List<ConBusiModel> contractList = new ArrayList<ConBusiModel>();
						contractList = contractService.getDfphtList();
						request.setAttribute("contractList", contractList);
						request.getRequestDispatcher("/dfphtList.jsp").forward(request, response);
					}else {
						PrintWriter out = response.getWriter();
						out.println("<script language = javascript>alert('You don't have the permission!')");
						out.println("window.history.go(-1)</script>");
					}
				}
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
