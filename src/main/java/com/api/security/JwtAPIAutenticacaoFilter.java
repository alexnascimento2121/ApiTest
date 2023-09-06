package com.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
/*filtro onde todas as requisiçoes serao capturadas para autenticar*/
public class JwtAPIAutenticacaoFilter extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		/*estabelece a autenticacao para a requisicao*/
		Authentication authentication = new JWTTokenAutenticacaoService().getAuthentication((HttpServletRequest)request, (HttpServletResponse)response);
		
		/*Coloca o processo de autenticacao no spring security*/
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}

}