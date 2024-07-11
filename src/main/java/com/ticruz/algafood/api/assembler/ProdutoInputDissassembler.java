package com.ticruz.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.input.ProdutoInput;
import com.ticruz.algafood.domain.model.Produto;

@Component
public class ProdutoInputDissassembler {

	
	@Autowired
	private ModelMapper modelMapper;
	
	public Produto toDomainObject(ProdutoInput produtoInput) {
		
		return modelMapper.map(produtoInput, Produto.class);
	}
	
	public void copyToDomainObject(ProdutoInput produtoInput, Produto produto) {
	modelMapper.map(produtoInput, produto);
	}
}
