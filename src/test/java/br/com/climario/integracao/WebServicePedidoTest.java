package br.com.climario.integracao;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.Pedido;
import br.com.climario.integracao.WebServicePedido.Code;
import br.com.climario.service.impl.PedidoServiceImplTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:climario-test-context.xml" })
public class WebServicePedidoTest extends JerseyTest {
    
    private static class MyResourceConfig extends ResourceConfig {
        
        public MyResourceConfig() {
        	
            final Resource.Builder resourceBuilder = Resource.builder(WebServicePedido.class);     
            final Resource resource = resourceBuilder.build();
            registerResources(resource);
        }
    }
    
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
                
        return new MyResourceConfig();
    }
    
    @Test
    public void putPedido() {
        
    	Cliente c = new Cliente();
    	c.setCodigo("90283129830912");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("748237489274298");
    	c.setEmail("roororor@hfhfhfh.com");
    	c.setLogradouro("Rua A");
    	c.setNumero("100");
    	c.setComplemento("casa");
    	c.setBairro("Santa Rosa");
    	c.setCidade("Niterói");
    	c.setEstado("RJ");
    	c.setCep("39847345");
    	c.setEmailRca("racrca@hfhfhfh.com");
    	c.setCodigoRca("8934724238");
    	c.setNomeRca("Nome RCA");
    	
        Pedido p = new Pedido();
        p.setNumero("93824093");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        PedidoServiceImplTest.addItem(p, 2);
        
        final Pedido rtw = target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(p), Pedido.class);
        assertThat(rtw, notNullValue());
        assertThat(rtw.getId(), notNullValue());
        assertThat(rtw.getNumero(), is(equalTo(p.getNumero())));
    }
    
    @Test
    public void putPedidoDuplicado() {        
    	
    	Cliente c = new Cliente();
    	c.setCodigo("90283129830912");
    	c.setNome("Teste Ws");
    	c.setCpfCnpj("748237489274298");
    	c.setEmail("roororor@hfhfhfh.com");
    	c.setLogradouro("Rua A");
    	c.setNumero("100");
    	c.setComplemento("casa");
    	c.setBairro("Santa Rosa");
    	c.setCidade("Niterói");
    	c.setEstado("RJ");
    	c.setCep("39847345");
    	c.setEmailRca("racrca@hfhfhfh.com");
    	c.setCodigoRca("8934724238");
    	c.setNomeRca("Nome RCA");
    	
        Pedido p = new Pedido();
        p.setNumero("93824098");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        p.setCobranca("Cobrasim");
        p.setPlanoPagamento("Plano 1");
        p.setFilial("Niteroi");
        PedidoServiceImplTest.addItem(p, 2);
        
        final Pedido rtw = target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(p), Pedido.class);
        assertThat(rtw, notNullValue());
        assertThat(rtw.getId(), notNullValue());
        assertThat(rtw.getNumero(), is(equalTo(p.getNumero())));
        
        try {
        	target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(p), Pedido.class);
        	Assert.fail();
        }
        catch(WebApplicationException e) {
        	
        	assertThat(e.getResponse().getStatus(), is(equalTo(Status.PRECONDITION_FAILED.getStatusCode())));
        	assertThat(e.getResponse().readEntity(Code.class), is(equalTo(Code.PEDIDO_EXISTE)));
        }
    }
    
    @Test
    public void putPedidoText() throws UnknownHostException, IOException, ParseException {
        
    	String input = "{"
    			+ "\"numero\":\"93824094\","
    			+ "\"criacao\":\"2016-03-18 00:00:00\","
    			+ "\"cliente\":{"
    				+ "\"codigo\":\"90283129830912\","
    				+ "\"nome\":\"Teste Ws\","
    				+ "\"cpfCnpj\":\"748237489274298\","
    				+ "\"email\":\"roororor@hfhfhfh.com\","
    				+ "\"logradouro\":\"Rua A\","
    				+ "\"numero\":\"100\","
    				+ "\"complemento\":\"casa\","
    				+ "\"bairro\":\"Santa Rosa\","
    				+ "\"cidade\":\"Niterói\","
    				+ "\"estado\":\"RJ\","
    				+ "\"cep\":\"39847345\","
    				+ "\"emailRca\":\"racrca@hfhfhfh.com\","
    				+ "\"codigoRca\":\"8934724238\","
    				+ "\"nomeRca\":\"Nome RCA\""
    			+ "},"
    			+ "\"filial\":\"Niteroi\","
    			+ "\"valorFrete\":0.0,"
    			+ "\"planoPagamento\":\"Plano 1\","
    			+ "\"cobranca\":\"Cobrasim\","
    			+ "\"itens\": [{\"codigo\":\"2\",\"descricao\":\"Descrição 2\",\"qtd\":8,\"precoUnitario\":1.9320663346223177},{\"codigo\":\"5\",\"descricao\":\"Descrição 3\",\"qtd\":2,\"precoUnitario\":200.2}]"
    		+"}";
        
    	Pedido rtw = target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(input), Pedido.class);
    	            
    	assertThat(rtw, notNullValue());
        assertThat(rtw.getId(), notNullValue());
        assertThat(rtw.getNumero(), is(equalTo("93824094")));
        assertThat(rtw.getCriacao(), is(equalTo(DateAdapter.dateFormat.parse("2016-03-18 00:00:00"))));
    }
    
    @Test
    public void getPedidoPorCliente() throws UnknownHostException, IOException {
        
    	String input1 = "{"
    			+ "\"numero\":\"93824095\","
    			+ "\"criacao\":\"2016-03-18 00:00:00\","
    			+ "\"cliente\":{"
    				+ "\"codigo\":\"90283129830912\","
    				+ "\"nome\":\"Teste Ws\","
    				+ "\"cpfCnpj\":\"748237489274298\","
    				+ "\"email\":\"roororor@hfhfhfh.com\","
    				+ "\"logradouro\":\"Rua A\","
    				+ "\"numero\":\"100\","
    				+ "\"complemento\":\"casa\","
    				+ "\"bairro\":\"Santa Rosa\","
    				+ "\"cidade\":\"Niterói\","
    				+ "\"estado\":\"RJ\","
    				+ "\"cep\":\"39847345\","
    				+ "\"emailRca\":\"racrca@hfhfhfh.com\","
    				+ "\"codigoRca\":\"8934724238\","
    				+ "\"nomeRca\":\"Nome RCA\""
    			+ "},"
    			+ "\"filial\":\"Niteroi\","
    			+ "\"valorFrete\":0.0,"
    			+ "\"planoPagamento\":\"Plano 1\","
    			+ "\"cobranca\":\"Cobrasim\","
    			+ "\"itens\": [{\"codigo\":\"2\",\"descricao\":\"Descrição 2\",\"qtd\":8,\"precoUnitario\":1.9320663346223177},{\"codigo\":\"5\",\"descricao\":\"Descrição 3\",\"qtd\":2,\"precoUnitario\":200.2}]"
    		+"}";
    	target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(input1), Pedido.class);
    	
    	String input2 = "{"
    			+ "\"numero\":\"93824096\","
    			+ "\"criacao\":\"2016-03-18 00:00:00\","
    			+ "\"cliente\":{"
    				+ "\"codigo\":\"90283129830912\","
    				+ "\"nome\":\"Teste Ws\","
    				+ "\"cpfCnpj\":\"748237489274298\","
    				+ "\"email\":\"roororor@hfhfhfh.com\","
    				+ "\"logradouro\":\"Rua A\","
    				+ "\"numero\":\"100\","
    				+ "\"complemento\":\"casa\","
    				+ "\"bairro\":\"Santa Rosa\","
    				+ "\"cidade\":\"Niterói\","
    				+ "\"estado\":\"RJ\","
    				+ "\"emailRca\":\"racrca@hfhfhfh.com\","
    				+ "\"codigoRca\":\"8934724238\","
    				+ "\"nomeRca\":\"Nome RCA\""
    			+ "},"
    			+ "\"filial\":\"Niteroi\","
    			+ "\"valorFrete\":0.0,"
    			+ "\"planoPagamento\":\"Plano 1\","
    			+ "\"cobranca\":\"Cobrasim\","
    			+ "\"itens\": [{\"codigo\":\"2\",\"descricao\":\"Descrição 2\",\"qtd\":8,\"precoUnitario\":1.9320663346223177},{\"codigo\":\"5\",\"descricao\":\"Descrição 3\",\"qtd\":2,\"precoUnitario\":200.2}]"
    		+"}";
    	target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(input2), Pedido.class);
    	
    	String input3 = "{"
    			+ "\"numero\":\"93824097\","
    			+ "\"criacao\":\"2016-03-18 00:00:00\","
    			+ "\"cliente\":{"
    				+ "\"codigo\":\"90283129830912\","
    				+ "\"nome\":\"Teste Ws\","
    				+ "\"cpfCnpj\":\"748237489274298\","
    				+ "\"email\":\"roororor@hfhfhfh.com\","
    				+ "\"logradouro\":\"Rua A\","
    				+ "\"numero\":\"100\","
    				+ "\"complemento\":\"casa\","
    				+ "\"bairro\":\"Santa Rosa\","
    				+ "\"cidade\":\"Niterói\","
    				+ "\"estado\":\"RJ\","
    				+ "\"emailRca\":\"racrca@hfhfhfh.com\","
    				+ "\"codigoRca\":\"8934724238\","
    				+ "\"nomeRca\":\"Nome RCA\""
    			+ "},"
    			+ "\"filial\":\"Niteroi\","
    			+ "\"valorFrete\":0.0,"
    			+ "\"planoPagamento\":\"Plano 1\","
    			+ "\"cobranca\":\"Cobrasim\","
    			+ "\"itens\": [{\"codigo\":\"2\",\"descricao\":\"Descrição 2\",\"qtd\":8,\"precoUnitario\":1.9320663346223177},{\"codigo\":\"5\",\"descricao\":\"Descrição 3\",\"qtd\":2,\"precoUnitario\":200.2}]"
    		+"}";
    	target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(input3), Pedido.class);
    	
    	List<Pedido> pedidos = target().path("pedido-ws").path("pedidos").queryParam("idCliente", "748237489274298").request(MediaType.APPLICATION_JSON).get(new GenericType<List<Pedido>>() {});
    	assertThat(pedidos.size(), greaterThan(2));
    	
    }
}
