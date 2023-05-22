package br.com.ernestoborges.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailOutlook {

	public static boolean enviar(String remetente, String senha, String destinatario, String assunto, String conteudo) {
		// Configurações do servidor de e-mail do Outlook e as propriedades
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp-mail.outlook.com");
		props.put("mail.smtp.port", 587);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		// Criar autenticação
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(remetente, senha);
			}
		};

		// Criar sessão
		Session session = Session.getInstance(props, auth);

		try {
			// Criar mensagem
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(remetente));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			message.setSubject(assunto);
			message.setText(conteudo);

			// Enviar o e-mail
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
