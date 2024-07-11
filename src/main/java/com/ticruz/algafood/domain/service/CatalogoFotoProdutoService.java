package com.ticruz.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticruz.algafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.ticruz.algafood.domain.model.FotoProduto;
import com.ticruz.algafood.domain.repository.ProdutoRepository;
import com.ticruz.algafood.domain.service.FotoStorageService.NovaFoto;

import jakarta.transaction.Transactional;

@Service
public class CatalogoFotoProdutoService {

	@Autowired
	private FotoStorageService fotoStorageService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Transactional
	public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {
		Long restauranteId = foto.getRestauranteId();
		Long produtoId = foto.getProduto().getId();
		String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
		String nomeArquivoExistente = null;

		Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);
		if (fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			produtoRepository.delete(fotoExistente.get());
		}
		foto.setNomeArquivo(nomeNovoArquivo);
		// feito o salvar para evitar rollback do banco e armazenar no local
		foto = produtoRepository.salvar(foto);
		// para descarregar antes e garantir não armazenar caso aconteça rollback
		produtoRepository.flush();
		NovaFoto novaFoto = NovaFoto.builder().nomeArquivo(foto.getNomeArquivo()).inputStream(dadosArquivo).build();

		fotoStorageService.substituir(nomeArquivoExistente, novaFoto);

		return foto;
	}

	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return produtoRepository.findFotoById(restauranteId, produtoId)
				.orElseThrow(() -> new FotoProdutoNaoEncontradaException(produtoId,restauranteId));
	}
	
	@Transactional
	public void removerFotoProduto(Long restauranteId, Long produtoId) {
		FotoProduto foto = buscarOuFalhar(restauranteId, produtoId);
		produtoRepository.delete(foto);
		produtoRepository.flush();
		fotoStorageService.remover(foto.getNomeArquivo());
	}

}
