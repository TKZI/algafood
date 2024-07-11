package com.ticruz.algafood.infrastructure.repository;

import static com.ticruz.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.ticruz.algafood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.ticruz.algafood.domain.model.Restaurante;
import com.ticruz.algafood.domain.repository.RestauranteRepository;
import com.ticruz.algafood.domain.repository.RestauranteRepositoryQueries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {
	
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired @Lazy
	private RestauranteRepository restauranteRepository;
	
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		
		var jpql = new StringBuilder();  //utilizando o string builder pra concatenar melhor
		var parametros = new HashMap<String, Object>();
		
		jpql.append("from Restaurante where 0 = 0 ");
		
		if(StringUtils.hasLength(nome)) {   //StringUtils ajuda a verificar se nulo ou vazio
			jpql.append("and nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}
		
		if(taxaFreteInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");
			parametros.put("taxaInicial", taxaFreteInicial);
		}
		
		if(taxaFreteFinal != null) {
			jpql.append("and taxaFrete <= :taxaFinal ");
			parametros.put("taxaFinal", taxaFreteFinal);
		}
		
		
		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);
				
		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));
		
		
		return query.getResultList();
		
	}
	
	@Override
	public List<Restaurante> findCriteria(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();  //builder é a fabrica do criteria 
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class); //usando criteria para criar as jpql
		
		Root<Restaurante> root = criteria.from(Restaurante.class); //criteria from retorna root
		
		var predicates = new ArrayList<Predicate>();
		
		if(StringUtils.hasText(nome)) {
		predicates.add(builder.like(root.get("nome"),"%" + nome + "%")) ; //usa o root para passar a string
		}
		
		if(taxaFreteInicial != null) {
		predicates.add(builder.
				greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial)) ; //cria o maior igual
		}
		
		if(taxaFreteFinal != null) {
		predicates.add(builder.
				lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal)); //cria o menor igual
		}
		
		criteria.where(predicates.toArray(new Predicate[0])); //cada parametro passado para o where ele faz and
		
		TypedQuery<Restaurante> query = manager.createQuery(criteria);  //precisa criar a query
		
		return query.getResultList();
	}

	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		
		return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
	}

	/*usando a lambda para atribuir para cada chave e valor no setParameter
		o setParameter vc expecifica como vai ser os parametros da pesquisa 
	
	/usando o criteria, deixando dinamico com os if, adiciona em um array de predicate
		depois passando para o where e tem que ser array. Pode usar o var também pra criar as variveis */
}	
