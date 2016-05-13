package br.com.climario.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "Cliente.all", query = "select c from Cliente c order by nome"),
	@NamedQuery(name = "Cliente.existe", query = "select c from Cliente c where cpfCnpj = :cpfCnpj")
})
@XmlRootElement
public class Cliente implements Serializable {

	private static final long serialVersionUID = 909283118822489309L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String codigo;
	
	@Column(unique = true)
	@NotNull
	private String cpfCnpj;

	@NotNull
	private String nome;

	@NotNull
	private String email;

	@NotNull
	private String logradouro;

	@NotNull
	private String numero;

	@NotNull
	private String complemento;

	@NotNull
	private String bairro;

	@NotNull
	private String cidade;

	@NotNull
	private String estado;
	
	@NotNull
	private String cep;

	@NotNull
	private String emailRca;

	@NotNull
	private String codigoRca;

	@NotNull
	private String nomeRca;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEmailRca() {
		return emailRca;
	}

	public void setEmailRca(String emailRca) {
		this.emailRca = emailRca;
	}

	public String getCodigoRca() {
		return codigoRca;
	}

	public void setCodigoRca(String codigoRca) {
		this.codigoRca = codigoRca;
	}

	public String getNomeRca() {
		return nomeRca;
	}

	public void setNomeRca(String nomeRca) {
		this.nomeRca = nomeRca;
	}

}
