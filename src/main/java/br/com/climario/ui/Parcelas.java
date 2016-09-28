package br.com.climario.ui;

import java.text.DecimalFormat;

public class Parcelas 
{
	private String parcela;	
	private int quantity;
	private double totalAmount;
	
	public Parcelas(String string, int quantity, double totalAmount)
	{
		this.setParcela(string);
		this.setQuantity(quantity);
		this.setTotalAmount(totalAmount);
	}


	


	public String getParcela() {
		return parcela;
	}


	public void setParcela(String parcela) {
		this.parcela = parcela;
	}


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount2) {
		this.totalAmount = totalAmount2;
	}
}
