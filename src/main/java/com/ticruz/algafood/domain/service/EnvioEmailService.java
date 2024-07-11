package com.ticruz.algafood.domain.service;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

public interface EnvioEmailService {
	
	void enviar(Mensagem mensagem);
	

	@Getter
	@Builder
	class Mensagem {
		@Singular// do  lombok, invez de passar um set ele cria um unico
		private Set<String> destinatarios;
		
		@NonNull
		private String assunto;
		
		@NonNull //se nao for passado nada ele joga a exception
		private String corpo;
		
		@Singular("variavel")
		private Map<String , Object> variaveis;
	}
}
