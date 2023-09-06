package com.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.api.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
//estabelece o gerenciador de token
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter{
	//configurando o gerenciador de autenticacao
	protected JwtLoginFilter(String url,AuthenticationManager authentication) {
		
		//obriga a autenticar a url
		super(new AntPathRequestMatcher(url));
		//gerenciador de autenticacao
		setAuthenticationManager(authentication);
	}
	/*retorna o usuario ao processar a autenticacao*/
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		/*pegando o token para validar*/
		Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
		
		/*retornar usuario com login, senha e acessos*/
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
	}
}
