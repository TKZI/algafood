package com.ticruz.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.input.RestauranteInput;
import com.ticruz.algafood.domain.model.Cidade;
import com.ticruz.algafood.domain.model.Cozinha;
import com.ticruz.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Restaurante toDomainObjectAntigo(RestauranteInput restauranteInput) {
		Restaurante restaurante = new Restaurante();
		
		
		restaurante.setNome(restauranteInput.getNome());
		restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(restauranteInput.getCozinha().getId());
		
		restaurante.setCozinha(cozinha);
		
		return restaurante;
	}
	
	public Restaurante toDomainObject(RestauranteInput restauranteInput) {
		
		return modelMapper.map(restauranteInput, Restaurante.class);
	}
	
	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
		restaurante.setCozinha(new Cozinha());
		/* para evitar 
		org.hibernate.HibernateException: identifier of an instance of 
		com.ticruz.algafood.domain.model.Cozinha was altered from 2 to 1 */ 
		
		if(restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}
		
		modelMapper.map(restauranteInput, restaurante);
		
	}

}
