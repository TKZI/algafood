package com.ticruz.algafood.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ticruz.algafood.domain.event.PedidoConfirmadoEvent;
import com.ticruz.algafood.domain.model.Pedido;
import com.ticruz.algafood.domain.service.EnvioEmailService;
import com.ticruz.algafood.domain.service.EnvioEmailService.Mensagem;

@Component
public class NotificacaoClientePedidoConfirmadoListener {

	@Autowired
	private EnvioEmailService envioEmail;
	
	
	
	@TransactionalEventListener
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		Pedido pedido = event.getPedido();
		
		var mensagem = Mensagem.builder().assunto(pedido.getRestaurante().getNome() + " pedido confirmado")
				.corpo("pedido-confirmado.html")
				.variavel("pedido", pedido)
				.destinatario("tiagopereiraast@gmail.com")
				.build();
		
		envioEmail.enviar(mensagem);
	}

}
