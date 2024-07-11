package com.ticruz.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.UsuarioModel;
import com.ticruz.algafood.domain.model.Usuario;

@Component
public class UsuarioModelAssembler {

	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public UsuarioModel toModel(Usuario usuario) {
		return modelMapper.map(usuario, UsuarioModel.class);
	}
	
	public List<UsuarioModel> toCollectionModel(Collection<Usuario> usuarios){
		
		return usuarios.stream().map((model) -> toModel(model)).collect(Collectors.toList());
	}
}
