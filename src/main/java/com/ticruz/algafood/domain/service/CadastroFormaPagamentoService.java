package com.ticruz.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticruz.algafood.domain.exception.EntidadeEmUsoException;
import com.ticruz.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.ticruz.algafood.domain.model.FormaPagamento;
import com.ticruz.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class CadastroFormaPagamentoService {

	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	private static final String MSG_FORMA_PAGAMENTO_EM_USO 
    = "Forma de pagamento de código %d não pode ser removida, pois está em uso";
	
	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return formaPagamentoRepository.save(formaPagamento);
	}
	
	@Transactional
	public void excluir(Long formaPagamentoId) {
		try {
			formaPagamentoRepository.findById(formaPagamentoId).orElseThrow(() -> 
			new EmptyResultDataAccessException(0));
			formaPagamentoRepository.deleteById(formaPagamentoId);
			formaPagamentoRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new FormaPagamentoNaoEncontradaException(formaPagamentoId);
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(MSG_FORMA_PAGAMENTO_EM_USO.formatted(formaPagamentoId));
		}
	}
	
	public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
		return formaPagamentoRepository.findById(formaPagamentoId)
				.orElseThrow(() ->new FormaPagamentoNaoEncontradaException(formaPagamentoId)); 
	}
	
	
}
