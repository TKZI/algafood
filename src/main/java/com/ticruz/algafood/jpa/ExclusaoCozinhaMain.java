package com.ticruz.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.ticruz.algafood.AlgafoodApiApplication;
import com.ticruz.algafood.domain.model.Cozinha;
import com.ticruz.algafood.domain.repository.CozinhaRepository;

public class ExclusaoCozinhaMain {
	
		public static void main(String[] args) {
			ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
					.web(WebApplicationType.NONE)
					.run(args);
			
			CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
			
			Cozinha cozinha = new Cozinha();
			cozinha.setId(1L);
			
			cozinhaRepository.deleteById(1L);
		}

}

