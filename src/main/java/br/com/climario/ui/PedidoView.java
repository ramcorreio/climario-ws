package br.com.climario.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.inputtext.InputText;

import br.com.climario.dominio.ItemPedido;
import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.ServiceLocator;

@ManagedBean
@ViewScoped
public class PedidoView implements Serializable {
	
	private static final long serialVersionUID = -3297581325023937731L;

	private transient IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	private String numero;
	
	private Pedido pedido;
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public Pedido getPedido() {
		return pedido;
	}
	
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	public void consultar(ActionEvent actionEvent) {
		
		InputText cpfCnpj = (InputText) actionEvent.getComponent().getParent().findComponent("cpfCnpj");
		if(!pedidoService.isPedidoClienteExiste(cpfCnpj.getValue().toString(), numero)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido " + numero + " não encontrado", "Erro!"));	
		}
		else {
			Util.redirect(Util.getContextRoot("/pages/resumo.jsf?id=" + numero));
		}
	}
	
	@PostConstruct
	public void load(){
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("id")) {
			numero = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			pedido = pedidoService.recuperarPedido(numero);
		}
		else {
			Util.redirect(Util.getContextRoot());
		}
	}
	
	public List<ItemPedido> getItens(){
		
		List<ItemPedido> itens = new ArrayList<>();
		for (ItemPedido itemPedido : pedido.getItens()) {
			itens.add(itemPedido);
		}
		return itens;
	}
	
	public Double getTotalPedido(){
		
		Double sum = 0d;
		for (ItemPedido itemPedido : pedido.getItens()) {
			sum += itemPedido.getTotal();
		}
		return sum;
	}
}
