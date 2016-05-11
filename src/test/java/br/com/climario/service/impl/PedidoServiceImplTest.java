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
		c.setNome("Cliente de Teste");
		c.setCodigo("07828359705");
		c = pedidoService.criarCliente(c);		
		
		Pedido pedido = new Pedido();
		pedido.setCliente(c);
		pedido.setNumero("3298432");
		pedido.setCriacao(Calendar.getInstance().getTime());
		addItem(pedido, 2);
		
		pedido = pedidoService.criar(pedido);
		assertThat(pedido.getId(), notNullValue());
		assertThat(pedido.getId(), greaterThan(0l));
		assertThat(pedido.getNumero(), is(equalTo("3298432")));
		assertThat(pedido.getCliente().getId(), notNullValue());
		assertThat(pedido.getCliente().getId(), greaterThan(0l));
		assertThat(pedido.getItens().size(), greaterThan(0));
		assertThat(pedido.getItens().size(), is(equalTo(2)));
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
		c.setNome("Cliente de Teste");
		c.setCodigo("07828359712");
		c = pedidoService.criarCliente(c);
		
		
		Pedido pedido = new Pedido();
		pedido.setCliente(c);
		pedido.setNumero("3298442");
		pedido.setCriacao(Calendar.getInstance().getTime());
		addItem(pedido, 5);
		
		pedido = pedidoService.criar(pedido);
		assertThat(pedido.getId(), notNullValue());
		assertThat(pedido.getId(), greaterThan(0l));
		assertThat(pedido.getNumero(), is(equalTo("3298442")));
		assertThat(pedido.getCliente().getId(), notNullValue());
		assertThat(pedido.getCliente().getId(), greaterThan(0l));
		assertThat(pedido.getItens().size(), greaterThan(0));
		assertThat(pedido.getItens().size(), is(equalTo(5)));
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void criarPedidoComItensPrecoNegativo() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste");
		c.setCodigo("0782835998798398439");
		c = pedidoService.criarCliente(c);
		
		
		Pedido pedido = new Pedido();
		pedido.setCliente(c);
		pedido.setNumero("3298446");
		pedido.setCriacao(Calendar.getInstance().getTime());
		addItem(pedido, 1);
		pedido.getItens().iterator().next().setPrecoUnitario(-1d);
		
		pedido = pedidoService.criar(pedido);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void criarPedidoComItensQtdZero() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste");
		c.setCodigo("8247329483749");
		c = pedidoService.criarCliente(c);
		
		
		Pedido pedido = new Pedido();
		pedido.setCliente(c);
		pedido.setNumero("3298446");
		pedido.setCriacao(Calendar.getInstance().getTime());
		addItem(pedido, 1);
		pedido.getItens().iterator().next().setQtd(0);
		
		pedido = pedidoService.criar(pedido);
	}
	
	@Test
	public void existeCliente() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste 2");
		c.setCodigo("07828359708");
		c = pedidoService.criarCliente(c);		
		
		assertThat(c.getId(), notNullValue());
		assertThat(c.getId(), greaterThan(0l));
		assertThat(pedidoService.isClienteExiste(c.getCodigo()), is(equalTo(true)));
	}
	
	@Test
	public void recuperarCliente() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste 2");
		c.setCodigo("07828359709");
		c = pedidoService.criarCliente(c);
		
		assertThat(c.getId(), notNullValue());
		assertThat(c.getId(), greaterThan(0l));
		assertThat(pedidoService.isClienteExiste(c.getCodigo()), is(equalTo(true)));
		assertThat(pedidoService.recuperarCliente(c.getCodigo()), BeanMatchers.theSameAs(c));
	}
	
	@Test
	public void pedidosPorCliente() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste Final");
		c.setCodigo("07828359711");
		c = pedidoService.criarCliente(c);		

		Pedido pedido1 = new Pedido();
		pedido1.setCliente(c);
		pedido1.setNumero("3298453");
		pedido1.setCriacao(Calendar.getInstance().getTime());
		addItem(pedido1, 2);
		pedidoService.criar(pedido1);
		
		
		Pedido pedido2 = new Pedido();
		pedido2.setCliente(c);
		pedido2.setNumero("3298434");
		pedido2.setCriacao(Calendar.getInstance().getTime());
		addItem(pedido2, 1);
		pedidoService.criar(pedido2);
		
		
		Pedido pedido3 = new Pedido();
		pedido3.setCliente(c);
		pedido3.setNumero("3298435");
		pedido3.setCriacao(Calendar.getInstance().getTime());
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
		c.setNome("Cliente de Teste Pedido");
		c.setCodigo("07828359713");
		c = pedidoService.criarCliente(c);		

		Pedido pedido = new Pedido();
		pedido.setCliente(c);
		pedido.setNumero("3298453555");
		pedido.setCriacao(Calendar.getInstance().getTime());
		addItem(pedido, 2);
		pedidoService.criar(pedido);
		
		assertThat(pedidoService.isPedidoExiste("3298453555"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoExiste("3498453555"), is(equalTo(false)));
	}
	
	@Test
	public void pedidoCpfExiste() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste Pedido");
		c.setCodigo("08828359713");
		c = pedidoService.criarCliente(c);		

		Pedido pedido = new Pedido();
		pedido.setCliente(c);
		pedido.setNumero("3298453523984");
		pedido.setCriacao(Calendar.getInstance().getTime());
		addItem(pedido, 2);
		pedidoService.criar(pedido);
		
		assertThat(pedidoService.isPedidoExiste("3298453523984"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoExiste("3298453529832"), is(equalTo(false)));
		assertThat(pedidoService.isClienteExiste("08828359713"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoClienteExiste("08828359713", "3298453523984"), is(equalTo(true)));
		assertThat(pedidoService.isPedidoClienteExiste("08828359713", "4905820934923"), is(equalTo(false)));
	}
}
