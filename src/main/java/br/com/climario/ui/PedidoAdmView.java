package br.com.climario.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.component.inputtext.InputText;

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
	
	public void limpar(ActionEvent actionEvent) {
		
		pedidos.clear();
		pedidos.addAll(pedidoService.listar());
		InputText inputText = (InputText) actionEvent.getComponent().findComponent("numero");
		inputText.resetValue();
	}
	
	public void buscar(ActionEvent actionEvent) {
		
		InputText inputText = (InputText) actionEvent.getComponent().findComponent("numero");
		
		if(inputText.getValue() != null && !inputText.getValue().toString().isEmpty()) {
			pedidos.clear();
			pedidos.add(pedidoService.recuperarPedido(inputText.getValue().toString()));	
		}
	}
	
	
	public Double getTotal(Pedido p) {
		
		return PedidoView.getTotalPedido(p);
	}

}
