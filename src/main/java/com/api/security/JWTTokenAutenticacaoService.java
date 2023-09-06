package com.api.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.api.ApplicationContextLoad;
import com.api.model.Usuario;
import com.api.repository.UsuarioRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {
	//tempo de validade do token de 2 dias
	private static final long  EXPIRATION_TIME=172800000;
	
	// senha unica para compor autenticacao e ajudar na seguranca
	private static final String SECRET="SenhaExtremamenteSecreta";
	// tipo do token
	private static final String TOKEN_PREFIX="Bearer";
	
	private static final String HEADER_STRING="Authorization";
	
	// gerando token e autenticando e adicionando ao cabeçalho e resposta http
	public void addAuthentication(HttpServletResponse response,String username) throws IOException{
		//montagem token
		String JWT=Jwts.builder() //chama token
				.setSubject(username)//add usuario
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))// tempo de expiracao do token
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();//compactacao e geracao de senha
		
		// junta token com prefixo
		String token= TOKEN_PREFIX+" "+JWT;
		//add no cabeçalho http
		response.addHeader(HEADER_STRING, token);//authorization: bearer 4564dada5444´
		
		
		ApplicationContextLoad.getContext().getBean(UsuarioRepository.class).atualizaTokenUser(JWT, username);
		
		/*liberando resposta para portas diferentes que usa API ou caso clientes web*/
			liberacaoCors(response);
		//escreve token como resposta no corpo
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	}
	
	//retorna o usuario validado com token
	public Authentication  getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		//pega token enviado no cabecalho http
		String token = request.getHeader(HEADER_STRING);
		try {
			if(token != null) {
				
				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
				// faz validacao do token do usuario na requisicao
				String user =Jwts.parser().setSigningKey(SECRET)
						.parseClaimsJws(tokenLimpo)
						.getBody().getSubject();
				
				if(user != null ){
					Usuario usuario = ApplicationContextLoad.getContext().getBean(UsuarioRepository.class).findByLogin(user);				
					if(usuario != null) {
						if(tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
						return new UsernamePasswordAuthenticationToken(usuario.getLogin(),usuario.getSenha(),usuario.getAuthorities());
						}
					}
				}
			}
		}catch (ExpiredJwtException e) {
			try {
				response.getOutputStream().println("Seu Token esta expirado,faça o login ou informe um novo Token para validacao");
			} catch (IOException e2) {}
			
		}
		liberacaoCors(response);
			return null;//nao autorizado
		
	}

	private void liberacaoCors(HttpServletResponse response) {
		
		if(response.getHeader("Access-Control-Allow-Origin")== null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Headers")== null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Request-Headers")== null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Methods")== null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}
}
