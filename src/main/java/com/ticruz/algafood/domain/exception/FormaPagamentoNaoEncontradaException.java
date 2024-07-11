package com.ticruz.algafood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FormaPagamentoNaoEncontradaException(String mensagem) {
		
		super(mensagem);
		
	}
	
	public FormaPagamentoNaoEncontradaException(Long formaPagamentoId) {
		
		this(String.format("Não existe uma cadastro de forma de pagamento com código %d", formaPagamentoId));
	}
	
	

}
