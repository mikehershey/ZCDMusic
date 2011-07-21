/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.utils;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import com.google.gson.Gson;

/**
 * Utility class that fetches a json object from the specified url and 
 * uses gson to parse the object into a java object which it returns.
 * @author mikehershey
 *
 */
public class JsonLoader<T> {
	
	Logger log = Logger.getLogger(JsonLoader.class.getName());
	
	/**
	 * Loads the json object at the specified pageUrl parses it into
	 * a java object of the specified type then returns the object.
	 * 
	 * To be able to return an object of the type your expecting 
	 * make the parameter to this function and the type param
	 * the same.
	 * @param pageUrl
	 * @param destinationType
	 * @return 
	 */
	public T getObject(String pageUrl, Type destinationType) {
		String json = URLFetch.getUrl(pageUrl);
		if(json == null) {
			log.warning("Got no response from URLFetch for URL: " + pageUrl);
			return null;
		}
		Gson gson = new Gson();
		return gson.fromJson(json, destinationType);
	}
	
	public T parseJson(String content, Type destinationType) {
		return new Gson().fromJson(content, destinationType);
	}
	
}
