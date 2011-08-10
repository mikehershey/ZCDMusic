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

import java.util.List;

/**
 *
 * @author mikehershey
 */
public interface Artist {
	
	public String getKey();
	
	public void setKey(String key);
	
	public void setTrackKeys(List<String> trackKeys);

	public List<String> getTrackKeys();

	public void setAlbumKeys(List<String> albumKeys);

	public List<String> getAlbumKeys();
	
	public void addAlbumKey(String albumKey);
	
	public void addTrackKey(String trackKey);

	public void setArtistArtKey(String artistArt);

	public String getArtistArtKey();

	public void setGenre(String genre);

	public String getGenre();

	public void setName(String name);
	
	public String getName();
	
}
