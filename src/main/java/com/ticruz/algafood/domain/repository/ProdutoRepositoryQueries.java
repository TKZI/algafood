package com.ticruz.algafood.domain.repository;

import com.ticruz.algafood.domain.model.FotoProduto;


public interface ProdutoRepositoryQueries {

	FotoProduto salvar (FotoProduto foto);
	void delete(FotoProduto foto);
}
