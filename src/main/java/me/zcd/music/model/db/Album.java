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

import java.util.Date;
import java.util.List;

/**
 *
 * @author mikehershey
 */
public interface Album {
	
	public String getKey();
	
	public void setKey(String key);

	public String getName();

	public String getArtistName();
	
	public void setArtistName(String name);

	public String getArtistKey();

	public void setReleaseDate(Date releaseDate);

	public Date getReleaseDate();

	public void setTrackKeys(List<String> tracks);

	public List<String> getTrackKeys();

	public void setAlbumArtKey(String albumArtKey);

	public String getAlbumArtKey();

	public void setName(String name);
	
	public void addTrackKey(String key);
	
}
