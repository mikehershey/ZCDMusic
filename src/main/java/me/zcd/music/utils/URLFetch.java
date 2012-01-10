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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;

public class URLFetch {
	
	private static final Log log = LogFactory.getLogger(URLFetch.class);

	public static String getUrl(String pageUrl) {
		StringBuilder data = new StringBuilder();
		log.info("Requesting: " + pageUrl);
		for(int i = 0; i < 5; i++) {
			try {
				URL url = new URL(pageUrl);
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(30000);
				connection.setReadTimeout(30000);
				connection.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					data.append(line);
				}
				reader.close();
				break;
			} catch (Exception e) {
				log.warn("Failed to load page: " + pageUrl, e);
			}
		}
	    String resp = data.toString();
		if(resp.isEmpty()) {
			return null;
		}
	    return resp;
	}
	
	public static String getUrl(String pageUrl, Map<String, String> requestHeaders) {
		StringBuilder data = new StringBuilder();
		log.info("Requesting: " + pageUrl);
		for(int i = 0; i < 5; i++) {
			try {
				URL url = new URL(pageUrl);
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(30000);
				connection.setReadTimeout(30000);
				for( Entry<String, String> entry : requestHeaders.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
				connection.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					data.append(line);
				}
				reader.close();
				break;
			} catch (Exception e) {
				log.warn("Failed to load page: " + pageUrl, e);
			}
		}
		String resp = data.toString();
		if(resp.isEmpty()) {
			return null;
		}
	    return resp;
	}
	
	public static byte[] getUrlData(String pageUrl, Map<String, String> requestHeaders) {
		StringBuilder data = new StringBuilder();
		log.info("Requesting: " + pageUrl);
		byte[] bytes = null;
		for(int i = 0; i < 5; i++) {
			try {
				URL url = new URL(pageUrl);
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(30000);
				connection.setReadTimeout(30000);
				for( Entry<String, String> entry : requestHeaders.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
				connection.connect();
				InputStream is = connection.getInputStream();
				long length = is.available();
				bytes = new byte[(int) length];
				is.read(bytes);
				is.close();
				break;
			} catch (Exception e) {
				log.warn("Failed to load page: " + pageUrl, e);
			}
		}
		String resp = data.toString();
		if(resp.isEmpty()) {
			return null;
		}
	    return bytes;
	}
	
}
