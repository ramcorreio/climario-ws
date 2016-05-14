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

import br.com.climario.dominio.Usuario;
import br.com.climario.service.IUserService;
import br.com.climario.service.impl.ServiceLocator;

@ManagedBean
@SessionScoped
public class LoginSession implements Serializable {
	
	
	private static final long serialVersionUID = 6689087374231436459L;
	
	private IUserService userService = ServiceLocator.getInstance().getUserService(); 
	
	
	public void initLogin() {
		System.out.println("initLogin...");
		
		if(userService.listarUsuario().isEmpty()) {
			Util.redirect(Util.getContextRoot("/admin/install.jsf"));
		}
	}
	
	public void doLogin(ActionEvent actionEvent) {
		
		System.out.println(actionEvent);
		System.out.println(actionEvent.getComponent().findComponent("login"));
		System.out.println(actionEvent.getComponent().findComponent("senha"));
	}
	
	public void doInstall(ActionEvent actionEvent) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		InputText login = (InputText) actionEvent.getComponent().findComponent("login");
		Password senha = (Password) actionEvent.getComponent().findComponent("senha");
		Password confirma = (Password) actionEvent.getComponent().findComponent("confirmasenha");
		
		if(!senha.getValue().toString().equals(confirma.getValue().toString())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha não confirma!", "Erro!"));
		}
		
		Usuario u = new Usuario();
		u.setLogin(login.getValue().toString());
		u.setSenha(Util.criptografarString(login.getValue().toString()));
		userService.criar(u);
		Util.redirect(Util.getContextRoot("/admin.jsf"));
	}

}
