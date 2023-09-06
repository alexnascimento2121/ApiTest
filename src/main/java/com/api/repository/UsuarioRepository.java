package com.api.repository;



import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.api.model.Usuario;
// CrudRepository usado no inicio do projeto. Porem nao serve para fazer paginacao e nem trazer dados de forma lazy na tela
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query("select u from Usuario u where u.login=?1")
	Usuario findByLogin(String login);
	
//	@Query("select u from Usuario u where u.nome like %?1%")
//	Usuario findByNome(String nome);
	
	@Query("select u from Usuario u where u.nome like %?1%")
	List<Usuario> findByNome(String nome);
	
	@Transactional
	@Modifying /*estou fazendo uma atualizacao no bd*/
	@Query(nativeQuery = true,value="update Usuario u set u.token =?1 where login =?2")
	void atualizaTokenUser(String token,String login);
	
	
	@Query(value="select CONSTRAINT_NAME\r\n"
			+ "from information_schema.table_constraints\r\n"
			+ "where TABLE_NAME ='usuarios_role' and CONSTRAINT_TYPE <>'FOREIGN KEY' and CONSTRAINT_NAME <>'unique_role_user';", nativeQuery = true)
	String consultaConstraintRole();
	
	@Modifying
	@Query(nativeQuery = true,value="ALTER TABLE usuarios_role DROP INDEX ?1;")
	void removerConstraint(String constraint);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true,value="insert into usuarios_role (usuario_id,role_id) values(?1,(select id from role where nome_role='Usuario a'));")
	void insereAcessoRolePadrao(Long id);

	default Page<Usuario> findUserByNamePage(String nome, PageRequest pageRequest){
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		
		/*Configurando para pesquisar por nome e paginacao*/
	
			ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome", 
					ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
			Example<Usuario> example = Example.of(usuario,exampleMatcher);
			Page<Usuario> retorno = findAll(example,pageRequest);
			return retorno;
	}
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true,value="update  usuario set senha = ?1 where id = ?2")
	void updateSenha(String senha,Long idUser);
}
