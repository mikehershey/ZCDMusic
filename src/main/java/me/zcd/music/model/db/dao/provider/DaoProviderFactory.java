/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
