/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.napster;

import me.zcd.music.Secrets;

public class NapsterSettings {

	/*
	 * Member Name	zcdmusicapiuser
	 */
	
	public static final String API_KEY = Secrets.NAPSTER_API_KEY;
	
	public static final String API_URL_BASE = "https://api.napster.com:8443/rest/1.1/";
	
	public static final String COUNTRY_CODE = "US";
	
	public static final String UNENC_API_URL_BASE = "http://api.napster.com:8443/rest/1.1/";
	
}
