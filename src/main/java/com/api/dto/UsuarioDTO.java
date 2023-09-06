package com.api.dto;

import java.io.Serializable;
import java.util.List;

import com.api.model.Profissao;
import com.api.model.Telefone;
import com.api.model.Usuario;

public class UsuarioDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6894981528427554328L;
	
	private String login;
	
	private String nome;
	
	private Long id;
	
	private String senha;
	
	private String token;
	
	private String cep;
	
	private String bairro;
	
	private String logradouro;
	
	private String localidade;
	
	private String complemento;
	
	private String uf;
	
	private List<Telefone> telefones;
	
	private Profissao profissao;
	
	public UsuarioDTO(Usuario usuario) {
		this.setLogin(usuario.getLogin());
		this.nome = usuario.getNome();
		this.senha=usuario.getSenha();
		this.id = usuario.getId();
		this.token = usuario.getToken();
		this.cep = usuario.getCep();
		this.bairro = usuario.getBairro();
		this.logradouro = usuario.getLogradouro();
		this.localidade = usuario.getLocalidade();
		this.complemento = usuario.getComplemento();
		this.uf = usuario.getUf();
		this.telefones=usuario.getTelefones();
		this.profissao=usuario.getProfissao();
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getLocalidade() {
		return localidade;
	}
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public List<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	public Profissao getProfissao() {
		return profissao;
	}
	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}
}
