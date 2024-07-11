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

import com.ticruz.algafood.api.assembler.EstadoInputDisassembler;
import com.ticruz.algafood.api.assembler.EstadoModelAssembler;
import com.ticruz.algafood.api.model.EstadoModel;
import com.ticruz.algafood.api.model.input.EstadoInput;
import com.ticruz.algafood.domain.model.Estado;
import com.ticruz.algafood.domain.repository.EstadoRepository;
import com.ticruz.algafood.domain.service.CadastroEstadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/estados")    // está é a requisição para chegar ao metodo
public class EstadoController {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;
	
	@Autowired
	private EstadoModelAssembler estadoModelAssembler;
	
	@Autowired
	private EstadoInputDisassembler estadoInputDissasembler;
	
	@GetMapping
	public List<EstadoModel> listar(){
		List<Estado> todosEstados = estadoRepository.findAll();
		return estadoModelAssembler.toCollectionModel(todosEstados);
	}
	
	
	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId){
		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);
		return estadoModelAssembler.toModel(estado);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
	Estado estado = estadoInputDissasembler.toDomainObject(estadoInput);
	estado = cadastroEstado.salvar(estado);
			return estadoModelAssembler.toModel(estado);
			
	}

	@PutMapping("/{estadoId}")
	public EstadoModel atualizar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput ){
		Estado estadoAtual = cadastroEstado.buscarOuFalhar(estadoId);
		
		estadoInputDissasembler.copyToDomainObject(estadoInput, estadoAtual);
			//BeanUtils.copyProperties(estado, estadoAtual, "id");
		estadoAtual = cadastroEstado.salvar(estadoAtual);
		
			return estadoModelAssembler.toModel(estadoAtual);
			
		
	}
	
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId){

		cadastroEstado.remover(estadoId);
	}

}
