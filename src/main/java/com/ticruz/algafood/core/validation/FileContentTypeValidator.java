package com.ticruz.algafood.core.validation;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private List<String> allowedContentTypes;
	
	@Override
	public void initialize(FileContentType constraintAnnotation) {
	this.allowedContentTypes = Arrays.asList(constraintAnnotation.allowed());
		
	}
	
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

		return value == null || this.allowedContentTypes.contains(value.getContentType());
		
	}
}
