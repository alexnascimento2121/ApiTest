package com.api.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.ObjetoErro;
import com.api.model.Usuario;
import com.api.repository.UsuarioRepository;
import com.api.service.ServiceEnviaEmail;

@RestController
@RequestMapping(value="/recuperar")
public class RecuperaController {
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ServiceEnviaEmail enviaEmail;
	
	@ResponseBody
	@PostMapping(value="/",produces = "application/json")
	public ResponseEntity<ObjetoErro> recuperar(@RequestBody Usuario login) throws Exception{
		ObjetoErro erro = new ObjetoErro();
		
		Usuario user = usuarioRepo.findByLogin(login.getLogin());
		if(user == null) {
			erro.setCode("404");
			erro.setError("Usuario não Encontrado");
		}else {
			
			
			
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime());
			String senhacripto= new BCryptPasswordEncoder().encode(senhaNova);
			usuarioRepo.updateSenha(senhacripto, user.getId());
			
			enviaEmail.enviarEmail("Recuperacao de Senha", user.getLogin(), "Sua nova Senha é: "+senhaNova);
			
			erro.setCode("200");
			erro.setError("Acesso enviado para seu E-mail");
		}
		return new ResponseEntity<ObjetoErro>(erro,HttpStatus.OK);
	}

}
