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

import com.ticruz.algafood.api.assembler.FormaPagamentoInputDisassembler;
import com.ticruz.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.ticruz.algafood.api.model.FormaPagamentoModel;
import com.ticruz.algafood.api.model.input.FormaPagamentoInput;
import com.ticruz.algafood.domain.model.FormaPagamento;
import com.ticruz.algafood.domain.repository.FormaPagamentoRepository;
import com.ticruz.algafood.domain.service.CadastroFormaPagamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Autowired
	private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoAssembler;
	
	
	@GetMapping
	public List<FormaPagamentoModel> listar(){
		List<FormaPagamento> formaPagamento = formaPagamentoRepository.findAll();
		
		return formaPagamentoAssembler.toCollectionObject(formaPagamento);
		
	}
	
	@GetMapping("/{formaPagamentoId}")
	public FormaPagamentoModel buscar(@PathVariable Long formaPagamentoId) {
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
	return formaPagamentoAssembler.toModel(formaPagamento);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
		cadastroFormaPagamento.salvar(formaPagamento);
		return formaPagamentoAssembler.toModel(formaPagamento);
		
	}
	
	@PutMapping("/{formaPagamentoId}")
	public FormaPagamentoModel FormaPagamentoModel (@PathVariable Long formaPagamentoId, 
			@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
		formaPagamentoAtual = cadastroFormaPagamento.salvar(formaPagamentoAtual);
		return formaPagamentoAssembler.toModel(formaPagamentoAtual);
		
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long formaPagamentoId) {
		cadastroFormaPagamento.excluir(formaPagamentoId);
	}
	
	
	

}
