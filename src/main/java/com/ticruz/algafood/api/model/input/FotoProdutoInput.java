package com.ticruz.algafood.api.model.input;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.ticruz.algafood.core.validation.FileContentType;
import com.ticruz.algafood.core.validation.FileSize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoProdutoInput {
	
	@NotNull
	@FileSize(max = "500KB")
	@FileContentType(allowed = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
	private MultipartFile arquivo;
	
	@NotBlank
	private String descricao;

}
