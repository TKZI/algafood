package com.ticruz.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticruz.algafood.domain.model.Cozinha;
import com.ticruz.algafood.domain.model.Restaurante;
import com.ticruz.algafood.domain.repository.CozinhaRepository;
import com.ticruz.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@GetMapping("/cozinhas/por-nome") // nao necessariamente precisa do requestParam devido a interface
	public List<Cozinha> cozinhasPorNome(String nome) {

		return cozinhaRepository.findBynomeContaining(nome);
	}

	@GetMapping("/cozinhas/exists-por-nome")
	public boolean exists(String nome) {
		return cozinhaRepository.existsByNome(nome);
	}

	@GetMapping("/restaurantes/por-taxa-frete")
	public List<Restaurante> restaurantePorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {

		return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	}

	@GetMapping("/restaurantes/por-nome")
	public List<Restaurante> restaurantePorTaxaFrete(String nome, Long cozinhaId) {

		return restauranteRepository.consultarPorNome(nome, cozinhaId);
	}

	@GetMapping("/restaurantes/primeiro-por-nome")
	public Optional<Restaurante> restaurantePrimeiroPorNome(String nome) {
		return restauranteRepository.findFirstByNomeContaining(nome);
	}

	@GetMapping("/restaurantes/top2-por-nome")
	public List<Restaurante> restaurantesTop2PorNome(String nome) {
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}

	@GetMapping("/restaurantes/count-por-nome")
	public int restaurantesCountPorCozinha(Long cozinhaId) {
		return restauranteRepository.countByCozinhaId(cozinhaId);
	}

	@GetMapping("/restaurantes/por-nome-externo")
	public List<Restaurante> restaurantesConsultarPorNome(String nome, Long cozinhaId) {

		return restauranteRepository.consultarPorNomeExterno(nome, cozinhaId);
	}

	@GetMapping("/restaurantes/por-nome-e-frete")
	public List<Restaurante> restaurantesPorNomeFrete(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal) {
		return restauranteRepository.findCriteria(nome, taxaFreteInicial, taxaFreteFinal);
	}

	@GetMapping("/restaurantes/com-frete-gratis")
	public List<Restaurante> restauranteComFreteGratis(String nome) {
//		 var comFreteGratis = new RestauranteComFreteGratisSpec();
//		 var comSemelhante = new RestauranteComNomeSemelhanteSpec(nome);

		return restauranteRepository.findComFreteGratis(nome); // ta importando o metodo static por ter definido em
																// favorites java editor

	}

	@GetMapping("/restaurantes/primeiro")
	public Optional<Restaurante> buscarPrimeiro() {
		return restauranteRepository.buscarPrimeiro();
	}

	@GetMapping("/cozinhas/primeiro")
	public Optional<Cozinha> buscarPrimeiroCozinha(){
		
		return cozinhaRepository.buscarPrimeiro();
	}
}

//usando keyword como find by e o containing para buscar por letra que contenha e por nome 
