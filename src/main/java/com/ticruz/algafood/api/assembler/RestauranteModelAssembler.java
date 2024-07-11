package com.ticruz.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.CozinhaModel;
import com.ticruz.algafood.api.model.RestauranteModel;
import com.ticruz.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;

	public RestauranteModel toModelAntigo(Restaurante restaurante) {	//método para retornar a classe model de serialization
		CozinhaModel cozinhaModel = new CozinhaModel();
		cozinhaModel.setId(restaurante.getCozinha().getId());
		cozinhaModel.setNome(restaurante.getCozinha().getNome());
		
		RestauranteModel restauranteModel =  new RestauranteModel();
		restauranteModel.setId(restaurante.getId());
		restauranteModel.setNome(restaurante.getNome());
		restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
		restauranteModel.setCozinha(cozinhaModel);
		return restauranteModel;
	}
	
	public List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes){
		 return restaurantes.stream().map(restaurante
				-> toModel(restaurante)).collect(Collectors.toList());
		
		
	}
	
	
	public RestauranteModel toModel(Restaurante restaurante) { //to model usando o modelMapper
		
		return modelMapper.map(restaurante, RestauranteModel.class);
	}
}
