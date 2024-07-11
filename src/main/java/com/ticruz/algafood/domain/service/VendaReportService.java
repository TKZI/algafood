package com.ticruz.algafood.domain.service;

import com.ticruz.algafood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {

	
	byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffSet);
}
