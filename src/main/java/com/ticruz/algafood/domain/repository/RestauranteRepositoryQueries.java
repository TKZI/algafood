package com.ticruz.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.ticruz.algafood.domain.model.Restaurante;

public interface RestauranteRepositoryQueries {

	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
	List<Restaurante> findCriteria(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
	
	List<Restaurante> findComFreteGratis(String nome);

}	

//interface extraida para impl do metodo find, fazendo herança multipla 