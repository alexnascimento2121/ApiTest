package com.api.dto;

import java.io.Serializable;

import com.api.model.Telefone;

public class TelefoneDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2464990220369114286L;
	
	private Long Id;
	
	private String numero;	
	
	private Long usuario_id;
	
	public TelefoneDTO(Telefone telefone) {
		this.Id=telefone.getId();
		this.numero=telefone.getNumero();
		this.usuario_id=telefone.getUsuario().getId();
	}
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Long getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}	
}
