package br.com.climario.dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.climario.integracao.DateAdapter;
import br.com.uol.pagseguro.enums.TransactionStatus;

@Entity
@NamedQueries({
	@NamedQuery(name = "Pedido.all", query = "select p from Pedido p order by p.criado desc"),
    @NamedQuery(name = "Pedido.por.cliente", query = "select p from Pedido p where p.cliente.cpfCnpj = :cpfCnpj"),
    @NamedQuery(name = "Pedido.existe", query = "select p from Pedido p where p.numero = :numero"),
    @NamedQuery(name = "Pedido.cliente.existe", query = "select p from Pedido p where p.numero = :numero and p.cliente.cpfCnpj = :cpfCnpj"),
})
public class Pedido implements Serializable {
	
	private static final long serialVersionUID = 2808817848074903456L;
	
	public enum PedidoStatus {
		
		AGUARDANDO_PAGAMENTO(TransactionStatus.WAITING_PAYMENT),
		EM_ANALISE(TransactionStatus.IN_ANALYSIS),
		PAGO(TransactionStatus.PAID),
		DISPONIVEL(TransactionStatus.AVAILABLE),
		EM_DISPUTA(TransactionStatus.IN_DISPUTE),
		DEVOLVIDO(TransactionStatus.REFUNDED),
		CANCELADO(TransactionStatus.CANCELLED),
		ESTORNO(TransactionStatus.SELLER_CHARGEBACK),
		CONSTESTACAO(TransactionStatus.CONTESTATION),
		DESCONHECIDO(TransactionStatus.UNKNOWN_STATUS);
		
		private TransactionStatus status;
		
		private PedidoStatus(TransactionStatus status) {
			this.status = status;
		}
		
		public TransactionStatus getStatus() {
			return status;
		}
		
		public static PedidoStatus getTIpo(TransactionStatus status) {
			
			for (PedidoStatus pedidoStatus : PedidoStatus.values()) {
				
				if(pedidoStatus.status.equals(status)) {
					return pedidoStatus;
				}
			}
			
			return DESCONHECIDO;

		}
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = true)
	private String codigoAutorizacao;
	
	@Enumerated(EnumType.STRING)
	private PedidoStatus status;

	@NotNull
	@Column(unique = true)
	private String numero;
	
	/**
	 * Este campo registra a data de criação do pedido nos sistema do cliente
	 */
	@NotNull
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date criacao;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Cliente cliente;
	
	@NotNull
	private String filial;
	
	private Double valorFrete = 0d;
	
	@NotNull
	private String planoPagamento;
	
	@NotNull
	private String cobranca;
	
	/**
	 * Este campo registra a data de criação do pedido neste sistema
	 */
	@NotNull
	@Column(updatable = false)
	private Date criado;
	
	@NotNull
	private Date atualizado;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(uniqueConstraints= @UniqueConstraint(columnNames={"pedido_id","codigo"}))
    @NotEmpty()
    @Size(min=1)
	private Set<ItemPedido> itens = new HashSet<ItemPedido>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigoAutorizacao() {
		return codigoAutorizacao;
	}
	
	public void setCodigoAutorizacao(String codigoAutorizacao) {
		this.codigoAutorizacao = codigoAutorizacao;
	}
	
	public PedidoStatus getStatus() {
		return status;
	}
	
	public void setStatus(PedidoStatus status) {
		this.status = status;
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
	
	public String getFilial() {
		return filial;
	}
	
	public void setFilial(String filial) {
		this.filial = filial;
	}
	
	public Double getValorFrete() {
		return valorFrete;
	}
	
	public void setValorFrete(Double valorFrete) {
		this.valorFrete = valorFrete;
	}
	
	public String getPlanoPagamento() {
		return planoPagamento;
	}
	
	public void setPlanoPagamento(String planoPagamento) {
		this.planoPagamento = planoPagamento;
	}
	
	public String getCobranca() {
		return cobranca;
	}
	
	public void setCobranca(String cobranca) {
		this.cobranca = cobranca;
	}
	
	public Set<ItemPedido> getItens() {
		return itens;
	}
	
	public void setItens(Set<ItemPedido> itens) {
		this.itens = itens;
	}

	public Date getCriado() {
		return criado;
	}

	public void setCriado(Date criado) {
		this.criado = criado;
	}

	public Date getAtualizado() {
		return atualizado;
	}

	public void setAtualizado(Date atualizado) {
		this.atualizado = atualizado;
	}
}
