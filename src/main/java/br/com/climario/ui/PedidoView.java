package br.com.climario.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.domain.checkout.Checkout;

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
	
	public void checkout(ActionEvent actionEvent) {
		
		System.out.println(actionEvent);
		Checkout checkout = new Checkout();
		System.out.println(checkout);
		//checkout.addItem("id", "ddd", 1, new BigDecimal(3), 0l, new BigDecimal(0));
				
		/*PaymentRequest p = new PaymentRequest();
		p.addItem(, description, quantity, amount, weight, shippingCost);*/
		//Checkout checkout = new Checkout();  

	}
	
	public void init() {
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("id")) {
			numero = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			pedido = pedidoService.recuperarPedido(numero);
		}
		/*else {
			Util.redirect(Util.getContextRoot());
		}*/
	}
	
	public class ItemWrap {
		
		private ItemPedido item;
		
		public ItemWrap(ItemPedido item) {
			this.item = item;
		}
		
		public String getCodigo() {
			return item.getCodigo();
		}

		public String getDescricao() {
			return item.getDescricao();
		}

		public Integer getQtd() {
			return item.getQtd();
		}
		
		public Double getPrecoUnitario() {
			return item.getPrecoUnitario();
		}
		
		public Double getTotal() {
			return item.getPrecoUnitario() * item.getQtd();
		}
		
	}
	
	public List<ItemWrap> getItens(){
		
		List<ItemWrap> itens = new ArrayList<>();
		for (ItemPedido itemPedido : pedido.getItens()) {
			itens.add(new ItemWrap(itemPedido));
		}
		return itens;
	}
	
	public Double getTotalPedido(){
		
		Double sum = 0d;
		for (ItemWrap itemPedido : getItens()) {
			sum += itemPedido.getTotal();
		}
		return sum;
	}
}
