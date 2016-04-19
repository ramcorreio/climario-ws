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
	
	@Path("enviar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Pedido enviar(Pedido pedido) {
		
		try {
			_logger.info("processando pedido: " + pedido.getNumero());
			if(!pedidoService.isClienteExiste(pedido.getCliente().getCodigo())) {
				pedido.setCliente(pedidoService.criarCliente(pedido.getCliente()));
			}
			else {
				pedido.setCliente(pedidoService.recuperarCliente(pedido.getCliente().getCodigo()));
			}
			
			//pedido.setCriacao(Calendar.getInstance().getTime());
			return pedidoService.criar(pedido);
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
			
			List<Pedido>  pedidos = pedidoService.listarPedidosPorCliente(idCliente);
			_logger.info("pedidos encontrados: " + pedidos.size());
			System.out.println(pedidos);
			return pedidos;
		}
		catch(RuntimeException e) {
			
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build());
		}
	}
}