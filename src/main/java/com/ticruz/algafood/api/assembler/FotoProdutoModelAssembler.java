package com.ticruz.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.FotoProdutoModel;
import com.ticruz.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;

	
	public FotoProdutoModel toModel(FotoProduto foto) {
		return modelMapper.map(foto, FotoProdutoModel.class);
	}
}
