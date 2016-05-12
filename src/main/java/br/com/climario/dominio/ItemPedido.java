package br.com.climario.dominio;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
public class ItemPedido implements Serializable {

	private static final long serialVersionUID = -3879211934699443946L;

	@NotNull
	private String codigo;

	@NotNull
	private String descricao;

	@NotNull
	private Integer qtd;

	@NotNull
	@NotEmpty
	private Double precoUnitario;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQtd() {
		return qtd;
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

	public Double getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}
	
	public Double getTotal() {
		return getPrecoUnitario() * getQtd();
	}

}
