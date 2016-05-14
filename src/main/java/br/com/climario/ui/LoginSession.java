package br.com.climario.ui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class LoginSession {
	
	
	public void initLogin() {
		
	}
	
	public void doLogin(ActionEvent actionEvent) {
		
		System.out.println(actionEvent);
		System.out.println(actionEvent.getComponent().findComponent("login"));
		System.out.println(actionEvent.getComponent().findComponent("senha"));
	}
	
	public void doInstall(ActionEvent actionEvent) {
		
		System.out.println(actionEvent);
		System.out.println(actionEvent.getComponent().findComponent("login"));
		System.out.println(actionEvent.getComponent().findComponent("senha"));
		
		
	}

}
