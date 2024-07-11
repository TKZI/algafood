package com.ticruz.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticruz.algafood.domain.exception.EntidadeEmUsoException;
import com.ticruz.algafood.domain.exception.EstadoNaoEncontradoException;
import com.ticruz.algafood.domain.model.Estado;
import com.ticruz.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

	private static final String MSG_ESTADO_EM_USO = "Estado de código %d não pode ser removido pois está em uso";
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	
	
	public Estado buscarOuFalhar(Long estadoId) {
		
		return estadoRepository.findById(estadoId).orElseThrow(()
				-> new EstadoNaoEncontradoException(estadoId));
	}
	
	@Transactional
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	@Transactional
	public void remover(Long estadoId) {
		
		try {
			estadoRepository.findById(estadoId).orElseThrow(()-> new EmptyResultDataAccessException(
				(0)));
			estadoRepository.deleteById(estadoId); 
			estadoRepository.flush();
				
			
		}catch(EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradoException(
						( estadoId));
		}
			
		 catch ( DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_ESTADO_EM_USO, estadoId));
		}
		
	}
}
