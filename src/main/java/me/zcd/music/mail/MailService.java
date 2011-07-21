package me.zcd.music.mail;

import java.io.IOException;

/**
 *
 * @author mikehershey
 */
public interface MailService {
	
	public void send(Message message) throws IOException;
	
}
