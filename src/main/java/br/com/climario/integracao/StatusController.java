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
import br.com.climario.ui.Util;
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
	
	public String envioEmail = "jonath@internit.com.br";
	
	public String envioEmailU = "jonath@internit.com.br";
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.addHeader("Access-Control-Allow-Origin", "https://sandbox.pagseguro.uol.com.br");
		
		String texto = null;
		String texto2 = null;
		
		try {
			
			Pedido pedido = ServiceLocator.getInstance().getPedidoService().recuperarPedidoPorTransacao(request.getParameter("transaction_id"));
			
			System.out.println(request.getParameter("transaction_id"));
			Map<String, String> st = new HashMap<String, String>();
				st.put("4" , "APROVADA");
				st.put("7" , "PENDENTE");
				st.put("5" , "EXPIRADA");
				st.put("6" , "REJEITADA");
			
			PedidoStatus ps = PedidoStatus.getTIpo(st.get(request.getParameter("state_pol")));
			
			texto2 = "Prezado(a)  "+pedido.getCliente().getNome()+", <br /> ";
			
			if (ps.toString().equals("APROVADA"))
			{
				texto = "O cliente "+pedido.getCliente().getNome()+", completou com sucesso o processo de pagamento para o pedido "+pedido.getNumero()+" e está com o pagamento aprovado. Prosseguir com a entrega e demais rotinas.<br /><br />";
				
				texto2 += "Seu pedido "+pedido.getNumero()+" teve o pagamento aprovado. A partir de agora você será notificado pela Clima Rio para prosseguimento de entrega deste pedido.<br /><br />";
				
				   
			}
			else if (ps.toString().equals("REJEITADA"))
			{
				texto = "O cliente "+pedido.getCliente().getNome()+", não teve sucesso com o pagamento para o pedido "+pedido.getNumero()+" e está com o pagamento recusado. Retornar o contato com o cliente.<br /><br />";
				
				texto2 += "Seu pedido "+pedido.getNumero()+" teve o pagamento reprovado. Entre em contato com a Clima Rio no telefone 021 xxxx-xxxx para mais informações.<br /><br />";
			}
			
				texto += "Clima Rio<br>";
				texto += "Sempre a melhor compra";
				
				texto2 += "Em caso de dúvidas ou quaisquer problemas ligue para 021 xxxx-xxxx.<br /><br />";
				texto2 += "Clima Rio<br/>";
				texto2 += "Sempre a melhor compra";
			
			Util.sendMail(envioEmail, "Solicitar Pedido", texto);
			Util.sendMail(envioEmailU, "Solicitar Pedido", texto2);
			
			//transaction_id
			ServiceLocator.getInstance().getPedidoService().atulizarStatus(request.getParameter("transaction_id"), ps);
		  
		} catch (Exception e) {  
		    System.err.println(e.getMessage());  
		}
	}

	


}
