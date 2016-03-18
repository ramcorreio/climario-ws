package br.com.climario.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
}
