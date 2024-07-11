package com.ticruz.algafood.core.validation;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {

	private String valorField;
	private String descricaoField;
	private String descricaoObrigatoria;
	
	
	@Override
	public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
		this.valorField = constraintAnnotation.valorField();
		this.descricaoField = constraintAnnotation.descricaoField();
		this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
	}
	
	
	@Override				//o object representa a classe que está a annotation
	public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
		boolean valido = true;
		
		try {
			//usando o beanUtils para pegar o getTaxaFrete
			BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), valorField)
					.getReadMethod().invoke(objetoValidacao);
			
			//usando o beanUtils para pegar o getNome
			String descricao = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), descricaoField)
					.getReadMethod().invoke(objetoValidacao);
			
			if(valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
				valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
			}
			
			return valido;
			
		}catch(Exception e) {
			throw new ValidationException(e);
		}
	}
	
}
	
