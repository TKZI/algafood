package com.ticruz.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder    //com o builder vc tem que construir pra instanciar
public class Problem {
	
	private Integer status;
	private String type;
	private String title;
	private String detail;
	
	private String userMessage;
	private OffsetDateTime timestamp;
	
	private List<Object> objects;
	
	
	@Getter
	@Builder
	public static class Object {
		private String name;
		private String userMessage;
	}
}
