package br.com.climario.integracao;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorController extends HttpServlet {

	private static final long serialVersionUID = 4200225829267178171L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		request.setAttribute("statusCode", statusCode);
		
		switch (statusCode) {
			case HttpServletResponse.SC_NOT_FOUND:
	
				request.setAttribute("description", "Desculpe, mas esta página não existe!");
				request.setAttribute("subDescription", "Gostaria de voltar ao <a href='"+ request.getContextPath() + "'>início</a>?");
				break;
	
			case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
	
				request.setAttribute("description", "Não se preocupe, há um pouco de turbulência!");
				request.setAttribute("subDescription", "Estamos trabalhando para corrigí-lo, por favor, tente mais tarde.");
				break;
	
			default:
				break;
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/error/error.jsp");
		requestDispatcher.forward(request, response);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}
}
