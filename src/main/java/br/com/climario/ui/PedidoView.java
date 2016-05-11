package br.com.climario.ui;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.inputtext.InputText;

import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.ServiceLocator;

@ManagedBean
@ViewScoped
public class PedidoView {
	
	private transient IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	private String numero;
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public void consultar(ActionEvent actionEvent) {
		
		InputText cpfCnpj = (InputText) actionEvent.getComponent().getParent().findComponent("cpfCnpj");
		if(!pedidoService.isPedidoClienteExiste(cpfCnpj.getValue().toString(), numero)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido " + numero + " não encontrado", "Erro!"));	
		}
		else {
			System.out.println("Abrir outra página");
		}
	}

}
