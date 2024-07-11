package com.ticruz.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

import com.ticruz.algafood.api.assembler.CozinhaInputDisassembler;
import com.ticruz.algafood.api.assembler.CozinhaModelAssembler;
import com.ticruz.algafood.api.model.CozinhaModel;
import com.ticruz.algafood.api.model.input.CozinhaInput;
import com.ticruz.algafood.domain.model.Cozinha;
import com.ticruz.algafood.domain.repository.CozinhaRepository;
import com.ticruz.algafood.domain.service.CadastroCozinhaService;

import jakarta.validation.Valid;

@RestController	//restController possui tanto o responsebody quanto o controller 
@RequestMapping("/cozinhas")
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;
	
	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;
	
	@GetMapping
	public Page<CozinhaModel> listar(Pageable pageable){
		System.out.println("Listar");
		
		//paginação retorna uma page, e e pegando o conteudo retorna uma lista
		Page<Cozinha> cozinhas = cozinhaRepository.findAll(pageable);
		
		List<CozinhaModel> cozinhasModel =  cozinhaModelAssembler.toCollectionModel(cozinhas.getContent());
		
		Page<CozinhaModel> cozinhasPage = new PageImpl<>(cozinhasModel, pageable,cozinhas.getTotalElements());
				
				return cozinhasPage;
	
	}
	

	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		return cozinhaModelAssembler.toModel(cozinha) ;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar( @RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		cozinha = cadastroCozinha.salvar(cozinha);
		
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@PutMapping("/{cozinhaId}")
	public CozinhaModel atualizar( @PathVariable Long cozinhaId,@RequestBody @Valid CozinhaInput cozinhaInput){
		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		//	BeanUtils.copyProperties(cozinha, cozinhaAtual, "id"); 

		cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual);
		
		return cozinhaModelAssembler.toModel(cozinhaAtual);
		
			
			
		}
		
	
	

	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{cozinhaId}")
	public void remover(@PathVariable Long cozinhaId){
		
			cadastroCozinha.excluir(cozinhaId);
		
		
		}
	
}


//produces reprepresenta quais negotiation o metodo vai retornar, xml ou json, podendo ter mais
//de um metodo ou colocando os dois para o mesmo 
// sempre precisa passar o caminho pro getmapping e pathVariable se colocar igual o parametro do http nao precisa especificar 
//responseEntity para poder passar a resposta que vc quer na requisição (http)
// beanUtils.copyProperties ajuda a copiar as propriedades de um local para outro podendo colocar algo para ignorar