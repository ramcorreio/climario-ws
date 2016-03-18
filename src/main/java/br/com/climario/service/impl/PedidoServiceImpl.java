package br.com.climario.service.impl;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.Pedido;
import br.com.climario.persistence.BaseManager;
import br.com.climario.service.IPedidoService;

@Service
@Transactional(value="climarioTM", readOnly = true)
public class PedidoServiceImpl extends BaseManager implements IPedidoService {

	@Transactional(value="climarioTM", readOnly = false)
	public Pedido criar(Pedido pedido) {
	
		create(pedido);
		return pedido;
	}
	
	@Transactional(value="climarioTM", readOnly = false)
	public Cliente criarCliente(Cliente cliente) {
		
		create(cliente);
		return cliente;
	}
	
	@Override
	public boolean isClienteExiste(String codigo) {
		
		TypedQuery<Cliente> query = createNamedQuery("Cliente.existe", Cliente.class);
		query.setParameter("codigo", codigo);
		return !query.getResultList().isEmpty();
	}
	
	@Override
	public Cliente recuperarCliente(String codigo) {
		
		TypedQuery<Cliente> query = createNamedQuery("Cliente.existe", Cliente.class);
		query.setParameter("codigo", codigo);
		try {
			Cliente c = query.getSingleResult();
			Hibernate.initialize(c.getPedidos());
			return c;
		}
		catch(NoResultException e){
			throw new RuntimeException("Cliente não encontrado.", e);
		}
	}
}
