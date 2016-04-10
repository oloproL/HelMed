package com.unir.helmed;

import com.unir.helmed.hive.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DiseasesServlet
 */
@WebServlet("/Diseases.servlet")
public class DiseasesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DiseasesServlet() {
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
		String symptom = request.getParameter("symptoms");			// Symptom selected
		String value = request.getParameter("values");				// Value selected
		
		
		Hive hive = new Hive();				// Creating a Hive Object to do hive queries
		try {
			// Obtaining all diseases for the symptom and value selected and return them as session parameters
			ArrayList<String[]> diseases = hive.getDisease(symptom, value);
			request.getSession().setAttribute("diseases", diseases);
			request.getSession().setAttribute("valueIndex", request.getParameter("valueIndex"));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("index.jsp");
	}
}
