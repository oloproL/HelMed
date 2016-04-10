package com.unir.helmed;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unir.helmed.hive.Hive;

/**
 * Servlet implementation class InfoServlet
 */
@WebServlet("/Info.servlet")
public class InfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String disease = request.getParameter("diseases");			// Obtaining the disease selected

		Hive hive = new Hive();			// Creating a Hive Object to do hive queries
		try {
			
			// Obtaining information for the disease selected and return them as session parameters
			String[] info = hive.getInfo(disease);
			request.getSession().setAttribute("disease", disease);
			request.getSession().setAttribute("tag", info[0]);
			request.getSession().setAttribute("info", info[1]);
			request.getSession().setAttribute("diseaseIndex", request.getParameter("diseaseIndex"));
			
			// Obtaining drugs for the disease selected and return them as session parameters
			ArrayList<String[]> drugs = hive.getDrugs(info[0]);
			request.getSession().setAttribute("drugs", drugs);
			

		} catch (SQLException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("index.jsp");
	}

}
