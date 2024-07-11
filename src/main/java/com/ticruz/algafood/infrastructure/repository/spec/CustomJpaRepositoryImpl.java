package com.ticruz.algafood.infrastructure.repository.spec;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.ticruz.algafood.domain.repository.CustomJpaRepository;

import jakarta.persistence.EntityManager;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

	private EntityManager manager;
	
	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.manager = entityManager;
	}

	
		//precisa usar o generics pq é uma implementação que todos conseguem usar 
		// T = classe modelo
	@Override
	public Optional<T> buscarPrimeiro() {
		var jpql = "from " + getDomainClass().getName();
		
	T entity =	manager.createQuery(jpql, getDomainClass()).setMaxResults(1).getSingleResult();
		
	return Optional.ofNullable(entity);
	}


	@Override
	public void detach(T entity) {
		manager.detach(entity);
		
	}
	
}
