package com.ticruz.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticruz.algafood.domain.exception.NegocioException;
import com.ticruz.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.ticruz.algafood.domain.model.Cidade;
import com.ticruz.algafood.domain.model.Cozinha;
import com.ticruz.algafood.domain.model.FormaPagamento;
import com.ticruz.algafood.domain.model.Restaurante;
import com.ticruz.algafood.domain.model.Usuario;
import com.ticruz.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {


	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Autowired
	private CadastroUsuarioService usuarioService;
	
	
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		
		return restauranteRepository.findById(restauranteId).orElseThrow(() -> 
		new RestauranteNaoEncontradoException( restauranteId));
		
	}
	
	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
		restauranteAtual.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
		restauranteAtual.inativar();
	}
	
	@Transactional
	public void ativar(List<Long> restauranteIds) {
		try {
			restauranteIds.forEach(this::ativar);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		// restauranteIds.forEach((e) -> ativar(e)); fiz assim 
	}
	
	@Transactional
	public void inativar(List<Long> restauranteIds) {
		try {
			restauranteIds.forEach(this::inativar);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(),e);
		}
		// restauranteIds.forEach((e) -> inativar(e)); fiz assim 
	}
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		return restauranteRepository.save(restaurante);
		
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		restaurante.removerFormaPagamento(formaPagamento);
		
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		restaurante.associarFormaPagamento(formaPagamento);
		
	}
	
	@Transactional
	public void abrir(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
		
		restauranteAtual.abrir();
	}
	
	@Transactional
	public void fechar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
		restauranteAtual.fechar();
	}
	
	@Transactional
	public void associarUsuario(Long resturanteId, Long usuarioId) {
		Restaurante restauranteAtual = buscarOuFalhar(resturanteId);
		Usuario usuarioAtual = usuarioService.buscarOuFalhar(usuarioId);
		restauranteAtual.adicionarResponsavel(usuarioAtual);
	}
	
	@Transactional
	public void dessassociarUsuario(Long resturanteId, Long usuarioId) {
		Restaurante restauranteAtual = buscarOuFalhar(resturanteId);
		Usuario usuarioAtual = usuarioService.buscarOuFalhar(usuarioId);
		restauranteAtual.removerResponsavel(usuarioAtual);
	}
	
	//no método salvar ele verifica o id da cozinha se existe, caso não ele lança a execessao
	

	
	
	
}
