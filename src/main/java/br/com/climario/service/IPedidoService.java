package br.com.climario.service;

import java.util.List;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.Pedido;

public interface IPedidoService {
	
	public Pedido criar(Pedido pedido);
	
	public List<Pedido> listarPedidosPorCliente(String codigo);
	
	public Cliente criarCliente(Cliente cliente);

	public boolean isPedidoClienteExiste(String codigo, String numero);
	
	public boolean isClienteExiste(String codigo);
	
	public boolean isPedidoExiste(String numero);
	
	public Cliente recuperarCliente(String codigo);
	
	public Pedido recuperarPedido(String numero);

}
