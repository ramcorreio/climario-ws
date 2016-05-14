package br.com.climario.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.ServiceLocator;

@ManagedBean
@ViewScoped
public class PedidoAdmView implements Serializable {
	
	private static final long serialVersionUID = 8236191390444962707L;

	private IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	public List<Pedido> pedidos = new ArrayList<Pedido>();
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}
	
	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	
	@PostConstruct
	public void init() {
		
		pedidos.addAll(pedidoService.listar());
	}

}
