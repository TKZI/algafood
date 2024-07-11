package com.ticruz.algafood.infrastructure.service.query;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ticruz.algafood.domain.filter.VendaDiariaFilter;
import com.ticruz.algafood.domain.model.Pedido;
import com.ticruz.algafood.domain.model.StatusPedido;
import com.ticruz.algafood.domain.model.dto.VendaDiaria;
import com.ticruz.algafood.domain.service.VendaQueryService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffSet) {
		var predicate = new ArrayList<Predicate>();
		
		
		
		//builder para criar a criteria
		var builder = manager.getCriteriaBuilder();
		
		//query para oque vai retornar 
		var query = builder.createQuery(VendaDiaria.class);
		
		//root de onde ira fazer o from no banco
		var root = query.from(Pedido.class);
		
		//função para alterar de utc para outro offSet
		var functionConvertTzDataCriacao = builder.function("convert_tz", Date.class, root.get("dataCriacao"),
				builder.literal("+00:00"), builder.literal(timeOffSet));
		
		//função para data 
		var functionDateDataCriacao = builder.function("date", Date.class, functionConvertTzDataCriacao);
		
		var selection = builder.construct(VendaDiaria.class,
			functionDateDataCriacao,
			builder.count(root.get("id")),
			builder.sum(root.get("valorTotal")));
		
		query.select(selection);
		query.groupBy(functionDateDataCriacao);
		
		if(filtro.getRestauranteId() != null) {
			predicate.add(builder.equal(root.get("restaurante").get("id"), filtro.getRestauranteId()));
		}
		if(filtro.getDataCriacaoInicio() != null) {
			predicate.add(builder.greaterThanOrEqualTo(root.get("dataCriacaoInicio"), filtro.getDataCriacaoInicio()));
		}
		if(filtro.getDataCriacaoFim() != null) {
			predicate.add(builder.lessThanOrEqualTo(root.get("dataCriacaoFim"), filtro.getDataCriacaoFim()));
		}
		
		predicate.add(root.get("status").in(StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));
		query.where(predicate.toArray(new Predicate[0]));
		
		return manager.createQuery(query).getResultList();
	}
}
