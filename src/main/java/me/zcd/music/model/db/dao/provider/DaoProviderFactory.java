/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db.dao.provider;

import me.zcd.music.Settings;
import me.zcd.music.Settings.SupportedDatabases;

/**
 *
 * @author mikehershey
 */
public class DaoProviderFactory {
	
	private static DaoProvider provider = null;
	
	public static synchronized DaoProvider getProvider() {
		if(provider == null) {
			if(Settings.CURRENT_DATABASE.equals(SupportedDatabases.AppEngine)) {
				provider = new GaeDaoProviderImpl();
			} else {
				throw new IllegalArgumentException("Unsupported CURRENT_DATABASE in use!");
			}
		}
		return provider;
	}
	
}
