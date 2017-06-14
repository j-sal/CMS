package com.ruanko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.service.ContractService;
import com.ruanko.utils.AppException;
import com.ruanko.utils.Constant;

@SuppressWarnings("serial")
public class AssignOperServlet extends HttpServlet {
	
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
			String[] hqht = request.getParameterValues("hqht");
			String[] spht = request.getParameterValues("spht");
			String[] qdht = request.getParameterValues("qdht");

			try {
				ContractService contractService = new ContractService();
				for (String hq : hqht) {
					contractService.distribute(conId, Integer.parseInt(hq),
							Constant.PROCESS_CSIGN);
				}

				for (String sp : spht) {
					contractService.distribute(conId, Integer.parseInt(sp),
							Constant.PROCESS_APPROVE);
				}

				for (String qd : qdht) {
					contractService.distribute(conId, Integer.parseInt(qd),
							Constant.PROCESS_SIGN);
				}

				response.sendRedirect("toDfphtList");
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
