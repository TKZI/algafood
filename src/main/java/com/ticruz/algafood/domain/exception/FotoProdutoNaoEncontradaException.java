package com.ticruz.algafood.domain.exception;

public class FotoProdutoNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public FotoProdutoNaoEncontradaException(String mensagem) {
		super(mensagem);
		
	}
	
	public FotoProdutoNaoEncontradaException(Long restauranteId, Long produtoId) {
		this("Não existe um cadastro de foto do produto com código %d para o restaurante de código %d".formatted(produtoId, restauranteId));
	}

}
