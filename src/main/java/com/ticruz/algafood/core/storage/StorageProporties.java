package com.ticruz.algafood.core.storage;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("algafood.storage")
public class StorageProporties {

	private Local local = new Local();
	
	
	
	@Getter
	@Setter
	public class Local{
		
		private Path diretorioFotos;
	}
}
