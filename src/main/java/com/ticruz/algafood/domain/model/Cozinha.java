package com.ticruz.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ticruz.algafood.core.validation.Groups;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonRootName("cozinha")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)    //@JsonrootName para mudar o nome da classe
@Entity
public class Cozinha {
	
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = Groups.CozinhaId.class)
	private Long id;
	
	
//	@JsonIgnore   anotation que ignora para mostrar
//	@JsonProperty("titulo")     anotation que troca o nome 
//	@NotBlank
	@Column(nullable = false)
	private String nome;
	

	@OneToMany(mappedBy = "cozinha")
	private List<Restaurante> restaurantes = new ArrayList<>();

}
