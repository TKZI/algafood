package com.ticruz.algafood.api.model.mixin;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ticruz.algafood.domain.model.Restaurante;

public abstract class CozinhaMixin extends SimpleModule {


	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private List<Restaurante> restaurantes = new ArrayList<>();

}
