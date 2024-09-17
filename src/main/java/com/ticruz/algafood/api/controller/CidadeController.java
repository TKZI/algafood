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

import com.ticruz.algafood.api.ResourceUriHelper;
import com.ticruz.algafood.api.assembler.CidadeInputDisassembler;
import com.ticruz.algafood.api.assembler.CidadeModelAssembler;
import com.ticruz.algafood.api.model.CidadeModel;
import com.ticruz.algafood.api.model.input.CidadeInput;
import com.ticruz.algafood.domain.exception.EstadoNaoEncontradoException;
import com.ticruz.algafood.domain.exception.NegocioException;
import com.ticruz.algafood.domain.model.Cidade;
import com.ticruz.algafood.domain.repository.CidadeRepository;
import com.ticruz.algafood.domain.service.CadastroCidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cidade", description = "Gerencia as cidades")
@RestController
@RequestMapping("/cidades")
public class CidadeController {
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;

	@Operation(description = "Lista as cidades")
	@GetMapping
	public List<CidadeModel> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}

	@Operation(description = "Busca uma cidade por ID")
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@Parameter(description = "ID de uma cidade", example = "1") @PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
		return cidadeModelAssembler.toModel(cidade);
	}

	@Operation(description = "Cadastra uma  cidade")
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public CidadeModel adicionar(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Representação de uma nova cidade") @RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

			cidade = cadastroCidade.salvar(cidade);

			CidadeModel cidadeModel =  cidadeModelAssembler.toModel(cidade);
			
			ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());
			return cidadeModel;
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@Operation(description = "Atualiza uma cidade por ID")
	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInput cidadeInput) {

		// BeanUtils.copyProperties(cidade, cidadeAtual, "id");

		try {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			cidadeAtual = cadastroCidade.salvar(cidadeAtual);

			return cidadeModelAssembler.toModel(cidadeAtual);

		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}

	@Operation(description = "Exclui uma cidade por ID")
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable() Long cidadeId) {
		cadastroCidade.excluir(cidadeId);

	}

}
