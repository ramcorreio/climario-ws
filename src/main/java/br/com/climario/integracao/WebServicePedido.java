package br.com.climario.integracao;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.climario.dominio.ItemPedido;
import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.ServiceLocator;
import br.com.climario.ui.Util;

@Path("/pedido-ws")
public class WebServicePedido {
	
	private static Logger _logger = LoggerFactory.getLogger(WebServicePedido.class);
	
	public String envioEmail = "jonath@internit.com.br";
	
	private IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	public enum Code {
		
		ERRO_PROCESSAMENTO(100, "Processamento Ok."),
		PROCESSADO_COM_SUCESSO(101, "Processamento Ok."),
		PEDIDO_EXISTE(102, "Pedido já cadastrado.");
		
		private int code;
		
		private String message;
		
		
		
		private Code(int code, String message) {
			this.code = code;
			this.message = message;
		}
		
		public int getCode() {
			return code;
		}
		
		public String getMessage() {
			return message;
		}
	}
	
	@Path("enviar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public PedidoResponse enviar(PedidoResponse pedido) {
				
		Set<ItemPedido> t1 = pedido.getItens();
		
		int i = 0;
		
		for(ItemPedido item : t1) 
		{
			_logger.info(" ############################################ ");
			_logger.info("Código do Item: " + item.getCodigo());
            _logger.info("Descrição do Item: " + item.getDescricao());
            _logger.info("Preço unitário dos Item: " + item.getPrecoUnitario());
            _logger.info("Quantidade de Itens: " + item.getQtd());
            _logger.info("Cidade: " + pedido.getCliente().getCidade());
            _logger.info("Bairro: " + pedido.getCliente().getBairro());
            _logger.info("Logradouro: " + pedido.getCliente().getLogradouro());
            _logger.info("Email RCA: " + pedido.getCliente().getEmailRca());
            _logger.info("Email: " + pedido.getCliente().getEmail());
            _logger.info(" ############################################ ");
        }
		
		String email = pedido.getCliente().getEmail();
		String EmailRca = pedido.getCliente().getEmailRca();
		
		System.out.println("------------------------------------------------");
		
		_logger.info("processando pedido: " + pedido.getNumero());
		
		if(pedidoService.isPedidoExiste(pedido.getNumero())){
			pedido.setCode(Code.PEDIDO_EXISTE);
			pedido.setMensagem("Pedido já cadastrado.");
			return pedido;
		}
		 
		try {
			
			if(!pedidoService.isClienteExiste(pedido.getCliente().getCpfCnpj())) 
			{
				pedido.setCliente(pedidoService.criarCliente(pedido.getCliente()));
			}
			else 
			{
				pedido.setCliente(pedidoService.recuperarCliente(pedido.getCliente().getCpfCnpj()));
				pedido.getCliente().setEmail(email);
				pedido.getCliente().setEmailRca(EmailRca);
				pedido.setCliente(pedidoService.atualizarCliente(pedido.getCliente()));
				
			}
			
			System.out.println("------------------------------------------------");
			System.out.println(pedido.getCliente().getEmailRca());
			System.out.println("------------------------------------------------");
			
			pedidoService.criar(pedido);
			pedido.setCode(Code.PROCESSADO_COM_SUCESSO);
			pedido.setMensagem("Pedido salvo");
			
			String texto = "Prezado(a)  "+pedido.getCliente().getNome()+", <br /><br /> ";
				   texto += "Desde já agradecemos a escolha pela Clima Rio.<br /><br />";
				   texto += "Seu pedido "+pedido.getNumero()+" está disponível para pagamento em nosso sistema online.Para acessá-lo use o endereço <a href='http://www.climariopagamentos.com.br' target='_blank'>www.climariopagamentos.com.br</a> e preencha com seu CPF ou CNPJ, bem como com o número do pedido informado acima para assim, iniciar o processo de pagamento da sua compra.<br /><br />";
				   texto += "Em caso de dúvida ou problema entre em contato com o consultor de vendas que lhe atendeu.<br /><br />";			
				   texto += "Clima Rio.<br/>";
				   texto += "Sempre a melhor compra.<br/><br/>";
				   texto += "<img src='http://climariopagamentos.com.br/javax.faces.resource/img/clima_logo.jpg.jsf?ln=media'>";
		
			Util.sendMail(pedido.getCliente().getEmail(), "Solicitar Pedido", texto);
			
			/*try {
				
				Util.enviarEmail("jonath@internit.com.br", "Teste Envio",	"jonath@oi.com.br", "Pingo", "Teste Assunto", "Teste texto");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		catch(RuntimeException e) {
			
			pedido.setCode(Code.ERRO_PROCESSAMENTO);
			pedido.setMensagem(e.getMessage());
			//throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build());
		}
		
		return pedido;
	}
	
	@Path("pedidos")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
	public List<Pedido> listar(@QueryParam("idCliente") String idCliente) {
		
		try {
			
			//List<Pedido> rs = new ArrayList<>();
			List<Pedido>  pedidos = pedidoService.listarPedidosPorCliente(idCliente);
			/*for (Pedido pedido : pedidos) {
				Pedido p = new Pedido();
				p.setId(pedido.getId());
				p.setNumero(pedido.getNumero());
				p.setCriacao(pedido.getCriacao());
				p.setCliente(pedido.getCliente());
				rs.add(p);
			}*/
			
			_logger.info("pedidos encontrados: " + pedidos.size());
			return pedidos;
		}
		catch(RuntimeException e) {
			
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build());
		}
	}
}