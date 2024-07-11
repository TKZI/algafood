package com.ticruz.algafood.core.jackson;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ticruz.algafood.api.model.mixin.CidadeMixin;
import com.ticruz.algafood.api.model.mixin.CozinhaMixin;
import com.ticruz.algafood.domain.model.Cidade;
import com.ticruz.algafood.domain.model.Cozinha;

@Component
public class JacksonMixinModule extends SimpleModule{


	private static final long serialVersionUID = 1L;
	
	public JacksonMixinModule() {
		setMixInAnnotation(Cidade.class, CidadeMixin.class);
		setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
	}

}
