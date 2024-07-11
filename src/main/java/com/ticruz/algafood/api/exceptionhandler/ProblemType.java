package com.ticruz.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	DADOS_INVALIDOS("/dados-invalidos", "Dados Inválidos"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel","Mensagem Incompreensivel"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrada","Recurso não encontrada"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação da regra de negócio"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema");
	
	
	
	
	private String title;
	private String uri;
	
	
	ProblemType(String path, String title){
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
		
		
	}

}
