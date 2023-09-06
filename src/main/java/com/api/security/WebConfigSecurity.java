package com.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.api.service.ImplementacaoUserDetailsService;

/*mapeia url,endereço e autoriza ou bloqueia acesso a url*/
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	@Autowired
	private ImplementacaoUserDetailsService implementacao;
	
	// configura as solicitaçoes por http
	@Override
		protected void configure(HttpSecurity http) throws Exception {
			//ativando a protecao contra usuario sem token validado
			http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			//ativando permissao para pagina inicial do sistema
			.disable().authorizeRequests().antMatchers("/").permitAll()
			.antMatchers("/index").permitAll()
			
			.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
			//url de logout- redireciona para index apos deslogar
			.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			
			/*Filtra as requisiçoes de login para autenticacao*/
			.and().addFilterBefore(new JwtLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			/*Filtra as demais requisiçoes para verificar a presenca do token jwt no header http*/
			.addFilterBefore(new JwtAPIAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		}
	
	protected void configure(AuthenticationManagerBuilder auth)throws Exception{
		// serviço que consulta o usuario no bd e encripta a senha
		auth.userDetailsService(implementacao).passwordEncoder(new BCryptPasswordEncoder());
	}
}
