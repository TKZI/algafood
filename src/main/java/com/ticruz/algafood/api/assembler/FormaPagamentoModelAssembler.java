package com.ticruz.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.FormaPagamentoModel;
import com.ticruz.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public FormaPagamentoModel toModel (FormaPagamento formaDePagamento) {
		
		return modelMapper.map(formaDePagamento, FormaPagamentoModel.class);
		
	}
	
	public List<FormaPagamentoModel> toCollectionObject(Collection<FormaPagamento> formaPagamento	){
		
		return formaPagamento.stream().map(forma -> toModel(forma)).collect(Collectors.toList());
	}

}
