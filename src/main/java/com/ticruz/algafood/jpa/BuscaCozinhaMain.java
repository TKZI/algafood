package com.ticruz.algafood.jpa;

import java.util.Optional;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.ticruz.algafood.AlgafoodApiApplication;
import com.ticruz.algafood.domain.model.Cozinha;
import com.ticruz.algafood.domain.repository.CozinhaRepository;

public class BuscaCozinhaMain {
	
	public static void main(String[] args) {
		
		
		ApplicationContext apllicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	
	CozinhaRepository cozinhaRepository = 	apllicationContext.getBean(CozinhaRepository.class );
	
		Optional<Cozinha> cozinha = cozinhaRepository.findById(1L);
		
			System.out.println(cozinha.get());
		
		
		
		
	}
	

}
