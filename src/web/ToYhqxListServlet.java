package com.ruanko.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.PermissionBusiModel;
import com.ruanko.service.UserService;
import com.ruanko.utils.AppException;

@SuppressWarnings("serial")
public class ToYhqxListServlet extends HttpServlet{
	
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
				UserService userService = new UserService();
				List<PermissionBusiModel> permissionList = new ArrayList<PermissionBusiModel>();
				permissionList = userService.getYhqxList();
				request.setAttribute("permissionList", permissionList);
				request.getRequestDispatcher("/yhqxList.jsp").forward(request, response);
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
