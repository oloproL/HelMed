package com.unir.helmed;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Symptoms
 */
@WebServlet("/Symptoms.servlet")
public class SymptomsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SymptomsServlet() {
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
		HttpSession session = request.getSession(false);		// Creating HTTP session
		
		Category cat = new Category();
		cat.setCategory(request.getParameter("category"));		// Obtaining category selected by the user

		// Obtaining symptoms for the category selected and return them as session parameters
		session.setAttribute("category", cat);												
		session.setAttribute("categoryIndex", request.getParameter("categoryIndex"));
		session.setAttribute("symptomIndex", request.getParameter("0"));
		session.setAttribute("valueIndex", request.getParameter("0"));
		response.sendRedirect("index.jsp");
	}

}
