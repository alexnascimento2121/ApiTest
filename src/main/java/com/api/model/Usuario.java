package com.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;





@Entity
public class Usuario implements UserDetails{

	/**classe usuario para acesso a api
	 * @author alexn
	 * @since 2023-01-25
	 * 
	 */
	private static final long serialVersionUID = -6454764427739454600L;
	
	@Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;
	
	@Size(max=30, message="Máximo de caracteres permitido é 30")
	@Column(name = "login", length = 30, nullable = false, unique = true)
	private String login;
	
	
	@Size(max=250, message="Máximo de caracteres permitido é 250")
	@Column(name = "senha", length = 250,nullable = false)
	private String senha;
	
	@OneToMany(mappedBy = "usuario",orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY) //orphanremoval apaga os telefones, caso o usuario seja apagado e faz em modo cascata.
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name="usuarios_role",uniqueConstraints=@UniqueConstraint(
	columnNames= {"usuario_id","role_id"},name="unique_role_user"),
	joinColumns=@JoinColumn(name ="usuario_id",referencedColumnName ="id",table="usuario",
	foreignKey=@ForeignKey(name ="usuario_fk",value = ConstraintMode.CONSTRAINT)),inverseJoinColumns = @JoinColumn(name="role_id",
			referencedColumnName ="id",table="role",unique=false,foreignKey = @ForeignKey(name="role_fk",value = ConstraintMode.CONSTRAINT)))
	private List<Role> roles = new ArrayList<Role>();
	
	private String Token;
	
	private String cep;
	
	private String bairro;
	
	private String logradouro;
	
	private String localidade;
	
	private String complemento;
	
	private String uf;
	
	@Size(max=250, message="Máximo de caracteres permitido é 250")
	@Column(name = "nome", length = 250, nullable = true)
	private String nome;
	
	@JsonFormat(pattern = "dd/MM/yyyy")// carrega a data para front neste padrao
	@Temporal(TemporalType.DATE)// cria neste formato no banco
	@DateTimeFormat(iso=ISO.DATE,pattern = "dd/MM/yyyy")// para backend entender o tipo de dado que recebe do frontend
	private Date dataNascimento;
	
	@ManyToOne
	private Profissao profissao;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public List<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
	//sao os acessos do usuario
	@Override
	public Collection<Role> getAuthorities() {
		
		return roles;
	}
	@JsonIgnore
	@Override
	public String getPassword() {
		
		return this.senha;
	}
	@JsonIgnore
	@Override
	public String getUsername() {		
		return this.login;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {		
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public Profissao getProfissao() {
		return profissao;
	}
	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}
}
