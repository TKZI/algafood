package com.ticruz.algafood.domain.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND) //, reason = "entidade nao encontrada")
public abstract class  EntidadeNaoEncontradaException extends NegocioException {

	private static final long serialVersionUID = 1L;


	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	
}
