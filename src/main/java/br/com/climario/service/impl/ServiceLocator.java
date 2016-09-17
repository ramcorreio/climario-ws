package br.com.climario.service.impl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.climario.service.IPedidoService;
import br.com.climario.service.IUserService;

/**
 * Classe responsável por disponibilizar o serviços de acesso a base de dados É
 * uma implementação do padrão sigleton
 * 
 * @author rodrigo cabeça
 *
 */
public class ServiceLocator {
	
	private static ServiceLocator INSTANCE = new ServiceLocator();
	
	public static final String ENV = "climario.environment";

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

		return INSTANCE;
	}

	public IPedidoService getPedidoService() {

		return (IPedidoService) applicationContext.getBean("pedidoService");
	}
	
	public IUserService getUserService() {

		return (IUserService) applicationContext.getBean("usuarioService");
	}
	
	public String getProperty(String key) {
		
		return Properties.class.cast(applicationContext.getBean("props")).getProperty(key);
	}
}