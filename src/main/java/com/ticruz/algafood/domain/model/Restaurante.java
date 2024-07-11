package com.ticruz.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ticruz.algafood.core.validation.Groups;
import com.ticruz.algafood.core.validation.ValorZeroIncluiDescricao;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@ValorZeroIncluiDescricao(
		valorField = "taxaFrete", descricaoField = "nome",  descricaoObrigatoria="Frete Gratis")
public class Restaurante {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	private String nome;
	
	//@DecimalMin("1")
	@NotNull
//	@TaxaFrete
	@PositiveOrZero
//	@Multiplo(numero = 10)
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
//	@JsonIgnoreProperties("hibernateLazyInitializer")
	
	
	@Valid //permite a validação em cascata dos demais atributos
	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class) //ao inves de validar default usa cadastro restaurante
	@NotNull
	@ManyToOne  // (fetch = FetchType.LAZY) propriedade para gerar menos select
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha;
	
	
	@Embedded	//indica que é de uma classe e é incorporado (que é uma parte da entidaded restaurante)
	private Endereco endereco;
	
	private Boolean ativo = Boolean.TRUE;
	
	private Boolean aberto = Boolean.FALSE;
	
	
	@CreationTimestamp	//vai atribuir a data hora atual no momento que for instanciada
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;
	
	
	@UpdateTimestamp		//atribui uma nova data sempre que atualiza
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;
	
	
	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento", joinColumns = @JoinColumn(name ="restaurante_id"),
	inverseJoinColumns = @JoinColumn(name ="forma_pagamento_id"))
	private Set<FormaPagamento> formasDePagamento = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "restaurante_usuario_responsavel",
	joinColumns = @JoinColumn (name ="restaurante_id"),
	inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
	
	public void ativar() {
		this.ativo = true;
	}
	
	public void inativar() {
		this.ativo = false;
	}
	
	public void abrir() {
		setAberto(true);
	}
	
	public void fechar() {
		setAberto(false);
	}
	
	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return formasDePagamento.remove(formaPagamento);
		
	}
	
	public boolean associarFormaPagamento(FormaPagamento formaPagamento) {
		return formasDePagamento.add(formaPagamento);
		
	}
	
	public Boolean removerResponsavel(Usuario usuario) {
		return getResponsaveis().remove(usuario);
	}
	
	public Boolean adicionarResponsavel(Usuario usuario) {
		return getResponsaveis().add(usuario);
	}
	
	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasDePagamento().contains(formaPagamento);
	}
	
	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
		return !getFormasDePagamento().contains(formaPagamento);
	}
	

}
