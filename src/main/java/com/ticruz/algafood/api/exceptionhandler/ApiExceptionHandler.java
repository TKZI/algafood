package com.ticruz.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.ticruz.algafood.core.validation.ValidacaoException;
import com.ticruz.algafood.domain.exception.EntidadeEmUsoException;
import com.ticruz.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.ticruz.algafood.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String USER_MESSAGE = """
            Ocorreu um erro interno inesperado no sistema\
            . Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.\
            """;

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity.status(status).headers(headers).build();
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException exception) {

			return handleInvalidFormatException(exception, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException exception) {
			return handlePropertyBindinException(exception, headers, status, request);
		}

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail).userMessage(USER_MESSAGE)
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(org.springframework.beans.TypeMismatchException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException exception) {
			return handleMethodArgumentTypeMismatch(exception, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		String detail = (
                """
                O parâmetro dde URL '%s' recebeu o valor '%s' \
                que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s\
                """).formatted(
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail).userMessage(USER_MESSAGE)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBindinException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String path = joinPath(ex.getPath());

		String detail = (
                """
                A propriedade '%s'não existe. \
                Corrija ou remova essa propriedade e tente novamente\
                """).formatted(path);
		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail).userMessage(USER_MESSAGE)
				.build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = (
                """
                A propriedade '%s' recebeu o valor '%s' , \
                que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s\
                """).formatted(
                path, ex.getValue(), ex.getTargetType().getSimpleName());
		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail).userMessage(USER_MESSAGE)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class) // metodo que o spring chama para tratar a exception
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e,
			WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = e.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);

	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = e.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = e.getMessage();
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).userMessage(detail)
				.build();

		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);

	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {

		if (body == null) {
			body = Problem.builder().timestamp(OffsetDateTime.now()).title(statusCode.toString())
					.status(statusCode.value()).userMessage(USER_MESSAGE).build();

		} else if (body instanceof String string) {
			body = Problem.builder().timestamp(OffsetDateTime.now()).title(string).status(statusCode.value())
					.userMessage(USER_MESSAGE).build();
		}

		return super.handleExceptionInternal(ex, body, headers, statusCode, request);
	}

	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {

		return Problem.builder().status(status.value()).type(problemType.getUri()).title(problemType.getTitle())
				.detail(detail).timestamp(OffsetDateTime.now());

	}

	private String joinPath(List<Reference> reference) {

		return reference.stream().map((m) -> m.getFieldName()).collect(Collectors.joining("."));
	} // método para criar a stream com map para pegar o nome da referencia e
		// concatenar com .
	// stream pega as informaçoes do getPath que é uma lista e o map pra cada nome
	// ele coleta e concatena com o joining

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

		String detail = "O recurso %s, que você tentou acessar, é inexistente.".formatted(ex.getRequestURL());
		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = USER_MESSAGE;
		ex.printStackTrace();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(USER_MESSAGE).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos. faça o preenchimento correto e tente novamente.";

		// armaneza as violaçoes de constraints de validação
		BindingResult bindingResult = ex.getBindingResult();

		List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream().map(objectError -> {

			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
			String name = objectError.getObjectName();

			if (objectError instanceof FieldError error) {
				name = error.getField();
			}
			return Problem.Object.builder().name(name).userMessage(message).build();
		}).collect(Collectors.toList());

		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail).userMessage(detail)
				.objects(problemObjects).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";

		List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream().map(objectError -> {
			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
			String name = objectError.getObjectName();

			if (objectError instanceof FieldError error) {
				name = error.getField();
			}

			return Problem.Object.builder().name(name).userMessage(message).build();
		}).collect(Collectors.toList());

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).objects(problemObjects)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler({ ValidacaoException.class })
	public ResponseEntity<Object> handleValidacaoException(ValidacaoException ex, WebRequest request) {

		return handleValidationInternal(ex, ex.getBidingResult(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

}

/*
 * como era a estanciação de um problema Problem problem = Problem.builder()
 * .status(status.value())
 * .type("https://algafood.com.br/entidade-nao-encontrada")
 * .title("entidade não encontrada") .detail(e.getMessage()) .build();
 */
