package br.com.climario.ui;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
import br.com.climario.service.impl.ServiceLocator;

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
	public void pagamentos() {
		
		Map<String, String> map = new HashMap<>();
		map.put("paymentMethods", "{\"BOLETO\":{\"name\":\"BOLETO\",\"options\":{\"BOLETO\":{\"name\":\"BOLETO\",\"displayName\":\"Boleto\",\"status\":\"AVAILABLE\",\"code\":202,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/booklet.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/booklet.png\"}}}},\"code\":2},\"BALANCE\":{\"name\":\"BALANCE\",\"options\":{\"BALANCE\":{\"name\":\"BALANCE\",\"displayName\":\"Saldo PagSeguro\",\"status\":\"AVAILABLE\",\"code\":401,\"images\":null}},\"code\":4},\"ONLINE_DEBIT\":{\"name\":\"ONLINE_DEBIT\",\"options\":{\"BANCO_BRASIL\":{\"name\":\"BANCO_BRASIL\",\"displayName\":\"Banco do Brasil\",\"status\":\"UNAVAILABLE\",\"code\":304,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/bb.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/bb.png\"}}},\"BANRISUL\":{\"name\":\"BANRISUL\",\"displayName\":\"Banco Banrisul\",\"status\":\"AVAILABLE\",\"code\":306,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/banrisul.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/banrisul.png\"}}},\"BRADESCO\":{\"name\":\"BRADESCO\",\"displayName\":\"Banco Bradesco\",\"status\":\"UNAVAILABLE\",\"code\":301,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/bradesco.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/bradesco.png\"}}},\"HSBC\":{\"name\":\"HSBC\",\"displayName\":\"Banco HSBC\",\"status\":\"AVAILABLE\",\"code\":307,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/hsbc.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/hsbc.png\"}}},\"ITAU\":{\"name\":\"ITAU\",\"displayName\":\"Banco Itaú\",\"status\":\"AVAILABLE\",\"code\":302,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/itau.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/itau.png\"}}}},\"code\":3},\"CREDIT_CARD\":{\"name\":\"CREDIT_CARD\",\"options\":{\"AMEX\":{\"name\":\"AMEX\",\"displayName\":\"American Express\",\"status\":\"AVAILABLE\",\"code\":103,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/amex.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/amex.png\"}}},\"AURA\":{\"name\":\"AURA\",\"displayName\":\"Aura\",\"status\":\"AVAILABLE\",\"code\":106,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/aura.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/aura.png\"}}},\"AVISTA\":{\"name\":\"AVISTA\",\"displayName\":\"Avista\",\"status\":\"UNAVAILABLE\",\"code\":118,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/avista.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/avista.png\"}}},\"BRASILCARD\":{\"name\":\"BRASILCARD\",\"displayName\":\"BrasilCard\",\"status\":\"AVAILABLE\",\"code\":112,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/brasilcard.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/brasilcard.png\"}}},\"CABAL\":{\"name\":\"CABAL\",\"displayName\":\"Cabal\",\"status\":\"AVAILABLE\",\"code\":116,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/cabal.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/cabal.png\"}}},\"CARDBAN\":{\"name\":\"CARDBAN\",\"displayName\":\"CARDBAN\",\"status\":\"UNAVAILABLE\",\"code\":114,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/cardban.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/cardban.png\"}}},\"DINERS\":{\"name\":\"DINERS\",\"displayName\":\"Diners\",\"status\":\"AVAILABLE\",\"code\":104,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/diners.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/diners.png\"}}},\"ELO\":{\"name\":\"ELO\",\"displayName\":\"Elo\",\"status\":\"AVAILABLE\",\"code\":107,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/elo.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/elo.png\"}}},\"FORTBRASIL\":{\"name\":\"FORTBRASIL\",\"displayName\":\"FORTBRASIL\",\"status\":\"AVAILABLE\",\"code\":113,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/fortbrasil.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/fortbrasil.png\"}}},\"GRANDCARD\":{\"name\":\"GRANDCARD\",\"displayName\":\"GRANDCARD\",\"status\":\"AVAILABLE\",\"code\":119,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/grandcard.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/grandcard.png\"}}},\"HIPERCARD\":{\"name\":\"HIPERCARD\",\"displayName\":\"Hipercard\",\"status\":\"AVAILABLE\",\"code\":105,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/hipercard.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/hipercard.png\"}}},\"MAIS\":{\"name\":\"MAIS\",\"displayName\":\"Mais!\",\"status\":\"AVAILABLE\",\"code\":117,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/mais.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/mais.png\"}}},\"MASTERCARD\":{\"name\":\"MASTERCARD\",\"displayName\":\"MasterCard\",\"status\":\"AVAILABLE\",\"code\":102,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/mastercard.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/mastercard.png\"}}},\"PERSONALCARD\":{\"name\":\"PERSONALCARD\",\"displayName\":\"PersonalCard\",\"status\":\"AVAILABLE\",\"code\":109,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/personalcard.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/personalcard.png\"}}},\"PLENOCARD\":{\"name\":\"PLENOCARD\",\"displayName\":\"PLENOCard\",\"status\":\"UNAVAILABLE\",\"code\":108,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/plenocard.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/plenocard.png\"}}},\"SOROCRED\":{\"name\":\"SOROCRED\",\"displayName\":\"Sorocred\",\"status\":\"AVAILABLE\",\"code\":120,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/sorocred.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/sorocred.png\"}}},\"VALECARD\":{\"name\":\"VALECARD\",\"displayName\":\"VALECARD\",\"status\":\"AVAILABLE\",\"code\":115,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/valecard.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/valecard.png\"}}},\"VISA\":{\"name\":\"VISA\",\"displayName\":\"Visa\",\"status\":\"AVAILABLE\",\"code\":101,\"images\":{\"SMALL\":{\"size\":\"SMALL\",\"path\":\"/public/img/payment-methods-flags/42x20/visa.png\"},\"MEDIUM\":{\"size\":\"MEDIUM\",\"path\":\"/public/img/payment-methods-flags/68x30/visa.png\"}}}},\"code\":1}}");
		
		pedidoView.setTipo("CREDIT_CARD");
		//pedidoView.processarPagamentos(map);
		assertThat(pedidoView.getCards(), hasSize(15));
		
		pedidoView.setTipo("BOLETO");
		//pedidoView.processarPagamentos(map);
		assertThat(pedidoView.getCards(), hasSize(1));
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
	
	@Test
	public void getProperty() throws IOException {
		
		assertThat("sandbox", is(equalTo(ServiceLocator.getInstance().getProperty(ServiceLocator.ENV))));
	}
	
}
