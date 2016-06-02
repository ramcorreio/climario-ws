package br.com.climario.service.impl;


import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import javax.validation.ConstraintViolationException;

import org.exparity.hamcrest.BeanMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.ItemPedido;
import br.com.climario.dominio.Pedido;
import br.com.climario.dominio.Pedido.Pagagamento;
import br.com.climario.dominio.Pedido.PedidoStatus;
import br.com.climario.service.IPedidoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:climario-test-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class PedidoServiceImplTest {
	
	@Autowired
	IPedidoService pedidoService;
	
	@Test
	public void criarPedido() {

		Cliente c = new Cliente();
    	c.setCodigo("07828359705");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("07828359705");
    	c.setEmail("07828359705@hfhfhfh.com");
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
        p.setNumero("3298432");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        addItem(p, 2);
		
		p = pedidoService.criar(p);
		assertThat(p.getId(), notNullValue());
		assertThat(p.getId(), greaterThan(0l));
		assertThat(p.getNumero(), is(equalTo("3298432")));
		assertThat(p.getCliente().getId(), notNullValue());
		assertThat(p.getCliente().getId(), greaterThan(0l));
		assertThat(p.getItens().size(), greaterThan(0));
		assertThat(p.getItens().size(), is(equalTo(2)));
	}

	public static void addItem(Pedido pedido, int qtd) {
		
		Random r = new Random();
		for (int i = 0; i < qtd; i++) {
			
			ItemPedido item = new ItemPedido();
			item.setCodigo(Integer.toString(i + 1));
			item.setDescricao("Descrição " + item.getCodigo());
			item.setPrecoUnitario(new Double(r.nextDouble() * 100));
			
			do {
				item.setQtd(r.nextInt(10));
			} while (item.getQtd() == 0);
			
			
			
			pedido.getItens().add(item);
		}
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void criarPedidoSemItens() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste");
		c.setCodigo("07828359713");
		c = pedidoService.criarCliente(c);		
		
		Pedido pedido = new Pedido();
		pedido.setCliente(c);
		pedido.setNumero("3298435667");
		pedido.setCriacao(Calendar.getInstance().getTime());
		
		pedido = pedidoService.criar(pedido);
		assertThat(pedido.getId(), notNullValue());
		assertThat(pedido.getId(), greaterThan(0l));
		assertThat(pedido.getNumero(), is(equalTo("3298435667")));
		assertThat(pedido.getCliente().getId(), notNullValue());
		assertThat(pedido.getCliente().getId(), greaterThan(0l));
	}
	
	@Test
	public void criarPedidoComItens() {
		
		Cliente c = new Cliente();
    	c.setCodigo("07828359712");
    	c.setNome("Cliente de Teste");
    	c.setCpfCnpj("07828359712");
    	c.setEmail("07828359712@hfhfhfh.com");
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
		p.setNumero("3298442");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        addItem(p, 5);
		
		p = pedidoService.criar(p);
		assertThat(p.getId(), notNullValue());
		assertThat(p.getId(), greaterThan(0l));
		assertThat(p.getNumero(), is(equalTo("3298442")));
		assertThat(p.getCliente().getId(), notNullValue());
		assertThat(p.getCliente().getId(), greaterThan(0l));
		assertThat(p.getItens().size(), greaterThan(0));
		assertThat(p.getItens().size(), is(equalTo(5)));
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void criarPedidoComItensPrecoNegativo() {
		
		Cliente c = new Cliente();
    	c.setCodigo("0782835998798398439");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("0782835998798398439");
    	c.setEmail("0782835998798398439@hfhfhfh.com");
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
        addItem(p, 1);
        p.getItens().iterator().next().setPrecoUnitario(-1d);
		
		p = pedidoService.criar(p);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void criarPedidoComItensQtdZero() {
		
		Cliente c = new Cliente();
    	c.setCodigo("8247329483749");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("7482374892");
    	c.setEmail("roororor@hfhfhfh.com");
    	c.setLogradouro("Rua A");
    	c.setNumero("100");
    	c.setComplemento("casa");
    	c.setBairro("Santa Rosa");
    	c.setCidade("Niterói");
    	c.setEstado("RJ");
    	c.setEmailRca("racrca@hfhfhfh.com");
    	c.setCodigoRca("8934724238");
    	c.setNomeRca("Nome RCA");
    	
        Pedido p = new Pedido();
        p.setNumero("3298446");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        addItem(p, 1);
        
		p.getItens().iterator().next().setQtd(0);
		
		p = pedidoService.criar(p);
	}
	
	@Test
	public void existeCliente() {
		
		Cliente c = new Cliente();
		c.setCodigo("07828359708");
		c.setNome("Cliente de Teste 2");
    	c.setCpfCnpj("07828359708");
    	c.setEmail("07828359708@hfhfhfh.com");
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
		
		assertThat(c.getId(), notNullValue());
		assertThat(c.getId(), greaterThan(0l));
		assertThat(pedidoService.isClienteExiste(c.getCodigo()), is(equalTo(true)));
	}
	
	@Test
	public void existeClienteEmail() {
		
		Cliente c = new Cliente();
		c.setCodigo("07828359710");
		c.setNome("Cliente de Teste 10");
    	c.setCpfCnpj("07828359710");
    	c.setEmail("07828359710@cpf.com");
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
		
		assertThat(c.getId(), notNullValue());
		assertThat(c.getId(), greaterThan(0l));
		assertThat(pedidoService.isClienteExiste(c.getCpfCnpj(), c.getEmail()), is(equalTo(true)));
		assertThat(pedidoService.isClienteExiste("17828359710", c.getEmail()), is(equalTo(false)));
	}
	
	@Test
	public void recuperarCliente() {
		
		
		Cliente c = new Cliente();
		c.setCodigo("07828359709");
		c.setNome("Cliente de Teste 2");
    	c.setCpfCnpj("07828359709");
    	c.setEmail("07828359709@hfhfhfh.com");
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
		
		assertThat(c.getId(), notNullValue());
		assertThat(c.getId(), greaterThan(0l));
		assertThat(pedidoService.isClienteExiste(c.getCpfCnpj()), is(equalTo(true)));
		assertThat(pedidoService.recuperarCliente(c.getCpfCnpj()), BeanMatchers.theSameAs(c));
	}
	
	@Test
	public void pedidosPorCliente() {
		
		Cliente c = new Cliente();
    	c.setCodigo("07828359711");
    	c.setNome("Cliente de Teste Final");
    	c.setCpfCnpj("07828359711");
    	c.setEmail("07828359711@hfhfhfh.com");
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

		Pedido pedido1 = new Pedido();
        pedido1.setNumero("3298453");
        pedido1.setCriacao(Calendar.getInstance().getTime());
        pedido1.setCliente(c);
        pedido1.setCobranca("Cobrasim");
        pedido1.setPlanoPagamento("Plano 1");
        pedido1.setFilial("Niteroi");
        addItem(pedido1, 2);
		pedidoService.criar(pedido1);
		
		
		Pedido pedido2 = new Pedido();
        pedido2.setNumero("3298434");
        pedido2.setCriacao(Calendar.getInstance().getTime());
        pedido2.setCliente(c);
        pedido2.setCobranca("Cobrasim");
        pedido2.setPlanoPagamento("Plano 1");
        pedido2.setFilial("Niteroi");
        addItem(pedido2, 1);
        pedidoService.criar(pedido2);
		
		
		Pedido pedido3 = new Pedido();
		pedido3.setNumero("3298435");
		pedido3.setCriacao(Calendar.getInstance().getTime());
		pedido3.setCliente(c);
		pedido3.setCobranca("Cobrasim");
		pedido3.setPlanoPagamento("Plano 1");
		pedido3.setFilial("Niteroi");
		addItem(pedido3, 4);
		pedidoService.criar(pedido3);
		
		List<Pedido> pedidos = pedidoService.listarPedidosPorCliente(c.getCodigo());
		
		assertThat(pedidos.size(), greaterThan(2));
		assertThat(pedido1.getItens().size(), greaterThan(0));
		assertThat(pedido1.getItens().size(), is(equalTo(2)));
		assertThat(pedido2.getItens().size(), greaterThan(0));
		assertThat(pedido2.getItens().size(), is(equalTo(1)));
		assertThat(pedido3.getItens().size(), greaterThan(0));
		assertThat(pedido3.getItens().size(), is(equalTo(4)));
	}
	
	@Test(expected = RuntimeException.class)
	public void pedidosPorClienteCodigoEmpty() {
		
		pedidoService.listarPedidosPorCliente("");
	}
	
	@Test(expected = RuntimeException.class)
	public void pedidosPorClienteCodigoNull() {
		
		pedidoService.listarPedidosPorCliente(null);
	}
	
	@Test
	public void pedidoExiste() {
		
		Cliente c = new Cliente();
		c.setCodigo("07828359713");
		c.setNome("Cliente de Teste Pedido");
    	c.setCpfCnpj("07828359713");
    	c.setEmail("07828359713@hfhfhfh.com");
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
		p.setNumero("3298453555");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        addItem(p, 2);
        pedidoService.criar(p);
		
		assertThat(pedidoService.isPedidoExiste("3298453555"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoExiste("3498453555"), is(equalTo(false)));
	}
	
	@Test
	public void pedidoCpfExiste() {
		
		Cliente c = new Cliente();
    	c.setCodigo("08828359713");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("748237489274299");
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
        p.setNumero("3298453523984");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        addItem(p, 2);
		pedidoService.criar(p);
		
		assertThat(pedidoService.isPedidoExiste("3298453523984"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoExiste("3298453529832"), is(equalTo(false)));
		assertThat(pedidoService.isClienteExiste("748237489274299"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoClienteExiste("748237489274299", "3298453523984"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoClienteExiste("748237489274299", "4905820934923"), is(equalTo(false)));
	}
	
	@Test
	public void atulaizarPedido() {
		
		Cliente c = new Cliente();
    	c.setCodigo("09809809834");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("09809809834");
    	c.setEmail("09809809834@hfhfhfh.com");
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
        p.setNumero("3298453523989");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        addItem(p, 2);
		pedidoService.criar(p);
		
		assertThat(pedidoService.isPedidoExiste("3298453523989"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoExiste("32983523423999"), is(equalTo(false)));
		assertThat(pedidoService.isClienteExiste("09809809834"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoClienteExiste("09809809834", "3298453523989"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoClienteExiste("09809809844", "3298453523989"), is(equalTo(false)));
		
		String transacao = Long.toString(System.currentTimeMillis());
		pedidoService.atulizarCodigoTransacao(p.getNumero(), Pagagamento.BOLETO, transacao, null);
		p = pedidoService.recuperarPedido(p.getNumero());
		assertThat(p.getCodigoAutorizacao(), is(equalTo(transacao)));
		assertThat(p.getPagamento(), is(equalTo(Pagagamento.BOLETO)));
		assertThat(p.getLink(), nullValue());
	}
	
	@Test
	public void atulaizarStatusPedido() {
		
		Cliente c = new Cliente();
    	c.setCodigo("09809809444");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("09809809444");
    	c.setEmail("09809809444@hfhfhfh.com");
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
        p.setNumero("3298453523999");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        addItem(p, 2);
		pedidoService.criar(p);
		
		assertThat(pedidoService.isPedidoExiste("3298453523999"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoExiste("3298453523789"), is(equalTo(false)));
		assertThat(pedidoService.isClienteExiste("09809809444"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoClienteExiste("09809809444", "3298453523999"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoClienteExiste("09809809444", "3298453523989"), is(equalTo(false)));
		
		pedidoService.atulizarStatus(p.getNumero(), PedidoStatus.PAGO);
		assertThat(pedidoService.recuperarPedido(p.getNumero()).getStatus(), is(equalTo(PedidoStatus.PAGO)));
		
	}
}