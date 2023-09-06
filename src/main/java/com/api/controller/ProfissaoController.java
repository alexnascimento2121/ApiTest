package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.Profissao;
import com.api.repository.ProfissaoRepository;

@RestController
@RequestMapping(value="/profissao")
public class ProfissaoController {
	
	@Autowired
	private ProfissaoRepository profissaoRepo;
	
	@GetMapping(value="/", produces="applicantion/json")
	public ResponseEntity<List<Profissao>> profissoes(){
		List<Profissao> lista =profissaoRepo.findAll();
		return new ResponseEntity<List<Profissao>>(lista,HttpStatus.OK);		
	}

}
