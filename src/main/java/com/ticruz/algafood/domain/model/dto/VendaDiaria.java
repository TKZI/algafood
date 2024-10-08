package com.ticruz.algafood.domain.model.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VendaDiaria {
	
	
	private Date data;
	private Long totalVendas;
	private BigDecimal totalFaturado;

}
