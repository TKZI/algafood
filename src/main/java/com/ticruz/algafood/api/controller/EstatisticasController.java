package com.ticruz.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticruz.algafood.domain.filter.VendaDiariaFilter;
import com.ticruz.algafood.domain.model.dto.VendaDiaria;
import com.ticruz.algafood.domain.service.VendaQueryService;
import com.ticruz.algafood.domain.service.VendaReportService;

@RestController
@RequestMapping(path = "estatisticas")
public class EstatisticasController {
	
	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Autowired
	private VendaReportService vendaReportService;
	
	@GetMapping(value ="/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, 
			@RequestParam (required = false, defaultValue = "+00:00") String timeOffSet){
	
		return vendaQueryService.consultarVendasDiarias(filtro, timeOffSet);
	}
	
	@GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> consultarVendasDiariasPdf(VendaDiariaFilter filtro, 
			@RequestParam (required = false, defaultValue = "+00:00") String timeOffSet){
	
		var headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");
		
		byte[] bytesPdf = vendaReportService.emitirVendasDiarias(filtro, timeOffSet);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.headers(headers)
				.body(bytesPdf);
				
	}

}
