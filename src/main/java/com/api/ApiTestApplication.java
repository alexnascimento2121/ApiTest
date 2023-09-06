package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"com.api.model"})// configura as tabelas no banco
@ComponentScan(basePackages = {"com.*"})// injeção de dependecias
@EnableJpaRepositories(basePackages = {"com.api.repository"}) // configura os repositories
@EnableTransactionManagement // controla as gerencia de transações
@EnableWebMvc // ativa o modo mvc do projeto
@RestController // defini como projeto rest
@EnableAutoConfiguration // ativa o modo auto configuraçaõ do spring
@EnableCaching // habilita o uso do cache em endpoints especificos
public class ApiTestApplication implements WebMvcConfigurer{
	// WebMvcConfigurer permite a configuração global para liberacao dos endpoints ao inves do individual
	public static void main(String[] args) {
		SpringApplication.run(ApiTestApplication.class, args);
		/*uso para encriptar senha para teste no postman*/
		//System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		//registry.addMapping("/**"); libera para todos os controlles e endepoints
		registry.addMapping("/usuario/**") //libera todo os endpoints dentro do controller usuario
		.allowedMethods("*")//libera os tipos de endpoints
		.allowedOrigins("*");
		//.allowedOrigins("www.google.com","localhost:8080");//libera apenas para o google
		
		
		registry.addMapping("/profissao/**") //libera todo os endpoints dentro do controller profissao
		.allowedMethods("*")//libera os tipos de endpoints
		.allowedOrigins("*");
		
		registry.addMapping("/recuperar/**") //libera todo os endpoints dentro do controller profissao
		.allowedMethods("*")//libera os tipos de endpoints
		.allowedOrigins("*");
	}
}
