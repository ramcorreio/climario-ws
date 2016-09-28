package br.com.climario.ui;

public class CartoesPagamento {
	
	private int id;
	
	private String description;
	private String country;
	private Boolean enabled;
	private String reason;
	
	public CartoesPagamento(int id, String description, String country, Boolean enabled)
	{
		this.setId(id);
		this.setDescription(description);
		this.setCountry(country);
		this.setEnabled(enabled);
		//this.setReason(reason);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
    
}
