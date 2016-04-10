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
 * Servlet implementation class ValuesServlet
 */
@WebServlet("/Values.servlet")
public class ValuesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ValuesServlet() {
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
		String symptom = request.getParameter("symptoms");			// Obtaining the symptom selected
		
		Hive hive = new Hive();			// Creating a Hive Object to do hive queries
		try {
			// Obtaining the value list for the symptom selected and return them as session parameters
			ArrayList<String> values = hive.getValues(symptom);			
			request.getSession().setAttribute("values", values);
			request.getSession().setAttribute("symptomIndex", request.getParameter("symptomIndex"));
			request.getSession().setAttribute("valueIndex", request.getParameter("0"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("index.jsp");
	}

}
