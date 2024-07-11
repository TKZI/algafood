package com.ticruz.algafood.infrastructure.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.ticruz.algafood.domain.model.Restaurante;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor 			//usando o lombok para gerar os construtores automaticamente
public class RestauranteComNomeSemelhanteSpec implements Specification<Restaurante> {


	private static final long serialVersionUID = 1L;
	
	private String nome;

	@Override
	public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	
		return criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
	}
}
