package com.ticruz.algafood.domain.exception;

public class PermissaoNaoEncontradaException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissaoNaoEncontradaException(String mensagem) {
		super(mensagem);
		
	}
	
	public PermissaoNaoEncontradaException(Long permissaoId) {
		super("Não existe um cadastro de permissão com código %d".formatted(permissaoId));
	}

}
