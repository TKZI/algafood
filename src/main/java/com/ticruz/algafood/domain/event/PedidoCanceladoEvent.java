package com.ticruz.algafood.domain.event;

import com.ticruz.algafood.domain.model.Pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoCanceladoEvent {
	
	private Pedido pedido;

}
