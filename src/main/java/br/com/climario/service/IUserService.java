package br.com.climario.service;

import java.util.List;

import br.com.climario.dominio.Usuario;

public interface IUserService {
	
	public void criar(Usuario usuario);
	
	public Usuario alterar(Usuario usuario);
	
	public boolean isExiste(String login);
	
	public List<Usuario> listarUsuario();

}
