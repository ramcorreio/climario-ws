package br.com.climario.service.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.ItemPedido;
import br.com.climario.dominio.Pedido;
import br.com.climario.persistence.BaseManager;
import br.com.climario.service.IPedidoService;

@Service
@Transactional(value="climarioTM", readOnly = true)
public class PedidoServiceImpl extends BaseManager implements IPedidoService {

	@Transactional(value="climarioTM", readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Pedido criar(Pedido pedido) {
	
		for (ItemPedido item : pedido.getItens()) {
			
			if(item.getQtd() <= 0 || item.getPrecoUnitario() < 0 ) {
				throw new IllegalArgumentException("item: " + item.getCodigo());
			}
		}
		
		create(pedido);
		return pedido;
	}
	
	@Transactional(value="climarioTM", readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Cliente criarCliente(Cliente cliente) {
		
		create(cliente);
		return cliente;
	}
	
	@Override
	public boolean isPedidoClienteExiste(String cpfCnpj, String numero) {
		
		TypedQuery<Pedido> query = createNamedQuery("Pedido.cliente.existe", Pedido.class);
		query.setParameter("numero", numero);
		query.setParameter("cpfCnpj", cpfCnpj);
		return !query.getResultList().isEmpty();
	}
	
	@Override
	public boolean isClienteExiste(String cpfCnpj) {
		
		TypedQuery<Cliente> query = createNamedQuery("Cliente.existe", Cliente.class);
		query.setParameter("cpfCnpj", cpfCnpj);
		return !query.getResultList().isEmpty();
	}
	
	@Override
	public boolean isPedidoExiste(String numero) {
		
		TypedQuery<Pedido> query = createNamedQuery("Pedido.existe", Pedido.class);
		query.setParameter("numero", numero);
		return !query.getResultList().isEmpty();
	}
	
	@Override
	public Cliente recuperarCliente(String cpfCnpj) {
		
		TypedQuery<Cliente> query = createNamedQuery("Cliente.existe", Cliente.class);
		query.setParameter("cpfCnpj", cpfCnpj);
		try {
			return query.getSingleResult();
		}
		catch(NoResultException e){
			throw new RuntimeException("Cliente não encontrado.", e);
		}
	}
	
	@Override
	public Pedido recuperarPedido(String numero) {
		
		TypedQuery<Pedido> query = createNamedQuery("Pedido.existe", Pedido.class);
		query.setParameter("numero", numero);
		try {
			return query.getSingleResult();
		}
		catch(NoResultException e){
			throw new RuntimeException("Pediod não encontrado.", e);
		}
	}
	
	@Override
	public List<Pedido> listarPedidosPorCliente(String cpfCnpj) {
		
		if(cpfCnpj == null){
			throw new NullPointerException("cnpjCnpj");
		}
		else if(cpfCnpj.isEmpty()){
			throw new IllegalArgumentException("cnpjCnpj");
		}
		
		TypedQuery<Pedido> query = createNamedQuery("Pedido.por.cliente", Pedido.class);
		query.setParameter("cpfCnpj", cpfCnpj);
		return query.getResultList();
	}
	
	
}
