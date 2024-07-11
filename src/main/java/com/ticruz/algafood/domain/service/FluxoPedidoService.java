package com.ticruz.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticruz.algafood.domain.model.Pedido;
import com.ticruz.algafood.domain.repository.PedidoRepository;

@Service
public class FluxoPedidoService {

	@Autowired
	private EmissaoPedidoService pedidoService;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Transactional 
		public void confirmar(String pedidoCodigo) {
		Pedido pedido = pedidoService.buscarOuFalhar(pedidoCodigo);
		pedido.confirmar();
		//chamando save pq precisa para registrar o evento
		pedidoRepository.save(pedido);

	}

	@Transactional
	public void cancelar(String pedidoCodigo) {
		Pedido pedido = pedidoService.buscarOuFalhar(pedidoCodigo);
		pedido.cancelar();

	}

	@Transactional
	public void entregar(String pedidoCodigo) {
		Pedido pedido = pedidoService.buscarOuFalhar(pedidoCodigo);
		pedido.entregue();
	}

}

//var mensagem = Mensagem.builder()
//.assunto(pedido.getRestaurante().getNome() + " pedido confirmado")
//.corpo("pedido-confirmado.html")
//.variavel("pedido", pedido)
//.destinatario("tiagopereiraast@gmail.com")
//.build();
//envioEmail.enviar(mensagem);  usando antes do DDD para enviar o email 