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
	
}
