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
public class DgContractServlet extends HttpServlet {
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
			String name = request.getParameter("name");
			String customer = request.getParameter("customer");
			String beginTime = request.getParameter("beginTime");
			String endTime = request.getParameter("endTime");
			String content = request.getParameter("content");

			Date begin = new Date();
			Date end = new Date();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				begin = dateFormat.parse(beginTime);
				end = dateFormat.parse(endTime);

				Contract contract = new Contract();
				contract.setId(conId);
				contract.setName(name);
				contract.setCustomer(customer);
				contract.setBeginTime(begin);
				contract.setEndTime(end);
				contract.setContent(content);
				contract.setUserId(userId);

				ContractService contractService = new ContractService();
				contractService.finalize(contract);
				response.sendRedirect("toDdghtList");
			} catch (ParseException e) {
				e.printStackTrace();
				String message = "";
				message = "Please enter the correct date format!";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/dgContract.jsp").forward(
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
