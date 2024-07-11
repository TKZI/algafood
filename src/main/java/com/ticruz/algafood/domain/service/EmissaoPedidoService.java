package com.ticruz.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticruz.algafood.domain.exception.NegocioException;
import com.ticruz.algafood.domain.exception.PedidoNaoEncontradoException;
import com.ticruz.algafood.domain.model.Cidade;
import com.ticruz.algafood.domain.model.FormaPagamento;
import com.ticruz.algafood.domain.model.Pedido;
import com.ticruz.algafood.domain.model.Produto;
import com.ticruz.algafood.domain.model.Restaurante;
import com.ticruz.algafood.domain.model.Usuario;
import com.ticruz.algafood.domain.repository.PedidoRepository;

@Service
public class EmissaoPedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuario;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CadastroProdutoService cadastroProduto;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Transactional
	public Pedido emitir(Pedido pedido) {
		validarPedido(pedido);
		validarItens(pedido);
		
		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.calcularValorTotal();
		return pedidoRepository.save(pedido);
	}
	
	
	public void validarPedido(Pedido pedido) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(pedido.getEndereco().getCidade().getId());
		Usuario cliente = cadastroUsuario.buscarOuFalhar(pedido.getCliente().getId());
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(pedido.getRestaurante().getId());
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(pedido.getFormaPagamento().getId());
		
		pedido.getEndereco().setCidade(cidade);
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setFormaPagamento(formaPagamento);
		
		
		if(restaurante.naoAceitaFormaPagamento(formaPagamento)) {
			throw new NegocioException(String.format("Forma de pagamento %s não é aceita por este restaurante",
					formaPagamento.getDescricao()));
		}
	
	
	}
	
	public void validarItens(Pedido pedido) {
		pedido.getItens().forEach(item -> {
			Produto produto = cadastroProduto
					.buscarOuFalhar(pedido.getRestaurante().getId(), item.getProduto().getId());
			
			item.setPedido(pedido);
			item.setProduto(produto);
			item.setPrecoUnitario(produto.getPreco());
		});
	}
	
	
	public Pedido buscarOuFalhar(String	 codigoPedido) {
		return pedidoRepository.findByCodigo(codigoPedido)
				.orElseThrow(() -> new PedidoNaoEncontradoException(codigoPedido));
				
	}

}
