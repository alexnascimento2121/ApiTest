package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.Profissao;

public interface ProfissaoRepository  extends JpaRepository<Profissao, Long>{

}
