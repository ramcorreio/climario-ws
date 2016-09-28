package br.com.climario.integracao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.climario.ui.PedidoView;
import br.com.climario.dominio.Pedido;
import br.com.climario.dominio.Pedido.PedidoStatus;
import br.com.climario.service.impl.PedidoServiceImpl;
import br.com.climario.service.impl.ServiceLocator;
import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.NotificationService;

@WebServlet(urlPatterns = "/status")
public class StatusController extends HttpServlet {
	
	private static final long serialVersionUID = 6656505569565364366L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.addHeader("Access-Control-Allow-Origin", "https://sandbox.pagseguro.uol.com.br");
		
		try {
			
			//PedidoServiceImpl pedicoS = new PedidoServiceImpl();
			//pedicoS.recuperarPedidoPorTransacao(request.getParameter("transaction_id"));
			
			//request.getParameter("transaction_id");
			
			//AccountCredentials accountCredencials = PedidoView.getAccountCredencials();
		  
			//Transaction transaction = NotificationService.checkTransaction(accountCredencials, request.getParameter("transaction_id"));
			
			/*String[] st = null;
			st[APPROVED]="APROVADO";*/
			System.out.println(request.getParameter("transaction_id"));
			Map<String, String> st = new HashMap<String, String>();
				st.put("4" , "APROVADA");
				st.put("7" , "PENDENTE");
				st.put("5" , "EXPIRADA");
				st.put("6" , "REJEITADA");
			
			PedidoStatus ps = PedidoStatus.getTIpo(st.get(request.getParameter("state_pol")));
			
			//transaction_id
			ServiceLocator.getInstance().getPedidoService().atulizarStatus(request.getParameter("transaction_id"), ps);
		  
		} catch (Exception e) {  
		    System.err.println(e.getMessage());  
		}
	}

	


}
