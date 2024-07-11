package com.ticruz.algafood.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticruz.algafood.domain.filter.VendaDiariaFilter;
import com.ticruz.algafood.domain.service.VendaQueryService;
import com.ticruz.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfVendaReportService implements VendaReportService {

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffSet) {
		
		try {
			var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");
			
			var parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffSet);
			
			var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
			
				/*precisa passar para o jasperPrint o inputStream que é onde está o arquivo jasper
				precisar passar os paramentros que são o local 
				precisa passar o dataSource que é de onde vem os dados do relatório */
				var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
			
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			throw new ReportException("Não foi possível emitir relatório de vendas diarias" ); 
		}
	}

}
