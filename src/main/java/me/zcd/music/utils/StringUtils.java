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
		for (String part : parts) {
			if (part.length() < 1) {
				continue;
			}
			sb.append(" ");
			sb.append(part.substring(0, 1).toUpperCase());
			if (part.length() > 1) {
				sb.append(part.substring(1));
			}
		}
		return sb.toString().substring(1);
	}

	public static String stripQuoted(String input) {
		String ret = input.replaceAll("\"[^\"]*\"", "");
		return ret;
	}

	public static String stripSpecialCharacters(String input) {
		//TODO regex this, just didnt feel like loooking up which chars need escaping
		//this is really embarassing, don't judge me.
		input = input.replace("!", "");
		input = input.replace("@", "");
		input = input.replace("#", "");
		input = input.replace("$", "");
		input = input.replace("%", "");
		input = input.replace("^", "");
		input = input.replace("&", "");
		input = input.replace("*", "");
		input = input.replace("(", "");
		input = input.replace(")", "");
		input = input.replace("-", "");
		input = input.replace("_", "");
		input = input.replace("+", "");
		input = input.replace("=", "");
		input = input.replace("[", "");
		input = input.replace("]", "");
		input = input.replace("{", "");
		input = input.replace("}", "");
		input = input.replace("|", "");
		input = input.replace("\\", "");
		input = input.replace("/", "");
		input = input.replace(",", "");
		input = input.replace("<", "");
		input = input.replace(".", "");
		input = input.replace(">", "");
		input = input.replace("?", "");
		input = input.replace("/", "");
		input = input.replace("`", "");
		input = input.replace("~", "");
		return input;
	}

	public static String mostFrequentString(String... ids) {
		Map<String, Integer> freq = new HashMap<String, Integer>();
		for (String id : ids) {
			if (id == null) {
				continue;
			}
			if (!freq.containsKey(id)) {
				freq.put(id, new Integer(1));
			} else {
				Integer i = freq.get(id);
				freq.put(id, i + 1);
			}
		}
		int max = 0;
		String best = null;
		for (Entry<String, Integer> entry : freq.entrySet()) {
			if (entry.getValue() > max) {
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

	/**
	 * This method checks if a String contains only numbers
	 */
	public static boolean containsOnlyNumbers(String str) {
		//It can't contain only numbers if it's null or empty...
		if (str == null || str.length() == 0) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			//If we find a non-digit character we return false.
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
