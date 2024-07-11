package com.ticruz.algafood.domain.service;

import java.util.List;

import com.ticruz.algafood.domain.filter.VendaDiariaFilter;
import com.ticruz.algafood.domain.model.dto.VendaDiaria;

public interface VendaQueryService {

	
	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffSet);
}
