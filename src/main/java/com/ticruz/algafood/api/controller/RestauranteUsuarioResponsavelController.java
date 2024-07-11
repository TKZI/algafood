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

import com.ticruz.algafood.api.assembler.UsuarioModelAssembler;
import com.ticruz.algafood.api.model.UsuarioModel;
import com.ticruz.algafood.domain.model.Restaurante;
import com.ticruz.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	
	@GetMapping
	public List<UsuarioModel> listar(@PathVariable Long restauranteId){
		Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);
		
		return usuarioModelAssembler.toCollectionModel(restauranteAtual.getResponsaveis());
	}
	
	
	@PutMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.associarUsuario(restauranteId, usuarioId);
		
	}
	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void dessassociarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.dessassociarUsuario(restauranteId, usuarioId);
	}

}
