package com.ruanko.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Handle the user's request, output the results page

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {

	//Handle the POST requests for secure data transmission, output the results page
	 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//Call doGet() to handle request
		doGet(request, response);
	}

	//Handle the GET requests for lower overhead, output the results page
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");// Set the types of output content 
		response.setCharacterEncoding("UTF-8");// Set the character encoding of output content 
		PrintWriter out = response.getWriter();// Get the output object
		// Output the <!DOCTYPE> declaration
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		// Output the standard HTML structure
		out.println("<html>");
		out.println("<head>");
		// Set the character encoding for the HTML document
		out.println("<meta http-equiv='Content-Type' content='text/html' charset='utf-8' />");
		out.println("<title>IndexServlet</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("Welcome to Group 3's Contract Management System! ");//Information in HTML Body
		out.println("</body>");
		out.println("</html>");
		// Clear and close the output stream
		out.flush();
		out.close();
	}
}


