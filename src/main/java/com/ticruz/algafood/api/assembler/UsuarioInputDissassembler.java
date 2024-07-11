package com.ticruz.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticruz.algafood.api.model.input.UsuarioInput;
import com.ticruz.algafood.domain.model.Usuario;

@Component
public class UsuarioInputDissassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Usuario toDomainObject(UsuarioInput usuarioInput) {
		
		return modelMapper.map(usuarioInput, Usuario.class);
	}
	
	public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
		
		 modelMapper.map(usuarioInput, usuario);
		
	}

}
