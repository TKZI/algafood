package com.ticruz.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.input.PedidoInput;
import com.ticruz.algafood.domain.model.Pedido;

@Component
public class PedidoInputDissassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Pedido toDomainObject(PedidoInput pedidoInput) {
		
		return modelMapper.map(pedidoInput, Pedido.class);
	}
	
	public  void copyToDomainObject(PedidoInput pedidoInput, Pedido pedido) {
		
		modelMapper.map(pedidoInput, pedido);
	}

}
