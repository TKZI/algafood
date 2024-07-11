package com.ticruz.algafood.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ticruz.algafood.api.assembler.FotoProdutoModelAssembler;
import com.ticruz.algafood.api.model.FotoProdutoModel;
import com.ticruz.algafood.api.model.input.FotoProdutoInput;
import com.ticruz.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.ticruz.algafood.domain.model.FotoProduto;
import com.ticruz.algafood.domain.model.Produto;
import com.ticruz.algafood.domain.service.CadastroProdutoService;
import com.ticruz.algafood.domain.service.CatalogoFotoProdutoService;
import com.ticruz.algafood.domain.service.FotoStorageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProduto;

	@Autowired
	private FotoStorageService fotoStorageService;

	@Autowired
	private CadastroProdutoService cadastroProdutoService;

	@Autowired
	private FotoProdutoModelAssembler fotoProdutoModelAssembler;

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		Produto produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
		MultipartFile arquivo = fotoProdutoInput.getArquivo();

		FotoProduto fotoProduto = new FotoProduto();
		fotoProduto.setProduto(produto);
		fotoProduto.setDescricao(fotoProdutoInput.getDescricao());
		fotoProduto.setContentType(arquivo.getContentType());
		fotoProduto.setTamanho(arquivo.getSize());
		fotoProduto.setNomeArquivo(arquivo.getOriginalFilename());

		FotoProduto fotoSalva = catalogoFotoProduto.salvar(fotoProduto, arquivo.getInputStream());

		return fotoProdutoModelAssembler.toModel(fotoSalva);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {

		return fotoProdutoModelAssembler.toModel(catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId));
	}

	@GetMapping
	public ResponseEntity<InputStreamResource> servirFoto(@PathVariable Long restauranteId,
			@PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader)
			throws HttpMediaTypeNotAcceptableException {
		try {
			FotoProduto fotoProduto = catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId);

			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			List<MediaType> mediaTypeAceitas = MediaType.parseMediaTypes(acceptHeader);

			verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypeAceitas);

			InputStream inputStream = fotoStorageService.recuperarFoto(fotoProduto.getNomeArquivo());

			return ResponseEntity.ok().contentType(mediaTypeFoto).body(new InputStreamResource(inputStream));
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long produtoId, @PathVariable Long restauranteId) {
		catalogoFotoProduto.removerFotoProduto(restauranteId, produtoId);
	}

	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypeAceitas)
			throws HttpMediaTypeNotAcceptableException {
		boolean compativel = mediaTypeAceitas.stream()
				.anyMatch((mediaTypeAceita) -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

		if (!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypeAceitas);
		}

	}

}
