/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
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
