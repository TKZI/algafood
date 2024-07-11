package com.ticruz.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticruz.algafood.api.assembler.UsuarioInputDissassembler;
import com.ticruz.algafood.api.assembler.UsuarioModelAssembler;
import com.ticruz.algafood.api.model.UsuarioModel;
import com.ticruz.algafood.api.model.input.SenhaInput;
import com.ticruz.algafood.api.model.input.UsuarioComSenhaInput;
import com.ticruz.algafood.api.model.input.UsuarioInput;
import com.ticruz.algafood.domain.model.Usuario;
import com.ticruz.algafood.domain.repository.UsuarioRepository;
import com.ticruz.algafood.domain.service.CadastroUsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value ="/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CadastroUsuarioService cadastroUsuario;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private UsuarioInputDissassembler usuarioInputDissassembler;
	
	@GetMapping
	public List<UsuarioModel> listar(){
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		return usuarioModelAssembler.toCollectionModel(usuarios);
		
	}
	
	@GetMapping("{usuarioId}")
	public UsuarioModel buscar(@PathVariable Long usuarioId) {
		Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
		
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioModel adicionar(@RequestBody UsuarioComSenhaInput usuarioInput) {
		Usuario usuario = usuarioInputDissassembler.toDomainObject(usuarioInput);
		usuario = cadastroUsuario.salvar(usuario);
		
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PutMapping("/{usuarioId}")
	public UsuarioModel atualizar(@PathVariable Long usuarioId , @RequestBody @Valid UsuarioInput usuarioInput) {
		Usuario usuarioAtual = cadastroUsuario.buscarOuFalhar(usuarioId);
		usuarioInputDissassembler.copyToDomainObject(usuarioInput, usuarioAtual);
		usuarioAtual = cadastroUsuario.salvar(usuarioAtual);
		
		return usuarioModelAssembler.toModel(usuarioAtual);
		
		
	}
	
	@PutMapping("/{usuarioId}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senha) {
		cadastroUsuario.alterarSenha(usuarioId, senha.getSenhaAtual(), senha.getNovaSenha());
		
	}
	
	
	
	
}

