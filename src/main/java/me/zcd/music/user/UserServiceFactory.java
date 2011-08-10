/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.user;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.zcd.music.Settings;

/**
 *
 * @author mikehershey
 */
public class UserServiceFactory {
	
	static final Logger log = Logger.getLogger(UserServiceFactory.class.getName());
	
	public static UserService getUserService() {
		if(Settings.CURRENT_DATABASE == Settings.SupportedDatabases.AppEngine) {
			return new UserServiceGaeImpl();
		} else {
			log.log(Level.SEVERE, "No suitable User service was found returning null", Thread.currentThread().getStackTrace());
			return null;
		}
	}
	
}
