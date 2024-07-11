package com.ticruz.algafood.api.model.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FormaPagamentoInput {
	
	@NotBlank
	private String descricao;

}
