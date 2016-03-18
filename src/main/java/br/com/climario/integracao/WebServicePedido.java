package br.com.climario.integracao;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.ServiceLocator;

@Path("pedido-ws")
public class WebServicePedido {
	
	
	private IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	@Path("enviar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Pedido enviar(Pedido pedido) {
	
		if(!pedidoService.isClienteExiste(pedido.getCliente().getCodigo())) {
			pedido.setCliente(pedidoService.criarCliente(pedido.getCliente()));
		}
		else {
			pedido.setCliente(pedidoService.recuperarCliente(pedido.getCliente().getCodigo()));
		}
		
		return pedidoService.criar(pedido);
	}
}
