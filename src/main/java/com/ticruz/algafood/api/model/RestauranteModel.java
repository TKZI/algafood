package com.ticruz.algafood.api.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticruz.algafood.api.model.resumo.RestauranteView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteModel { //classe de modelo de representação

	@JsonView({RestauranteView.Resumo.class,  RestauranteView.ResumoApenasNome.class})
	private Long id;
	
	@JsonView({RestauranteView.Resumo.class,  RestauranteView.ResumoApenasNome.class})
	private String nome;
	
	@JsonView(RestauranteView.Resumo.class)
	private BigDecimal taxaFrete;
	
	@JsonView(RestauranteView.Resumo.class)
	private CozinhaModel cozinha;
	
	
	private Boolean ativo;
	private Boolean aberto;
	private EnderecoModel endereco;
	
}
