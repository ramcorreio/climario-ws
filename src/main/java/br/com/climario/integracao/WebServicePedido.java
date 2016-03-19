package br.com.climario.integracao;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.ServiceLocator;

@Path("pedido-ws")
public class WebServicePedido {
	
	private static Logger _logger = LoggerFactory.getLogger(WebServicePedido.class);
	
	private IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	@Path("enviar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Pedido enviar(Pedido pedido) {
		
		_logger.info("processando pedido: " + pedido.getNumero());
		if(!pedidoService.isClienteExiste(pedido.getCliente().getCodigo())) {
			pedido.setCliente(pedidoService.criarCliente(pedido.getCliente()));
		}
		else {
			pedido.setCliente(pedidoService.recuperarCliente(pedido.getCliente().getCodigo()));
		}
		
		return pedidoService.criar(pedido);
	}
	
	@Path("pedidos/{idCliente}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
	public List<Pedido> listar(@PathParam("idCliente") String idCliente) {
		
		System.out.println("cliente: " + idCliente);
		List<Pedido>  pedidos = pedidoService.listarPedidosPorCliente(idCliente);
		_logger.info("pedidos encontrados: " + pedidos.size());
		System.out.println(pedidos);
		return pedidos;
	}
}
