/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.napster.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import me.zcd.music.napster.NapsterSettings;
import me.zcd.music.napster.api.resources.SessionResponse;
import me.zcd.music.utils.JsonLoader;
import me.zcd.music.utils.URLFetch;

public class Session {

	private static Session instance = new Session();
	
	private Session() {}
	
	public static Session getSession() {
		return instance;
	}
	
	private SessionResponse session;
	
	public boolean establishSession() {
		//https://api.napster.com:8443/rest/1.1/security/createSession?countryCode=[country code]&apiKey=[apiKey]
		StringBuilder requestUrl = new StringBuilder();
		requestUrl.append(NapsterSettings.API_URL_BASE);
		requestUrl.append("security/createSession?countryCode=");
		requestUrl.append(NapsterSettings.COUNTRY_CODE);
		requestUrl.append("&apiKey=");
		requestUrl.append(NapsterSettings.API_KEY);
		requestUrl.append("&format=json");
		JsonLoader<SessionResponse> loader = new JsonLoader<SessionResponse>();
		session = loader.getObject(requestUrl.toString(), SessionResponse.class);
		if(session == null) {
			return false;
		}
		System.out.println(session.getSession().getSessionKey());
		System.out.println(session.getSession().getMinutesUntilExpiry());
		return true;
	}
	
	/**
	 * Wraps url fetcher and injects the cookie setting that is needed to use napster services.
	 * If the current session is not valid a new one will be created for your.
	 * @return
	 */
	public String getAuthenticatedUrl(String url) {
		if(this.session == null || !this.session.isValid()) {
			this.establishSession();
		}
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("Cookie", "sessionKey="+this.session.getSession().getSessionKey());
		return URLFetch.getUrl(url, headers);
	}
	
	public byte[] getAuthenticatedUrlData(String url) {
		if(this.session == null || !this.session.isValid()) {
			this.establishSession();
		}
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("Cookie", "sessionKey="+this.session.getSession().getSessionKey());
		return URLFetch.getUrlData(url, headers);
	}
	
	public static void main(String[] argv) throws UnsupportedEncodingException {
		//test basic session creation
		//Session.getSession().establishSession();
		
		//test loading a page that requires a session.
		String artist = URLEncoder.encode("The fall of troy", "UTF-8");
		String resp = Session.getSession().getAuthenticatedUrl("https://api.napster.com:8443/rest/1.1/search/artists?searchTerm=" + artist + "&format=json");
		System.out.println(resp);
	}
	
}
