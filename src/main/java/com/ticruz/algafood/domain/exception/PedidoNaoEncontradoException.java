package com.ticruz.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public PedidoNaoEncontradoException (String codigoPedido) {
		super("Não existe um pedido com código %s".formatted(codigoPedido));
	}

}
