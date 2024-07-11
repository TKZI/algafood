package com.ticruz.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticruz.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.ticruz.algafood.domain.model.Permissao;
import com.ticruz.algafood.domain.repository.PermissaoRepository;

@Service
public class CadastroPermissaoService {
	
	@Autowired
	private PermissaoRepository permissaoRepository;

	
	public Permissao buscarOuFalhar(Long permissaoId) {
		
		return permissaoRepository.findById(permissaoId).orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
	}
}
