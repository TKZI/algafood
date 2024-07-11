package com.ticruz.algafood.api.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteIdInput {
	
	@NotNull
	private Long id;

}
