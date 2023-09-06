package com.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.model.Usuario;
import com.api.repository.UsuarioRepository;
@Service
public class ImplementacaoUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*consulta no banco*/
		Usuario usuario = repository.findByLogin(username);
		if(usuario == null) {
			throw new UsernameNotFoundException("usuario nao encontrado");
		}
		return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
	}

	public void insereAcessoPadrao(Long id) {
		String constraint = repository.consultaConstraintRole();
		if(constraint != null) {
			jdbcTemplate.execute(" ALTER TABLE usuarios_role DROP INDEX  "+constraint);
			//repository.removerConstraint(constraint);
		}		
		repository.insereAcessoRolePadrao(id);
		
	}

}
