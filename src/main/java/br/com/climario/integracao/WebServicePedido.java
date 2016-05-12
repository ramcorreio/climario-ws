package br.com.climario.integracao;

import java.util.List;

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

import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.ServiceLocator;

@Path("/pedido-ws")
public class WebServicePedido {
	
	private static Logger _logger = LoggerFactory.getLogger(WebServicePedido.class);
	
	private IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	public enum Code {
		
		PEDIDO_EXISTE(100, "Pedido já cadastrado.");
		
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
	public Pedido enviar(Pedido pedido) {
		
		_logger.info("processando pedido: " + pedido.getNumero());
		
		if(pedidoService.isPedidoExiste(pedido.getNumero())){
			throw new WebApplicationException(Response.status(Status.PRECONDITION_FAILED).entity(Code.PEDIDO_EXISTE).build());
		}
		
		try {
			
			if(!pedidoService.isClienteExiste(pedido.getCliente().getCpfCnpj())) {
				pedido.setCliente(pedidoService.criarCliente(pedido.getCliente()));
			}
			else {
				pedido.setCliente(pedidoService.recuperarCliente(pedido.getCliente().getCpfCnpj()));
			}
			
			pedidoService.criar(pedido);
			return pedido; 
		}
		catch(RuntimeException e) {
			
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build());
		}
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