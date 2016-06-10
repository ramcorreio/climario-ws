package br.com.climario.integracao;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.climario.ui.LoginSession;

public class LoginFilter implements Filter {

	public static final String BEAN_NAME = "loginSession";
	
	private static Logger _logger = LoggerFactory.getLogger(LoginFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean isAdminHome = httpRequest.getRequestURI().replaceAll("admin/", "admin").endsWith("/admin");
        boolean isLoginPage = httpRequest.getRequestURI().contains("entrar.jsf");
        boolean isInstallPage = httpRequest.getRequestURI().contains("install.jsf");
        _logger.info("Contexto " + httpRequest.getContextPath());
        _logger.info("Acessando a " + httpRequest.getRequestURI());
        _logger.info("Admin Home: " + isAdminHome);
        _logger.info("Login Page: " + isLoginPage);
        _logger.info("Install Page: " + isInstallPage);
        
        FacesContext context = FacesContext.getCurrentInstance();
        _logger.info("context => " + context);
        //LoginSession bean = context.getApplication().evaluateExpressionGet(context, "#{loginSession}", LoginSession.class);

        _logger.info("bean session => " + httpRequest.getSession().getAttribute(BEAN_NAME));
        LoginSession session = (LoginSession) httpRequest.getSession().getAttribute(BEAN_NAME);
        
        if(isInstallPage) {
        	chain.doFilter(request, response);
        }
        else {
        	
        	        	
        	//if(!isLoginPage && (session == null || session != null && !session.isLogged())) {
        	//if(session == null || session != null && !session.isLogged()) {
        	if(isAdminHome || !isLoginPage && (session == null || (session != null && !session.isLogged()))) {
            	
            	httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/entrar.jsf");
            }
            else if(isLoginPage && session != null && session.isLogged()) {
            	
            	httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/index.jsf");
            }
            else {
                
            	chain.doFilter(request, response);
            }
        }
    }
    
    public void init(FilterConfig arg0) throws ServletException {
        _logger.info("Inicilizando filtro de login.");
    }

    public void destroy() {
    	_logger.info("Finalizando filtro de login.");
    }


	
}
