package br.com.climario.integracao;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiFilter implements Filter {
	
	private static Logger _logger = LoggerFactory.getLogger(ApiFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = HttpServletRequest.class.cast(request);
		
		_logger.info("processando api...");
		_logger.info("host: " + httpRequest.getRemoteHost());
		_logger.info("addr: " + httpRequest.getRemoteAddr());
		_logger.info("url: " + httpRequest.getRequestURL());
		_logger.info("uri: " + httpRequest.getRequestURI());
		chain.doFilter(request, response);
		//_logger.debug("Bytes lidos: " + out.length());
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
	
	/*private String readBody(HttpServletRequest request) throws ServletException{

        try {
            byte bytes[] = new byte[request.getContentLength()];
            int reads = request.getInputStream().read(bytes);
            
            if(reads == -1) {
                return "";
            }
            else {
            	return new String(bytes);	
            }
        } catch (IOException e) {
            
            throw new ServletException("Requisição inválida.", e);
        }
	}*/

}
