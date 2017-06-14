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
import com.ruanko.model.Role;
import com.ruanko.service.UserService;
import com.ruanko.utils.AppException;

@SuppressWarnings("serial")
public class ToAssignPermServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		HttpSession session = null;
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		if (userId == null) {
			response.sendRedirect("toLogin");
		} else {

			int uId = Integer.parseInt(request.getParameter("userId"));
			String userName = (String)request.getParameter("uName");
			int roleId = Integer.parseInt(request.getParameter("roleId"));
			PermissionBusiModel permission = new PermissionBusiModel();
			permission.setUserId(uId);
			permission.setUserName(userName);
			permission.setRoleId(roleId);
			
			request.setAttribute("permission", permission);
			
			try {
				UserService userService = new UserService();
				List<Role> roleList = new ArrayList<Role>();
				roleList = userService.getRoleList();

				request.setAttribute("roleList", roleList);
				request.getRequestDispatcher("/assignPermission.jsp").forward(
						request, response);
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
