package com.ticruz.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticruz.algafood.domain.exception.CidadeNaoEncontradaException;
import com.ticruz.algafood.domain.exception.EntidadeEmUsoException;
import com.ticruz.algafood.domain.model.Cidade;
import com.ticruz.algafood.domain.model.Estado;
import com.ticruz.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {
	
	private static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";



	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;
	
	public Cidade buscarOuFalhar(Long cidadeId) {
		
		return cidadeRepository.findById(cidadeId).orElseThrow(()->
		new CidadeNaoEncontradaException(cidadeId));
	}
	
	@Transactional
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado =  cadastroEstado.buscarOuFalhar(estadoId);
		
		cidade.setEstado(estado);
		return cidadeRepository.save(cidade);
	}
	
	@Transactional
	public void excluir(Long cidadeId) {
		try {
			cidadeRepository.findById(cidadeId).orElseThrow(() -> 
			new EmptyResultDataAccessException(0));
			cidadeRepository.deleteById(cidadeId);
			cidadeRepository.flush();
				
			
		} catch (EmptyResultDataAccessException e) {
			throw  new CidadeNaoEncontradaException(cidadeId);
			
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException(
					String.format(MSG_CIDADE_EM_USO, cidadeId));
			}
		
	}

}
