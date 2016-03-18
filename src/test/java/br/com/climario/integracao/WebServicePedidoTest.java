package br.com.climario.integracao;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
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
                
        MyResourceConfig config = new MyResourceConfig();
        
        return config;
    }
    
    @Test
    public void postPedido() {
        
    	Cliente c = new Cliente();
    	c.setCodigo("90283129830912");
    	c.setNome("Teste Ws");
    	
        Pedido p = new Pedido();
        p.setNumero("93824093");
        p.setCliente(c);
        
        final Pedido rtw = target().path("pedido-ws").path("enviar").request(MediaType.APPLICATION_JSON).put(Entity.json(p), Pedido.class);
        MatcherAssert.assertThat(rtw, Matchers.notNullValue());
        MatcherAssert.assertThat(rtw.getId(), Matchers.notNullValue());
        MatcherAssert.assertThat(rtw.getNumero(), Matchers.is(Matchers.equalTo(p.getNumero())));
        //Assert.assertEquals("velho-mac", rtw.getLastMac());
        //Assert.assertEquals("velho-serial-update", rtw.getLastSerial());
        ///Assert.assertEquals(ModeloTag.STB1, rtw.getValue().getModelo());
        //Assert.assertEquals(IntegratioTag.Operation.UPDATE, rtw.getOperation());
    }
    
    @Test
    @Ignore
    public void postPedidoText() throws UnknownHostException, IOException {
        
    	Cliente c = new Cliente();
    	c.setCodigo("90283129830912");
    	c.setNome("Teste Ws");
    	
        Pedido p = new Pedido();
        p.setNumero("93824093");
        p.setCliente(c);
        
        
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9998));
        //socketChannel.configureBlocking(false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ps.println("PUT /pedido-ws/enviar");
        ps.println("Accept: application/json");
        ps.println("Content-Type: application/json");
        ps.println("{\"numero\":\"93824093\",\"cliente\":{\"codigo\":\"90283129830912\",\"nome\":\"Teste Ws\"}}");
        ps.close();
        
        ByteBuffer buf = ByteBuffer.allocate(2048);
        buf.clear();
        buf.put(baos.toByteArray());
        buf.flip();
        
        while(buf.hasRemaining()) {
            socketChannel.write(buf);
        }
        
        buf = ByteBuffer.allocate(2048);
        int bytesRead = socketChannel.read(buf);
        socketChannel.close();
        System.out.println(new String(buf.array()));
        //final Pedido rtw = target().path("pedido-ws").path("enviar").request().equest(MediaType.APPLICATION_JSON).build("PUT").put(Entity.json(p), Pedido.class);
        //MatcherAssert.assertThat(rtw, Matchers.notNullValue());
        //MatcherAssert.assertThat(rtw.getId(), Matchers.notNullValue());
        //MatcherAssert.assertThat(rtw.getNumero(), Matchers.is(Matchers.equalTo(p.getNumero())));
        //Assert.assertEquals("velho-mac", rtw.getLastMac());
        //Assert.assertEquals("velho-serial-update", rtw.getLastSerial());
        ///Assert.assertEquals(ModeloTag.STB1, rtw.getValue().getModelo());
        //Assert.assertEquals(IntegratioTag.Operation.UPDATE, rtw.getOperation());
       Assert.fail();
    }
}
