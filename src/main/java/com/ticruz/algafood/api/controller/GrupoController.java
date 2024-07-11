package com.ticruz.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticruz.algafood.api.assembler.GrupoInputDissassembler;
import com.ticruz.algafood.api.assembler.GrupoModelAssembler;
import com.ticruz.algafood.api.model.GrupoModel;
import com.ticruz.algafood.api.model.input.GrupoInput;
import com.ticruz.algafood.domain.model.Grupo;
import com.ticruz.algafood.domain.repository.GrupoRepository;
import com.ticruz.algafood.domain.service.CadastroGrupoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private GrupoModelAssembler grupoAssembler;
	
	@Autowired
	private GrupoInputDissassembler grupoDissassembler;
	
	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@GetMapping
	public List<GrupoModel> listar(){
		List<Grupo> grupos = grupoRepository.findAll();
		
		return grupoAssembler.toCollectionModel(grupos);
		
	}
	
	@GetMapping("/{grupoId}")
	public GrupoModel buscar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		
		return grupoAssembler.toModel(grupo);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupo = grupoDissassembler.toDomainObject(grupoInput);
		grupo = cadastroGrupo.salvar(grupo);
		return grupoAssembler.toModel(grupo);
		
	}
	
	@PutMapping("/{grupoId}")
	public GrupoModel atualizar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(grupoId);
		grupoDissassembler.copyToDomainObject(grupoInput, grupoAtual);
		grupoAtual = cadastroGrupo.salvar(grupoAtual);
		
		return grupoAssembler.toModel(grupoAtual);
	}
	
	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long grupoId) {
		cadastroGrupo.excluir(grupoId);
		
	}

}
