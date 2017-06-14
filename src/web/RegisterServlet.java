package com.ruanko.web;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ruanko.model.User;
import com.ruanko.service.UserService;
import com.ruanko.utils.AppException;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		boolean flag = false;

		String message = ""; 
		try {
			User user = new User();
			UserService userService = new UserService();
			user.setName(name);
			user.setPassword(password); 
			flag = userService.register(user);
			if (flag) { 
				response.sendRedirect("toLogin");
			} else { 
				message = "Registration failed";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/register.jsp").forward(request,response);
			}
		} catch (AppException e) {
			e.printStackTrace();
			response.sendRedirect("toError");
		}
		
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
