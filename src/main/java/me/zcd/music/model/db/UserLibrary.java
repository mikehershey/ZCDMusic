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

import java.util.Map;
import java.util.Set;
import me.zcd.music.model.UserLibraryTrack;

/**
 *
 * @author mikehershey
 */
public interface UserLibrary {
	
	public void setEmailAddress(String emailAddress);
	public String getEmailAddress();
	
	public Set<UserLibraryTrack> getTracks();
	public void addTrack(UserLibraryTrack trackKey);
	public void setTracks(Set<UserLibraryTrack> trackKeys);
	public Map<String, UserLibraryTrack> getIndexedTracks();
	public void setIndexedTracks(Map<String, UserLibraryTrack> trackKeys);
	
}
