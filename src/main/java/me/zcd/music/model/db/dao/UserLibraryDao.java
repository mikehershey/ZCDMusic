/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db.dao;

import java.util.List;
import me.zcd.music.model.db.UserLibrary;

/**
 *
 * @author mikehershey
 */
public interface UserLibraryDao {

	public UserLibrary persistUserLibrary(UserLibrary library);
	public UserLibrary getUserLibrary(String emailAddress);
	public UserLibrary addTracksToLibrary(List<String> trackKeys, String emailAddress);
	public UserLibrary addTracksToLibrary(String[] trackKeys, String emailAddress);
	public UserLibrary incrementTrackPlayCount(String trackKey, String emailAddress);
	

}
