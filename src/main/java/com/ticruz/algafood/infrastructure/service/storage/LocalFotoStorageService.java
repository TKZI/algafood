package com.ticruz.algafood.infrastructure.service.storage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.ticruz.algafood.core.storage.StorageProporties;
import com.ticruz.algafood.domain.service.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {

	@Autowired
	private StorageProporties storageProperties;

	@Override
	public void armazenar(NovaFoto novaFoto) {
	
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			
			throw new StorageException("Não foi possível armazenar arquivo", e);
		}
		
	}

	private Path getArquivoPath(String nomeArquivo) {
		return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
	}

	@Override
	public void remover(String nomeArquivo) {
		Path arquivoPath = getArquivoPath(nomeArquivo); 
		
		try {
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
		
	}

	@Override
	public InputStream recuperarFoto(String nomeArquivo) {
		try {
		
			Path arquivoPath = getArquivoPath(nomeArquivo);
			return Files.newInputStream(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível recuperar a arquivo", e);
		}
		
	}

}
