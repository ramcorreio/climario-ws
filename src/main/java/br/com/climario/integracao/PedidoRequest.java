package br.com.climario.integracao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.ItemPedido;

public class PedidoRequest {

	private Long id;

	private String numero;

	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date criacao;

	private Cliente cliente;

	private List<ItemPedido> itens = new ArrayList<ItemPedido>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getCriacao() {
		return criacao;
	}

	public void setCriacao(Date criacao) {
		this.criacao = criacao;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public List<ItemPedido> getItens() {
		return itens;
	}
	
	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}
}
