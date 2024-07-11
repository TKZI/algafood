package com.ticruz.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticruz.algafood.api.assembler.PermissaoModelAssembler;
import com.ticruz.algafood.api.model.PermissaoModel;
import com.ticruz.algafood.domain.model.Grupo;
import com.ticruz.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(value = "grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;
	
	@Autowired
	private CadastroGrupoService grupoService;
	
	
	
	
	@GetMapping
	public List<PermissaoModel> listar(@PathVariable Long grupoId){
		Grupo grupo = grupoService.buscarOuFalhar(grupoId);
		
		return permissaoModelAssembler.toCollectionModel(grupo.getPermissoes());
	}
	
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void assossiarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		grupoService.associarPermissao(grupoId, permissaoId);
		
	}
	
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void dessassociarPermissao(@PathVariable Long grupoId,@PathVariable Long permissaoId) {
		grupoService.dessassociarPermissao(grupoId, permissaoId);
	}
}
