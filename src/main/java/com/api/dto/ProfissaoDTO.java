package com.api.dto;

import java.io.Serializable;

public class ProfissaoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3750180712967654960L;
	
	private Long Id;
	
	private String descricao;

	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	
}
