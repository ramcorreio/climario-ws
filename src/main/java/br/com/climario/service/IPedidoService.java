package br.com.climario.service;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.Pedido;

public interface IPedidoService {
	
	public Pedido criar(Pedido pedido);
	
	public Cliente criarCliente(Cliente cliente);

	public boolean isClienteExiste(String codigo);
	
	public Cliente recuperarCliente(String codigo);

}
