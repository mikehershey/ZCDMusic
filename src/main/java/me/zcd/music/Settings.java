/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music;

public class Settings {

	public static final String KEY_SEPERATOR = ";;;";
	
	public enum SupportedDatabases {
		AppEngine
	};
	
	public static final SupportedDatabases CURRENT_DATABASE = SupportedDatabases.AppEngine;
	
	public static final String errorEmailRecipiant = "errors@zcd.me";
	
	public enum MusicDiscoveryServices {
		MusicBrainz
	}
	
	public static MusicDiscoveryServices getService() {
		return MusicDiscoveryServices.MusicBrainz;
	}
	
	public static boolean shouldMinify() {
		return true;
	}
	
}
