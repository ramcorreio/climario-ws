package br.com.climario.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.ItemPedido;
import br.com.climario.dominio.Pedido;
import br.com.climario.dominio.Pedido.Pagagamento;
import br.com.climario.dominio.Pedido.PedidoStatus;
import br.com.climario.persistence.BaseManager;
import br.com.climario.service.IPedidoService;

@Service
@Transactional(value="climarioTM", readOnly = true)
public class PedidoServiceImpl extends BaseManager implements IPedidoService, Serializable {
	

	private static final long serialVersionUID = 6720815186839783215L;

	@Transactional(value="climarioTM", readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Pedido criar(Pedido pedido) {
	
		for (ItemPedido item : pedido.getItens()) {
			
			if(item.getQtd() <= 0 || item.getPrecoUnitario() < 0 ) {
				throw new IllegalArgumentException("item: " + item.getCodigo());
			}
		}
		
		pedido.setCriado(Calendar.getInstance().getTime());
		pedido.setAtualizado(pedido.getCriacao());
		pedido.setStatus(PedidoStatus.AGUARDANDO_PAGAMENTO);
		
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
	public boolean isClienteExiste(String codigo, String email) {
		
		TypedQuery<Cliente> query = createNamedQuery("Cliente.existe.cnpj.email", Cliente.class);
		query.setParameter("cpfCnpj", codigo);
		query.setParameter("email", email);
		return !query.getResultList().isEmpty();
	}
	
	@Override
	public boolean isClienteExiste(String cpfCnpj) {
		
		TypedQuery<Cliente> query = createNamedQuery("Cliente.existe.cnpj", Cliente.class);
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
		
		TypedQuery<Cliente> query = createNamedQuery("Cliente.existe.cnpj", Cliente.class);
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
			throw new RuntimeException("Pedido não encontrado.", e);
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
	
	@Override
	@Transactional(value="climarioTM", readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void atulizarCodigoTransacao(String numero, Pagagamento pagagamento, String transacao, String link) 
	{
		
		Pedido p = recuperarPedido(numero);
		p.setCodigoAutorizacao(transacao);
		p.setPagamento(pagagamento);
		p.setLink(link);
		update(p);
	}
	
	@Override
	@Transactional(value="climarioTM", readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void atulizarStatus(String numero, PedidoStatus status) {
		
		Pedido p = recuperarPedido(numero);
		p.setAtualizado(Calendar.getInstance().getTime());
		p.setStatus(status);
		update(p);
	}
	
	@Override
	public List<Pedido> listar() {
		
		TypedQuery<Pedido> query = createNamedQuery("Pedido.all", Pedido.class);
		return query.getResultList();
	}
}
