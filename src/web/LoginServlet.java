package com.ruanko.web;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ruanko.model.Role;
import com.ruanko.service.UserService;
import com.ruanko.utils.AppException;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
				int userId = -1;
		String message = "";

		try {
			UserService userService = new UserService();
			userId = userService.login(name, password);
			if (userId > 0) {   

				HttpSession session = null;
				session = request.getSession();
				session.setAttribute("userId", userId);
				session.setAttribute("userName", name);
				Role role = null;
				role = userService.getUserRole(userId);
				if ( role == null) {
					response.sendRedirect("toNewUser");
				} else if (role.getName().equals("admin")) {
					response.sendRedirect("toAdmin");
				} else if (role.getName().equals("operator")) {
					response.sendRedirect("toOperator");
				}
			} else {
				message = "Incorrect user name or password!";
				request.setAttribute("message", message); 
				request.getRequestDispatcher("/login.jsp").forward(request,
						response);
			}
		} catch (AppException e) {
			e.printStackTrace();
			response.sendRedirect("toError");
		}
	}

	/**
	 * Process GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
}
