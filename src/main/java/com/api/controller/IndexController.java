package com.api.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.UsuarioDTO;
import com.api.dto.UsuarioReportDTO;
import com.api.model.Usuario;
import com.api.model.UsuarioReport;
import com.api.repository.TelefoneRepository;
import com.api.repository.UsuarioRepository;
import com.api.service.ImplementacaoUserDetailsService;
import com.api.service.ServiceRelatorio;
import com.google.gson.Gson;

// @CrossOrigin permite que qualquer requisicao via ajax acesse os recursos do controller
// @CrossOrigin(origins = "https://www.algumcliente.com") permite apenas para o cliente especifico
@RestController
@RequestMapping(value="/usuario")
public class IndexController {
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private TelefoneRepository telefoneRepo;
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoService;
	
	@Autowired
	private ServiceRelatorio relatorioService;
	
	/*servico restful*/
	@GetMapping(value="/{id}",produces = "application/json")
	@CachePut("cacheuser")
	public ResponseEntity<UsuarioDTO> usuarioById(@PathVariable(value="id")Long id) {
		
		Optional<Usuario> usuario = usuarioRepo.findById(id);		
		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario.get()),HttpStatus.OK);
	}
	// @CrossOrigin(origins = "https://www.google.com") permite liberar este endpoint para o google
	@GetMapping(value="/",produces = "application/json")
	@CachePut("cacheUsuarios")
	public ResponseEntity<Page<Usuario>> allUsuarios(){
		// page e pagerequest permite trazer pagina e trazer os elementos de uma lista no back
		PageRequest page = PageRequest.of(0, 5,Sort.by("nome"));
		
		Page<Usuario> list=usuarioRepo.findAll(page);
		
		//List<Usuario> list =(List<Usuario>) usuarioRepo.findAll(); retorna todos usuarios independente do tamanho da lista -> deve ser usada com crud repository
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	
	@GetMapping(value="/page/{pagina}",produces = "application/json")
	@CachePut("cacheUsuarios")
	public ResponseEntity<Page<Usuario>> allUsuariosPagina(@PathVariable("pagina")int pagina)throws InterruptedException{
		// page e pagerequest permite trazer pagina e trazer os elementos de uma lista no back
		PageRequest page = PageRequest.of(pagina, 5,Sort.by("nome"));
		
		Page<Usuario> list=usuarioRepo.findAll(page);
		
		//List<Usuario> list =(List<Usuario>) usuarioRepo.findAll(); retorna todos usuarios independente do tamanho da lista -> deve ser usada com crud repository
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	/*simula uma applicaçaõ pesada com varios dados requisitados*/
		@GetMapping(value="/v2/",produces = "application/json")
		 /*@Cacheable("cacheAllUsuarios")habilita o uso do cache para este endpoint*/
	@CacheEvict(value = "cacheAllUsuarios",allEntries = true)// remove cache não utilizado 
	@CachePut("cacheAllUsuarios")// tras novos dados e coloca em cache
	public ResponseEntity<List<Usuario>> allUsuariosLento() throws InterruptedException{
			List<Usuario> list =(List<Usuario>) usuarioRepo.findAll();
			
			// Thread.sleep(6000);/*simula uma applicaçaõ pesada com varios dados requisitados por 6 segundos*/
			return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
		
	
	@PostMapping(value="/",produces = "application/json")
	public ResponseEntity<Usuario> novoUsuario(@RequestBody Usuario usuario) throws Exception {
		
		for(int pos=0; pos<usuario.getTelefones().size();pos ++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		/*Consumindo uma api publica externa*/
		URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		
		String cep ="";
		StringBuilder jsonCep= new StringBuilder();
		
		while((cep =br.readLine()) != null) {
			jsonCep.append(cep);
		}
		
		System.out.println(jsonCep.toString());
		
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		
		usuario.setCep(userAux.getCep());
		usuario.setBairro(userAux.getBairro());
		usuario.setLocalidade(userAux.getLocalidade());
		usuario.setLogradouro(userAux.getLogradouro());
		usuario.setUf(userAux.getUf());
		usuario.setComplemento(userAux.getComplemento());

		
		String senhacripto= new BCryptPasswordEncoder().encode(usuario.getLogin());
		
		usuario.setSenha(senhacripto);
		
		Usuario usuarioSalvo = usuarioRepo.save(usuario);	
		
		implementacaoService.insereAcessoPadrao(usuarioSalvo.getId());
		
		return new ResponseEntity<Usuario>(usuarioSalvo,HttpStatus.OK);
	}
	 //@CrossOrigin(origins = "localhost:4200") //permite liberar este endpoint para serviço que estiver na porta 8080
	@PutMapping(value="/",produces = "application/json")
	public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario usuario) {
		for(int pos=0; pos<usuario.getTelefones().size();pos ++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		Usuario userTemp=usuarioRepo.findById(usuario.getId()).get();
		if(!userTemp.getSenha().equals(usuario.getSenha())) {
			String senhacripto= new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacripto);
		}
		
		Usuario usuarioSalvo = usuarioRepo.save(usuario);		
		return new ResponseEntity<Usuario>(usuarioSalvo,HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{id}",produces = "application/json")
	public String deleteUsuario(@PathVariable(value = "id")Long id){
		usuarioRepo.deleteById(id);
		return "usuario deletado";		
	}
	

	@GetMapping(value="/usuarioPorNome/{nome}",produces = "application/json")
	@CachePut("cacheUsuarioporNome")
	public ResponseEntity<Page<Usuario>> usuarioPorNome(@PathVariable("nome")String nome)throws InterruptedException{
		//List<Usuario> list =(List<Usuario>) usuarioRepo.findByNome(nome); com crudrepository sem paginacao
		PageRequest pageRequest = null;
		Page<Usuario> list=null;
		
		if(nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")) {
			pageRequest = PageRequest.of(0, 5,Sort.by("nome"));
			list = usuarioRepo.findAll(pageRequest);
		}else {
			pageRequest = PageRequest.of(0, 5,Sort.by("nome"));
			list = usuarioRepo.findUserByNamePage(nome,pageRequest);
		}
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	
	@GetMapping(value="/usuarioPorNome/{nome}/page/{page}",produces = "application/json")
	@CachePut("cacheUsuarioporNome")
	public ResponseEntity<Page<Usuario>> usuarioPorNomePage(@PathVariable("nome")String nome,@PathVariable("page")int page)throws InterruptedException{
		//List<Usuario> list =(List<Usuario>) usuarioRepo.findByNome(nome); com crudrepository sem paginacao
		PageRequest pageRequest = null;
		Page<Usuario> list=null;
		
		if(nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")) {
			pageRequest = PageRequest.of(page, 5,Sort.by("nome"));
			list = usuarioRepo.findAll(pageRequest);
		}else {
			pageRequest = PageRequest.of(page, 5,Sort.by("nome"));
			list = usuarioRepo.findUserByNamePage(nome,pageRequest);
		}
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	// espero receber uma resposta em texto todas vez que excluir telefone
	@DeleteMapping(value="/deleteTelefone/{id}",produces = "application/text")
	public String deleteTelefone(@PathVariable("id") Long id) {
		telefoneRepo.deleteById(id);
		
		return "telefone deletado";
	}
	
	@GetMapping(value="/relatorio",produces = "application/text")	
	public ResponseEntity<String> downloadRelatorio(HttpServletRequest request)throws Exception{
		byte [] pdf=relatorioService.gerarRelatorio("relatorio-usuario",new HashMap<>(), request.getServletContext());
		
		/*serve para ser testada de qualquer browser*/
		String base64Pdf = "data:application/pdf;base64,"+Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}
	
	@PostMapping(value="/relatorio/",produces = "application/text")	
	public ResponseEntity<String> downloadRelatorioParam(HttpServletRequest request,@RequestBody UsuarioReport usuarioReport)throws Exception{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		SimpleDateFormat dateFormatParam= new SimpleDateFormat("yyyy-MM-dd");
		
		String dataInicio=dateFormatParam.format(dateFormat.parse(usuarioReport.getDataInicio()));
		
		String dataFim=dateFormatParam.format(dateFormat.parse(usuarioReport.getDataFim()));
		
		Map<String, Object> params= new HashMap<String,Object>();
		
		params.put("DATA_INICIO", dataInicio);
		params.put("DATA_FIM", dataFim);

		byte [] pdf=relatorioService.gerarRelatorio("relatorio-usuario-param",params, request.getServletContext());
		
		/*serve para ser testada de qualquer browser*/
		String base64Pdf = "data:application/pdf;base64,"+Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}
}
