/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db;

import java.util.Set;

/**
 *
 * @author mikehershey
 */
public interface UserLibrary {
	
	public void setEmailAddress(String emailAddress);
	public String getEmailAddress();
	
	public Set<String> getTrackKeys();
	public void addTrackKey(String trackKey);
	public void setTrackKeys(Set<String> trackKeys);
	
}
