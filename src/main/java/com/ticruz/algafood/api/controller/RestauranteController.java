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

import com.fasterxml.jackson.annotation.JsonView;
import com.ticruz.algafood.api.assembler.RestauranteInputDisassembler;
import com.ticruz.algafood.api.assembler.RestauranteModelAssembler;
import com.ticruz.algafood.api.model.RestauranteModel;
import com.ticruz.algafood.api.model.input.RestauranteInput;
import com.ticruz.algafood.api.model.resumo.RestauranteView;
import com.ticruz.algafood.domain.exception.CidadeNaoEncontradaException;
import com.ticruz.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.ticruz.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.ticruz.algafood.domain.exception.NegocioException;
import com.ticruz.algafood.domain.model.Restaurante;
import com.ticruz.algafood.domain.repository.RestauranteRepository;
import com.ticruz.algafood.domain.service.CadastroRestauranteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	
	
	
	
	 @GetMapping(produces = "application/json" )
	public List<RestauranteModel> listar(){
		 
		 return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	} 
	
	
	@GetMapping(params = "projecao=resumo")
	@JsonView(RestauranteView.Resumo.class)
	public List<RestauranteModel> listarResumo(){
		return listar();
	}
	
	@GetMapping(params = "projecao=apenas-nome")
	@JsonView(RestauranteView.ResumoApenasNome.class)
	public List<RestauranteModel> listarApenasNome(){
		return listar();
	} 
	
	
	@GetMapping("/{restauranteId}")
	public RestauranteModel buscarPorId(@PathVariable() Long restauranteId){
		Restaurante restaurante =  cadastroRestaurante.buscarOuFalhar(restauranteId);

		return restauranteModelAssembler.toModel(restaurante);
		
		
	}

	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(	//validated valida o grupo especifico
			@RequestBody @Valid RestauranteInput restauranteInput){
		try {	Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
			return  restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
			
		}catch(EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteInput restauranteInput){
	//	Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
		
		
	//	BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasDePagamento", "endereco", "dataCadastro", "produtos");
		//nao usa mais devido a modelMapper

				try {
					return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
				} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
					throw new NegocioException(e.getMessage());
				}
				
		
	}
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		cadastroRestaurante.ativar(restauranteIds);
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		cadastroRestaurante.inativar(restauranteIds);
	}
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir (@PathVariable Long restauranteId) {
		cadastroRestaurante.abrir(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		cadastroRestaurante.fechar(restauranteId);
	}
	
	

	
	
	//o método alterar verifica se o restaurante é nulo e entra no if, verifica a cozinha no service
	// e caso for nula ele trata a execessão que retorna
			
		
	
	
	
	
	/* @GetMapping
	public MappingJacksonValue listar(@RequestParam (required = false) String projecao) {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		List<RestauranteModel> restauranteModel = restauranteModelAssembler.toCollectionModel(restaurantes);
		
		//como se estivesse envolopado
		MappingJacksonValue restauranteWrapper = new MappingJacksonValue(restauranteModel);
		restauranteWrapper.setSerializationView(RestauranteView.Resumo.class); //padrao
		
		if("apenas-nome".equals(projecao)) {
			restauranteWrapper.setSerializationView(RestauranteView.ResumoApenasNome.class);
		}else if ("completo".equals(projecao)) {
			restauranteWrapper.setSerializationView(null);
		}
		return restauranteWrapper;
		
	} */ //classe para listar usando o mappingJackson
	
	
	

}
