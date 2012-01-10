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

import java.util.HashMap;
import java.util.Map;
import me.zcd.leetml.DefaultSettings;

/**
 *
 * @author mikehershey
 */
public class LeetmlSettings extends DefaultSettings {
	
	@Override
	public boolean isStripFormatting() {
		return true;
	}

	@Override
	public int getTemplateRecacheInterval() {
		return 0;
	}

	@Override
	public String getRootPackage() {
		return "me.zcd.music";
	}
	
	/**
	 * You can use this setting to set the root path that templates are loaded from.
	 * The parent that all templates in this namespace will inherit from is 
	 * templates/[yourpath/]main/ and all other templates will be
	 * templates/[yourpath/]{pagename}/ 
	 * 
	 * NOTE: your paths need to end with / but NOT begin with /.
	 * 
	 * If you wanted the default use the empty string that sets to /templates/[whatever]
	 */
	@Override
	public Map<String,String> getDomainTemplateMapping() {
		Map<String,String> mapping = new HashMap<String,String>();
		//iphone testing and production
		mapping.put("iphone.localhost:8080", "iphone/");
		mapping.put("iphone.music.zcd.me", "iphone/");
		mapping.put("10.0.0.3:8080", "iphone/");
		return mapping;
	}
	
	/**
	 * a domain and package prefix to use when looking up a class.
	 * Example suppose you have com.example.controllers and net.example.controllers
	 * 
	 * To make requests from example.com map to com.example.controllers do
	 * domainMapping.put("example.com","com.example.controllers");
	 * 
	 * If the domain does not exactly match if falls back to the one provided 
	 * as a param in web.xml to URLDispatcher filter.
	 */
	@Override
	public Map<String,String> getDomainControllerMapping() {
		Map<String,String> mapping = new HashMap<String,String>();
		mapping.put("iphone.localhost:8080", "me.zcd.music.iphone.controllers");
		mapping.put("iphone.music.zcd.me", "me.zcd.music.iphone.controllers");
		mapping.put("10.0.0.3:8080", "me.zcd.music.iphone.controllers");
		return mapping;
	}
	
	public String getDefaultTemplateMapping() {
		return "standard/";
	}
	
}
