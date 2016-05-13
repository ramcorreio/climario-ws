package br.com.climario.ui;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.PedidoServiceImplTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:climario-test-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class PedidoViewTest {

	private PedidoView pedidoView;
	
	@Autowired
	private IPedidoService pedidoService;
	
	@Before
	public void init() {
		
		pedidoView = new PedidoView();
	}
	
	@Test
	@Ignore
	public void pagar() {
		
		Cliente c = new Cliente();
    	c.setCodigo("8247329483749");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("07828359705");
    	c.setEmail("roororor@hfhfhfh.com");
    	c.setLogradouro("Rua A");
    	c.setNumero("100");
    	c.setComplemento("casa");
    	c.setBairro("Santa Rosa");
    	c.setCidade("Niterói");
    	c.setEstado("RJ");
    	c.setCep("38924923");
    	c.setEmailRca("racrca@hfhfhfh.com");
    	c.setCodigoRca("8934724238");
    	c.setNomeRca("Nome RCA");
    	c = pedidoService.criarCliente(c);
    	
        Pedido p = new Pedido();
        p.setNumero("3298446");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        PedidoServiceImplTest.addItem(p, 2);
        
		pedidoService.criar(p);
		
		pedidoView.setPedido(p);
		pedidoView.checkout(null);
		//pedidoView.pagar(null);
	}
	
}
