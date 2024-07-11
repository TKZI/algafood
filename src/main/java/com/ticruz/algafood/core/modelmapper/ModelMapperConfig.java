package com.ticruz.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ticruz.algafood.api.model.EnderecoModel;
import com.ticruz.algafood.api.model.input.ItemPedidoInput;
import com.ticruz.algafood.domain.model.Endereco;
import com.ticruz.algafood.domain.model.ItemPedido;

@Configuration // cria o bean para as classes de injeção
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();

		modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
				.addMappings(mapper -> mapper.skip(ItemPedido::setId));

		var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);

		enderecoToEnderecoModelTypeMap.<String>addMapping(src -> src.getCidade().getEstado(),
				(rest, value) -> rest.getCidade().setNomeEstado(value));

		return modelMapper;
	}

}
