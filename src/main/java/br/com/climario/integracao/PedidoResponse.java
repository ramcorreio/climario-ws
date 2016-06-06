package br.com.climario.integracao;

import javax.persistence.Entity;

import br.com.climario.dominio.Pedido;
import br.com.climario.integracao.WebServicePedido.Code;

@Entity
public class PedidoResponse extends Pedido {

	private static final long serialVersionUID = 4141723104203972308L;

	private transient Code code;

	private transient String mensagem;

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
