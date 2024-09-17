package com.ticruz.algafood;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ticruz.algafood.domain.exception.EntidadeEmUsoException;
import com.ticruz.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.ticruz.algafood.domain.model.Cozinha;
import com.ticruz.algafood.domain.service.CadastroCozinhaService;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
class CadastroCozinhaIntregrationIT {

	@Autowired
	CadastroCozinhaService cadastroCozinha;


    @Test
    void deveAtribuirId_QaundoCadastroCozinhaComDadosCorretos() {
		//primeiro um cenário 
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		//depois uma ação
		novaCozinha = cadastroCozinha.salvar(novaCozinha);
		
		//e no fim uma validação
		 assertThat(novaCozinha).isNotNull();
		 assertThat(novaCozinha.getId()).isNotNull();
		
		
	}

    @Test
    void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		Cozinha cozinha = new Cozinha();
		cozinha.setNome(null);
		
		//passando a exception esperada pela ação realizada na lambda
		ConstraintViolationException erroEsperado = assertThrows(ConstraintViolationException.class, () -> 
		cadastroCozinha.salvar(cozinha));
		
		assertThat(erroEsperado).isNotNull();
		
		
	}

    @Test
    void deveFalhar_QuandoExcluirCozinhaEmUso() {
		
		EntidadeEmUsoException cozinhaEmUso = assertThrows(EntidadeEmUsoException.class, ()
				-> cadastroCozinha.excluir(1L));
		
		
		
		assertThat(cozinhaEmUso).isNotNull();
	}

    @Test
    void deveFalhar_QuandoExcluirCozinhaInexistente() {
		
		//o método lança a exception e atribui no cozinhaNaoEncontrada, se não for o esperado vai falhar
		EntidadeNaoEncontradaException cozinhaNaoEncontrada = assertThrows(EntidadeNaoEncontradaException.class
				, () -> cadastroCozinha.excluir(100L));
		
		assertThat(cozinhaNaoEncontrada).isNotNull();
		
		
	}
	
	
}




