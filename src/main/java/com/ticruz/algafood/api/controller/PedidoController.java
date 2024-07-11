package com.ticruz.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.ticruz.algafood.api.assembler.PedidoInputDissassembler;
import com.ticruz.algafood.api.assembler.PedidoModelAssembler;
import com.ticruz.algafood.api.assembler.PedidoResumoModelAssembler;
import com.ticruz.algafood.api.model.PedidoModel;
import com.ticruz.algafood.api.model.PedidoResumoModel;
import com.ticruz.algafood.api.model.input.PedidoInput;
import com.ticruz.algafood.core.data.PageableTranslator;
import com.ticruz.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.ticruz.algafood.domain.exception.NegocioException;
import com.ticruz.algafood.domain.filter.PedidoFilter;
import com.ticruz.algafood.domain.model.Pedido;
import com.ticruz.algafood.domain.model.Usuario;
import com.ticruz.algafood.domain.repository.PedidoRepository;
import com.ticruz.algafood.domain.service.EmissaoPedidoService;
import com.ticruz.algafood.infrastructure.repository.spec.PedidoSpecs;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;
	
	@Autowired
	private PedidoInputDissassembler pedidoInputDissassembler;
	
	

	
	
	
	
	@GetMapping
	public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, @PageableDefault(size = 2) Pageable pageable ){
		pageable = traduzirPageable(pageable);
		Page<Pedido> pedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);
		List<PedidoResumoModel> pedidosResumoModel =  pedidoResumoModelAssembler.toCollectionModel(pedidos.getContent());
		Page<PedidoResumoModel> pagePedidoResumoModel = new PageImpl<>(pedidosResumoModel, pageable, pedidos.getTotalElements());
		
		return pagePedidoResumoModel;
		
	}
	
	
	
	
	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		return pedidoModelAssembler.toModel(emissaoPedido.buscarOuFalhar(codigoPedido));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel adicionar (@Valid @RequestBody PedidoInput pedidoInput) {
		
		try {
			Pedido novoPedido = pedidoInputDissassembler.toDomainObject(pedidoInput);
			novoPedido.setCliente(new Usuario());
			novoPedido.getCliente().setId(1L);
			
			novoPedido = emissaoPedido.emitir(novoPedido);
			
			return pedidoModelAssembler.toModel(novoPedido);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	
	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = ImmutableMap.of(
				"codigo", "codigo",
				"restaurante.nome", "restaurante.nome",
				"nomeCliente", "cliente.nome",
				"valorTotal", "valorTotal"

				);
		return PageableTranslator.translate(apiPageable, mapeamento);
		
	}
	
	
	
	
	
	
	
	
	
	
	/* @GetMapping
	public MappingJacksonValue listar(@RequestParam (required = false) String campos){
		List<Pedido> pedidos = pedidoRepository.findAll();
		List<PedidoResumoModel> pedidosResumo = pedidoResumoModelAssembler.toCollectionModel(pedidos);
		
		MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosResumo);
		
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		
		filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
		
		if(StringUtils.isNotBlank(campos)) {
	filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
			
		}
		
		pedidosWrapper.setFilters(filterProvider);
		
		return pedidosWrapper;
			método usando mappingjacsonvalue para filtrar 
	
	} */ 
}
