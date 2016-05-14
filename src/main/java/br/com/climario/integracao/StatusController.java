package br.com.climario.integracao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.climario.dominio.Pedido.PedidoStatus;
import br.com.climario.service.impl.ServiceLocator;
import br.com.climario.ui.PedidoView;
import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.NotificationService;

@WebServlet(urlPatterns = "/status")
public class StatusController extends HttpServlet {
	
	private static final long serialVersionUID = 6656505569565364366L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "https://sandbox.pagseguro.uol.com.br");
		
		try {
			
			AccountCredentials accountCredencials = PedidoView.getAccountCredencials();
		  
			Transaction transaction = NotificationService.checkTransaction(accountCredencials, request.getParameter("notificationCode"));
			ServiceLocator.getInstance().getPedidoService().atulizarStatus(transaction.getReference(), PedidoStatus.getTIpo(transaction.getStatus()));
		  
		} catch (PagSeguroServiceException e) {  
		    System.err.println(e.getMessage());  
		}
	}

}
