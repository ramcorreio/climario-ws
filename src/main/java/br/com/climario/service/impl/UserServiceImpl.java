package br.com.climario.service.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.climario.dominio.Usuario;
import br.com.climario.persistence.BaseManager;
import br.com.climario.service.IUserService;

@Service
@Transactional(value="climarioTM", readOnly = true)
public class UserServiceImpl extends BaseManager implements IUserService {

	@Override
	@Transactional(value="climarioTM", readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void criar(Usuario usuario) {
		create(usuario);
	}
	
	@Override
	@Transactional(value="climarioTM", readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Usuario alterar(Usuario usuario) {

		return update(usuario);
	}
	
	@Override
	public boolean isExiste(String login) {
		
		TypedQuery<Usuario> query = createNamedQuery("Usuario.existe", Usuario.class);
		query.setParameter("login", login);
		return !query.getResultList().isEmpty();
	}
	
	@Override
	public boolean doLogin(String login, String senha) {
		
		TypedQuery<Usuario> query = createNamedQuery("Usuario.do.login", Usuario.class);
		query.setParameter("login", login);
		query.setParameter("senha", senha);
		try {
			Usuario u = query.getSingleResult();
			return true;
		}
		catch(NoResultException e) {
			return false;
		}
	}
	
	@Override
	public List<Usuario> listarUsuario() {
		
		TypedQuery<Usuario> query = createNamedQuery("Usuario.all", Usuario.class);
		return query.getResultList();
	}
}
