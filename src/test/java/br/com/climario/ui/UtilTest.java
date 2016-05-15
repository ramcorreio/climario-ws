package br.com.climario.ui;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.Matchers.*;

import org.junit.Test;



public class UtilTest {
	
	@Test
	public void getTextoSolicitacao() {
		
		Object[] args = new Object[]{"Oi", "OK", "Olá"};
		
		String text1 = Util.getString("texto.solicitacao", args);
		String text2 = Util.getString("texto.solicitacao.info", args);
		
		MatcherAssert.assertThat(text1, is(equalTo("Peço por favor informar o número de pedido para o cliente Oi(OK) telefone Olá.")));
		MatcherAssert.assertThat(text2, is(equalTo("Prezado cliente Oi(OK) telefone Olá.\nSua solicitação foi recebida e responderemos em breve.")));
	}

}
