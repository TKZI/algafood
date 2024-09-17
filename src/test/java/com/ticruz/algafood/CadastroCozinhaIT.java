package com.ticruz.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.ticruz.algafood.domain.model.Cozinha;
import com.ticruz.algafood.domain.repository.CozinhaRepository;
import com.ticruz.algafood.util.DatabaseCleaner;
import com.ticruz.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT) // precisa passar o ambiente pra levantar a aplicação
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIT {
	
	private static final int COZINHA_ID_INEXISTENTE = 100;

	@LocalServerPort	//injeta o numero da porta que foi usado
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository; 
	
	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;

    @BeforeEach
    void setUp() {  //esse método é o método dos testes e executa antes dos testes
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//ajuda a debugar, mostra o logging da requisição 
		
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		//roda antes e permite que nao precise colocar nos testes 
		
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
				"/json/correto/cozinha-chinesa.json");
		
		databaseCleaner.clearTables();
		//usa a classe para limpar o banco de dados de test
		
		prepararDados(); //insere os dados pra test
		
		
	}

    //usando o assured para test de API
    @Test
    void deveRetornarStatus200_QuandoConsultarCozinhas() {
		
		//RestAssured
		given() //dado que..
		//	.basePath("/cozinhas") // no path
		//	.port(port) // na porta
			.accept(ContentType.JSON) //aceitando json
		.when() 		//quando
			.get()   //fizer get
		.then()   //então 
			.statusCode(HttpStatus.OK.value()); //apresentar status 200
	}

    @Test
    void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {

		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
		.body("", hasSize(quantidadeCozinhasCadastradas)); //valida se o corpo responde esse tamanho
		//.body("nome", hasItems("Tailandesa","Americana"));  valida se o corpo tem estes nomes
	}

    @Test
    void deveRetornarStatus201_QuandoCadastrarCozinha() {
			
		given()
			.body(jsonCorretoCozinhaChinesa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}

    @Test
    void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		given()
		.pathParam("cozinhaId", cozinhaAmericana.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(cozinhaAmericana.getNome()));
	}

    @Test
    void deveRetornarEStatus404_QuandoConsultarCozinhaInexistente() {
		given()
		.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	
	
	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
		cozinhaTailandesa.setNome("Tailandesa");
		cozinhaRepository.save(cozinhaTailandesa);
		
		cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(cozinhaAmericana);
		
		quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
	}
}
