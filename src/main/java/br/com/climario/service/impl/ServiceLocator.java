package br.com.climario.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.climario.service.IPedidoService;

/**
 * Classe responsável por disponibilizar o serviços de acesso a base de dados É
 * uma implementação do padrão sigleton
 * 
 * @author rodrigo
 *
 */
public class ServiceLocator {

	private static ServiceLocator INSTANCE;

	@Autowired
	private ApplicationContext applicationContext;

	private ServiceLocator() {
	}

	/**
	 * Método que disponibilizar a instância
	 * 
	 * @return A instância local
	 */
	public static ServiceLocator getInstance() {

		//synchronized (INSTANCE) {

			if (INSTANCE == null) {

				INSTANCE = new ServiceLocator();
			}
		//}

		return INSTANCE;
	}

	public IPedidoService getPedidoSerice() {

		return (IPedidoService) applicationContext.getBean("pedidoService");
	}
}