package br.com.climario.service.impl;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import br.com.climario.dominio.Usuario;
import br.com.climario.persistence.ViolationException;
import br.com.climario.service.IUserService;
import br.com.climario.ui.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:climario-test-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class UserServiceImplTest {
	
	@Autowired
	private IUserService usuarioService;
	
	@Test
	public void criarUsuario() throws NoSuchAlgorithmException, UnsupportedEncodingException {

		Usuario u = new Usuario();
		u.setLogin("admin");
		u.setSenha(Util.criptografarString("123456"));
		
		usuarioService.criar(u);
		assertThat(u.getId(), notNullValue());
		assertThat(u.getId(), greaterThan(0l));
		assertThat(u.getLogin(), is(equalTo("admin")));
		assertThat(u.getSenha(), is(not(equalTo("123456"))));
	}
	
	@Test
	public void alterarUsuario() throws NoSuchAlgorithmException, UnsupportedEncodingException {

		Usuario u = new Usuario();
		u.setLogin("admin2");
		u.setSenha("123456");
		
		usuarioService.criar(u);
		assertThat(u.getId(), notNullValue());
		assertThat(u.getId(), greaterThan(0l));
		assertThat(u.getLogin(), is(equalTo("admin2")));
		assertThat(u.getSenha(), is(equalTo("123456")));
		
		
		u.setSenha(Util.criptografarString("123456"));
		usuarioService.alterar(u);
		assertThat(u.getSenha(), is(not(equalTo("123456"))));
	}
	
	@Test(expected = ViolationException.class)
	public void usuarioDuplicado() throws NoSuchAlgorithmException, UnsupportedEncodingException {

		Usuario u = new Usuario();
		u.setLogin("admin3");
		u.setSenha(Util.criptografarString("123456"));
		
		usuarioService.criar(u);
		assertThat(u.getId(), notNullValue());
		assertThat(u.getId(), greaterThan(0l));
		assertThat(u.getLogin(), is(equalTo("admin3")));
		assertThat(u.getSenha(), is(not(equalTo("123456"))));
		
		Usuario u2 = new Usuario();
		u2.setLogin("admin3");
		u2.setSenha(Util.criptografarString("123456"));
		usuarioService.criar(u2);
	}
	
	@Test
	public void usuarioExiste() throws NoSuchAlgorithmException, UnsupportedEncodingException  {
		
		Usuario u = new Usuario();
		u.setLogin("admin4");
		u.setSenha(Util.criptografarString("123456"));
		
		usuarioService.criar(u);
		assertThat(u.getId(), notNullValue());
		assertThat(u.getId(), greaterThan(0l));
		assertThat(u.getLogin(), is(equalTo("admin4")));
		assertThat(u.getSenha(), is(not(equalTo("123456"))));
		
		assertThat(usuarioService.isExiste("admin4"), is(equalTo(true)));
		
	}

	@Test
	public void listarUsuario() throws NoSuchAlgorithmException, UnsupportedEncodingException  {
		
		Usuario u = new Usuario();
		u.setLogin("admin5");
		u.setSenha(Util.criptografarString("123456"));
		
		usuarioService.criar(u);
		
		Usuario u2 = new Usuario();
		u2.setLogin("admin6");
		u2.setSenha(Util.criptografarString("123456"));
		
		usuarioService.criar(u2);
		
		assertThat(usuarioService.listarUsuario(), hasSize(greaterThan(2)));
	}
	
	@Test
	public void doLogin() {
		
		Usuario u = new Usuario();
		u.setLogin("admin8");
		u.setSenha("adminsenha");
		
		usuarioService.criar(u);
		assertThat(u.getId(), notNullValue());
		assertThat(u.getId(), greaterThan(0l));
		assertThat(u.getLogin(), is(equalTo("admin8")));
		assertThat(u.getSenha(), is(equalTo("adminsenha")));
		
		assertThat(usuarioService.doLogin("admin8", "adminsenha"), is(equalTo(true)));
		assertThat(usuarioService.doLogin("admin8", "adminsenhaoooo"), is(equalTo(false)));
	}
}