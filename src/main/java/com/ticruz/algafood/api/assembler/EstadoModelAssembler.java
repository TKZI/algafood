package com.ticruz.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.EstadoModel;
import com.ticruz.algafood.domain.model.Estado;

@Component
public class EstadoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public EstadoModel toModel(Estado estado) {
		
		return modelMapper.map(estado, EstadoModel.class);
		
		
	}
	
	public List<EstadoModel> toCollectionModel(List<Estado> estado){
		
		return estado.stream().map(estadoModel -> toModel(estadoModel)).collect(Collectors.toList());
		
	}

}
