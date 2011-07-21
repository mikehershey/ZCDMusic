package me.zcd.music.mail;

import java.io.IOException;

/**
 *
 * @author mikehershey
 */
public class AppEngineMailService implements MailService {

	@Override
	public void send(Message message) throws IOException {
		com.google.appengine.api.mail.MailService mailService = com.google.appengine.api.mail.MailServiceFactory.getMailService();
		
		com.google.appengine.api.mail.MailService.Message gaeMessage = new com.google.appengine.api.mail.MailService.Message();
		gaeMessage.setTo(message.getTo());
		gaeMessage.setSubject(message.getSubject());
		gaeMessage.setTextBody(message.getBody());
		gaeMessage.setSender(message.getSender());
		
		mailService.send(gaeMessage);
	}
	
}
