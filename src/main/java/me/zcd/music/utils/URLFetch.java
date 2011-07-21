package me.zcd.music.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class URLFetch {
	
	private static final Logger log = Logger.getLogger(URLFetch.class.getName());

	public static String getUrl(String pageUrl) {
		StringBuilder data = new StringBuilder();
		log.info("Requesting: " + pageUrl);
		try {
	        URL url = new URL(pageUrl);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            data.append(line);
	        }
	        reader.close();
	
	    } catch (Exception e) {
	    	log.warning("Failed to load page: " + pageUrl);
	    	return null;
	    }
	    String resp = data.toString();
	    return resp;
	}
	
	public static String getUrl(String pageUrl, Map<String, String> requestHeaders) {
		StringBuilder data = new StringBuilder();
		log.info("Requesting: " + pageUrl);
		try {
	        URL url = new URL(pageUrl);
	        URLConnection connection = url.openConnection();
	        for( Entry<String, String> entry : requestHeaders.entrySet()) {
	        	connection.setRequestProperty(entry.getKey(), entry.getValue());
	        }
	        connection.connect();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            data.append(line);
	        }
	        reader.close();
	
	    } catch (Exception e) {
	    	log.warning("Failed to load page: " + pageUrl);
	    	return null;
	    }
	    String resp = data.toString();
	    return resp;
	}
	
	public static byte[] getUrlData(String pageUrl, Map<String, String> requestHeaders) {
		StringBuilder data = new StringBuilder();
		log.info("Requesting: " + pageUrl);
		byte[] bytes = null;
		try {
	        URL url = new URL(pageUrl);
	        URLConnection connection = url.openConnection();
	        for( Entry<String, String> entry : requestHeaders.entrySet()) {
	        	connection.setRequestProperty(entry.getKey(), entry.getValue());
	        }
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        long length = is.available();
	        bytes = new byte[(int) length];
	        is.read(bytes);
	        is.close();
	    } catch (Exception e) {
	    	log.warning("Failed to load page: " + pageUrl);
	    	return null;
	    }
	    String resp = data.toString();
	    return bytes;
	}
	
}
