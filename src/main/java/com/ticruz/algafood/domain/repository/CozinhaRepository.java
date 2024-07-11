package com.ticruz.algafood.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ticruz.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {

	//o próprio framework implementa em tempo de execução 
	
	List<Cozinha> findBynomeContaining(String nome);
	//para que o próprio jpa crie o método ele "procura" por uma propriedade por isso 
	//	é usado o nome como ao nomear o metodo pois ai ele procura na classe essa propriedade
	//da para usar "findBy + a propriedade
	
	boolean existsByNome(String nome);
}
