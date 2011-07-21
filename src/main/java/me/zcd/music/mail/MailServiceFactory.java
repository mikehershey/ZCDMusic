package me.zcd.music.mail;

/**
 *
 * @author mikehershey
 */
public class MailServiceFactory {
	
	public static MailService getMailService() {
		return new AppEngineMailService();
		//TODO check for app engine if no use other DB
	}
	
}
