package com.ticruz.algafood.domain.exception;

//@ResponseStatus(code = HttpStatus.CONFLICT) nao precisa mais por usar o exceptionHandler
public class EntidadeEmUsoException extends NegocioException{

	private static final long serialVersionUID = 1L;
	
	
	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}
	
	
}
