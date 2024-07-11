package com.ticruz.algafood.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.ticruz.algafood.core.email.EmailProperties;
import com.ticruz.algafood.domain.service.EnvioEmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


public class SmtpEnvioEmailService implements EnvioEmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailProperties emailProperties;
	
	@Autowired
	private Configuration freemarkerConfig;
	
	@Override
	public void enviar(Mensagem mensagem) {
	try {	

		MimeMessage mimeMessage = criarMimeMessage(mensagem);

		//o mail sender tem o metodo send que recebe o mimeMessage
		mailSender.send(mimeMessage);
		
	}catch(Exception e ) {
		throw new EmailException("Não foi possível enviar e-mail", e);
	}
	
	}
	
	protected String processarTemplate(Mensagem mensagem) {
		try {
			Template template = freemarkerConfig.getTemplate(mensagem.getCorpo());
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getVariaveis());
		
		} catch (Exception e) {
			
			throw new EmailException("Não foi possível montar o template do e-mail", e);
		}
		
	}
	
	protected MimeMessage criarMimeMessage(Mensagem mensagem) throws MessagingException {
		String corpo = processarTemplate(mensagem);
		//mailSender tem o metodo para criar o mime
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		//classe para ajudar a criar a mensagem
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
		helper.setFrom(emailProperties.getRemetente());
		helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
		helper.setSubject(mensagem.getAssunto());
		helper.setText(corpo, true);
		
		return mimeMessage;
	}
}
