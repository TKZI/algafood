package com.ticruz.algafood.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProdutoNaoEncontradoException(String mensagem) {
		super(mensagem);
		
	}
	
	public ProdutoNaoEncontradoException(Long produtoId, Long restauranteId) {
		this("Não existe um cadastro de produto com o código %d para o restaurante de código %d".formatted
                (
                        produtoId, restauranteId));
	}

}
