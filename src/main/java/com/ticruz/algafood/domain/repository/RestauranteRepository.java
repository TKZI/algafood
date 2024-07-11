package com.ticruz.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticruz.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries,
	JpaSpecificationExecutor<Restaurante>{
	
	@Query("from Restaurante r join fetch r.cozinha")
	List<Restaurante> findAll();
	
	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

	@Query("from Restaurante where nome like %:nome% and cozinha.id= :id") //usando queires jpql e param para identificar 
	List<Restaurante> consultarPorNome(String nome,@Param("id") Long cozinhaId);
	
	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);
	
	Optional<Restaurante> findFirstByNomeContaining(String nome);
	
	List<Restaurante> findTop2ByNomeContaining(String nome);
	
	int countByCozinhaId(Long cozinhaId);
	
	List<Restaurante> consultarPorNomeExterno(String nome, Long id); //metodo usando mapeamento do xml orm.xml

	
}
