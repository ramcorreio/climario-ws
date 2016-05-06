package br.com.climario.integracao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.Pedido;

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
    	
        Pedido p = new Pedido();
        p.setNumero("93824093");
        p.setCriacao(Calendar.getInstance().getTime());
        p.setCliente(c);
        //PedidoServiceImplTest.addItem(p, 2);
        
        final Pedido rtw = target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(p), Pedido.class);
        MatcherAssert.assertThat(rtw, Matchers.notNullValue());
        MatcherAssert.assertThat(rtw.getId(), Matchers.notNullValue());
        MatcherAssert.assertThat(rtw.getNumero(), Matchers.is(Matchers.equalTo(p.getNumero())));
    }
    
    @Test
    public void putPedidoText() throws UnknownHostException, IOException, ParseException {
        
    	//String input = "{\"numero\":\"93824094\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"}}";
    	//String input = "{\"numero\":\"93824094\",\"criacao\":\"2016-03-18 00:00:00\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"},\"itens\":[{\"codigo\":\"2\",\"descricao\":\"Descrição 2\",\"qtd\":8,\"precoUnitario\":1.9320663346223177},{\"codigo\":\"1\",\"descricao\":\"Descrição 1\",\"qtd\":6,\"precoUnitario\":8.83666440420624}]}";
    	String input = "{\"numero\":\"93824094\",\"criacao\":\"2016-03-18 00:00:00\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"}}";
        
    	Pedido rtw = target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(input), Pedido.class);
    	            
    	MatcherAssert.assertThat(rtw, Matchers.notNullValue());
        MatcherAssert.assertThat(rtw.getId(), Matchers.notNullValue());
        MatcherAssert.assertThat(rtw.getNumero(), Matchers.is(Matchers.equalTo("93824094")));
        MatcherAssert.assertThat(rtw.getCriacao(), Matchers.equalTo(DateAdapter.dateFormat.parse("2016-03-18 00:00:00")));
    }
    
    @Test
    public void getPedidoPorCliente() throws UnknownHostException, IOException {
        
    	//String input1 = "{\"numero\":\"93824095\",\"criacao\":\"2016-03-18 00:00:00\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"},\"itens\":[{\"codigo\":\"2\",\"descricao\":\"Descrição 2\",\"qtd\":6,\"precoUnitario\":1.9320663346223177},{\"codigo\":\"1\",\"descricao\":\"Descrição 1\",\"qtd\":2,\"precoUnitario\":8.83666440420624}]}";
    	String input1 = "{\"numero\":\"93824095\",\"criacao\":\"2016-03-18 00:00:00\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"}}";
    	target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(input1), Pedido.class);
    	
    	//String input2 = "{\"numero\":\"93824096\",\"criacao\":\"2016-03-18 00:00:00\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"},\"itens\":[{\"codigo\":\"2\",\"descricao\":\"Descrição 2\",\"qtd\":2,\"precoUnitario\":1.9320663346223177},{\"codigo\":\"1\",\"descricao\":\"Descrição 1\",\"qtd\":3,\"precoUnitario\":8.83666440420624}]}";
    	String input2 = "{\"numero\":\"93824096\",\"criacao\":\"2016-03-18 00:00:00\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"}}";
    	target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(input2), Pedido.class);
    	
    	//String input3 = "{\"numero\":\"93824097\",\"criacao\":\"2016-03-18 00:00:00\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"},\"itens\":[{\"codigo\":\"2\",\"descricao\":\"Descrição 2\",\"qtd\":1,\"precoUnitario\":1.9320663346223177},{\"codigo\":\"1\",\"descricao\":\"Descrição 1\",\"qtd\":1,\"precoUnitario\":8.83666440420624}]}";
    	String input3 = "{\"numero\":\"93824097\",\"criacao\":\"2016-03-18 00:00:00\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"}}";
    	target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(input3), Pedido.class);
    	
    	List<Pedido> pedidos = target().path("pedido-ws").path("pedidos").queryParam("idCliente", "90283129830912").request(MediaType.APPLICATION_JSON).get(new GenericType<List<Pedido>>() {});
    	MatcherAssert.assertThat(pedidos.size(), Matchers.greaterThan(2));
    	
    }
}
