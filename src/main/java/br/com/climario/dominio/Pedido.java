package br.com.climario.dominio;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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

@Entity
@NamedQueries({ 
    @NamedQuery(name = "Pedido.por.cliente", query = "select p from Pedido p where p.cliente.codigo = :codigo")
})
public class Pedido {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String numero;
	
	//private String filial;
	
	@NotNull
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date criacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Cliente cliente;
	
	/*private String descricaoCobranca;
	
	private String descricaoPlanoPagamento;*/
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(uniqueConstraints= @UniqueConstraint(columnNames={"pedido_id","codigo"}))
    @NotEmpty()
    @Size(min=1)
	private Set<ItemPedido> itens = new HashSet<ItemPedido>();
	
	//private Float valorFrete;
	
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
	
	public Set<ItemPedido> getItens() {
		return itens;
	}
	
	public void setItens(Set<ItemPedido> itens) {
		this.itens = itens;
	}
}
