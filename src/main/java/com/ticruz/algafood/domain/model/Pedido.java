package com.ticruz.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.ticruz.algafood.domain.event.PedidoConfirmadoEvent;
import com.ticruz.algafood.domain.exception.NegocioException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
public class Pedido extends AbstractAggregateRoot<Pedido> {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String codigo;
	
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	
	@Embedded
	private Endereco endereco;
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO;
	
	@CreationTimestamp
	private OffsetDateTime dataCriacao;
	
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;
	
	@ManyToOne
	@JoinColumn(name = "usuario_cliente_id", nullable = false)
	private Usuario cliente;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itens = new ArrayList<>();
	
	public void calcularValorTotal() {
		getItens().forEach(ItemPedido::calcularPrecoTotal);
		this.subtotal = getItens().stream().map(item -> item.getPrecoTotal())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		this.valorTotal = this.subtotal.add(this.taxaFrete);
	}
	
	public void definirFrete() {
		setTaxaFrete(getRestaurante().getTaxaFrete());
	}
	
	public void atribuirPedidosAosItens() {
		getItens().forEach(item -> item.setPedido(this));
	}
	
	public void confirmar() {
		setStatus(StatusPedido.CONFIRMADO);
		setDataConfirmacao(OffsetDateTime.now());
		
		//registrando o evento ao confirmar pedido
		registerEvent(new PedidoConfirmadoEvent(this));
	}
	
	public void cancelar() {
		setStatus(StatusPedido.CANCELADO);
		setDataCancelamento(OffsetDateTime.now());
	}
	
	public void entregue() {
		setStatus(StatusPedido.ENTREGUE);
		setDataEntrega(OffsetDateTime.now());
	}
	
	private void setStatus(StatusPedido statusPedido) {
		if(getStatus().naoPodeAlterarPara(statusPedido)) {
			throw new NegocioException(String.format
					("Pedido de código %s não pode ser alterado de %s para %s",
						getCodigo(),getStatus().getDescricao(),statusPedido.getDescricao()));
		}
		
		this.status = statusPedido;
	}
	
	
	@PrePersist //método de callback do jpa, antes de persistir ele roda esse método
	private void gerarCodigo() {
		setCodigo(UUID.randomUUID().toString());
	}
	

}
