package com.ticruz.algafood.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.ticruz.algafood.domain.model.FotoProduto;
import com.ticruz.algafood.domain.repository.ProdutoRepositoryQueries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	@Override
	public FotoProduto salvar(FotoProduto foto) {

		return manager.merge(foto);
	}

	@Transactional
	@Override
	public void delete(FotoProduto foto) {
		manager.remove(foto);
		
	}

}
