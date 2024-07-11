package com.ticruz.algafood.infrastructure.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.ticruz.algafood.domain.model.Restaurante;

public class RestauranteSpecs {
	
	public static Specification<Restaurante> comFreteGratis(){
		
		//return new RestauranteComFreteGratisSpec();
		return (root, query, builder ) -> builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
		//criando a lambida pra fazer a função visto que o metodo toPredicate recebe esses 3
	}
	
	public static Specification<Restaurante> comNomeSemelhante(String nome){
		
		//return new RestauranteComNomeSemelhanteSpec(nome);
		
		return (root, query, builder) -> builder.like(root.get("nome"), "%" + nome + "%");
	}

}
