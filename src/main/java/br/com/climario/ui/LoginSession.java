package br.com.climario.ui;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.password.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.climario.dominio.Usuario;
import br.com.climario.integracao.LoginFilter;
import br.com.climario.service.IUserService;
import br.com.climario.service.impl.ServiceLocator;

@ManagedBean(name = LoginFilter.BEAN_NAME)
@SessionScoped
public class LoginSession implements Serializable {

	private static Logger _logger = LoggerFactory.getLogger(LoginSession.class);
	
	private static final long serialVersionUID = 6689087374231436459L;
	
	private transient IUserService userService = ServiceLocator.getInstance().getUserService();
	
	private boolean logged = false;
	
	public boolean isLogged() {
		return logged;
	}

	/*public void checkLogin() {
		_logger.info("checkLogin...");
		
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		
		if(!ctx.getRequestServletPath().contains("install.jsf")) {
			if(!logged && !ctx.getRequestServletPath().contains("entrar.jsf")) {
				
				Util.redirect(Util.getContextRoot("/admin/entrar.jsf"));
			}	
		}
		else if(!userService.listarUsuario().isEmpty()) {
			Util.redirect(Util.getContextRoot("/admin/entrar.jsf"));			
		}
		//else if(!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().contains("entrar.jsf")) {
		
	}*/
	
	public void initInstall() {
		_logger.info("initInstall...");
		
		if(!userService.listarUsuario().isEmpty()) {
			Util.redirect(Util.getContextRoot("/admin/entrar.jsf"));			
		}
	}
	
	public void initLogin() {
		_logger.info("initLogin...");
		
		if(userService != null && userService.listarUsuario().isEmpty()) {
			Util.redirect(Util.getContextRoot("/admin/install.jsf"));
		}
	}
	
	public void doLogout(ActionEvent actionEvent) {
		
		_logger.info("actionEvent...", actionEvent);
		Util.invalidateSession();
        Util.redirectByContext("/admin");
	}
	
	public void doLogin(ActionEvent actionEvent) {
		
		InputText login = (InputText) actionEvent.getComponent().findComponent("login");
		Password senha = (Password) actionEvent.getComponent().findComponent("senha");
		
		if(userService.doLogin(login.getValue().toString(), senha.getValue().toString())) {
			logged = true;
			Util.redirect(Util.getContextRoot("/admin/index.jsf"));
		}
		else {
			logged = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acesso não autorizado!", "Erro!"));
		}
	}
	
	public void doInstall(ActionEvent actionEvent) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		initInstall();
		
		InputText login = (InputText) actionEvent.getComponent().findComponent("login");
		Password senha = (Password) actionEvent.getComponent().findComponent("senha");
		Password confirma = (Password) actionEvent.getComponent().findComponent("confirmasenha");
		
		if(!senha.getValue().toString().equals(confirma.getValue().toString())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha não confirma!", "Erro!"));
		}
		
		Usuario u = new Usuario();
		u.setLogin(login.getValue().toString());
		//u.setSenha(Util.criptografarString(login.getValue().toString()));
		u.setSenha(senha.getValue().toString());
		userService.criar(u);
		Util.redirect(Util.getContextRoot("/admin/entrar.jsf"));
	}

}
