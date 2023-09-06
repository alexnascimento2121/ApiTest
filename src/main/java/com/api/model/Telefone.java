package com.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;




@Entity
public class Telefone implements Serializable{

	/**classe telefone para relacao com usuario
	 * @author alexn
	 * @since 2023-01-26
	 */
	private static final long serialVersionUID = -7447679503602012230L;
	
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	
	
	@JsonIgnore
	@ManyToOne(optional = false) // obriga a cadastrar o usuario caso cadastre telefone
	@ForeignKey(name="usuario_id")	
	private Usuario usuario;
	
	@Size(max=15, message="Máximo de caracteres permitido é 15")
	@Column(name = "numero", length = 15)
	private String numero;

	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
}
