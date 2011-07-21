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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author mikehershey
 */
public class StringUtils {
	
	public static String formatName(String name) {
		String[] parts = name.split(" ");
		StringBuilder sb = new StringBuilder();
		for(String part : parts) {
			if(part.length() < 2) {
				continue;
			}
			sb.append(" ");
			sb.append(part.substring(0,1).toUpperCase()).append(part.substring(1));
		}
		return sb.toString().substring(1);
	}
	
	public static String stripQuoted(String input) {
		String ret = input.replaceAll("\"[^\"]*\"", "");
		return ret;
	}
	
	public static String mostFrequentString(String... ids) {
		Map<String, Integer> freq = new HashMap<String, Integer>();
		for(String id : ids) {
			if(id == null) {
				continue;
			}
			if(!freq.containsKey(id)) {
				freq.put(id, new Integer(1));
			} else {
				Integer i = freq.get(id);
				freq.put(id, i + 1);
			}
		}
		int max = 0;
		String best = null;
		for(Entry<String, Integer> entry : freq.entrySet()) {
			if(entry.getValue() > max) {
				max = entry.getValue();
				best = entry.getKey();
			}
		}
		return best;
	}
	
	public static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
}
