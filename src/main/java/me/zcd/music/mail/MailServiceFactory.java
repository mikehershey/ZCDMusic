/**
 * Copyright � 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
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
