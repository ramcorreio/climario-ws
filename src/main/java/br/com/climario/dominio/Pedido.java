package br.com.climario.dominio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
	
	@NotNull
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date criacao;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Cliente cliente;
	
	/*@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(uniqueConstraints= @UniqueConstraint(columnNames={"pedido_id","codigo"}))
    @NotEmpty()
    @Size(min=1)
	private Set<ItemPedido> itens = new HashSet<ItemPedido>();*/
	
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
	
	/*public Set<ItemPedido> getItens() {
		return itens;
	}
	
	public void setItens(Set<ItemPedido> itens) {
		this.itens = itens;
	}*/
}
